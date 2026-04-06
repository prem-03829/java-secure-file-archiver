const express = require("express");
const multer = require("multer");
const { execFile } = require("child_process");
const path = require("path");
const fs = require("fs");
const cors = require("cors");
require("dotenv").config();

const { Pool } = require("pg");

const app = express();
const port = process.env.PORT || 5000;

// ✅ FIXED: Clean DB config (no password hacks, correct SSL handling)
const DATABASE_URL = process.env.DATABASE_URL;

if (!DATABASE_URL) {
  throw new Error("DATABASE_URL is missing");
}

const pool = new Pool({
  connectionString: DATABASE_URL,
  ssl: DATABASE_URL.includes("localhost")
    ? false
    : { rejectUnauthorized: false },
});

// ✅ Logging function
const logOperation = async (data) => {
  const query = `     INSERT INTO logs (
      action, filename, file_type, original_size, compressed_size, 
      status, error_message, time_taken_ms, ip_address, user_agent
    ) VALUES ($1,$2,$3,$4,$5,$6,$7,$8,$9,$10)
  `;

  const values = [
    data.action,
    data.filename,
    data.file_type,
    data.original_size,
    data.compressed_size,
    data.status,
    data.error_message,
    data.time_taken_ms,
    data.ip_address,
    data.user_agent,
  ];

  try {
    await pool.query(query, values);
  } catch (err) {
    console.warn("Database logging failed:", err.message);
  }
};

app.use(cors());
app.use(express.json());

const storage = multer.diskStorage({
  destination: (req, file, cb) => cb(null, "uploads/"),
  filename: (req, file, cb) => {
    const uniqueSuffix = Date.now() + "-" + Math.round(Math.random() * 1e9);
    cb(
      null,
      uniqueSuffix + "-" + file.originalname.replace(/[^a-zA-Z0-9.-]/g, "_"),
    );
  },
});

const upload = multer({ storage });

const JAVA_JAR = path.join(__dirname, "../out/BytePress.jar");
if (!fs.existsSync(JAVA_JAR)) throw new Error("JAR not found: " + JAVA_JAR);

if (!fs.existsSync("uploads")) fs.mkdirSync("uploads");
if (!fs.existsSync("outputs")) fs.mkdirSync("outputs");

const cleanupFiles = async (files) => {
  for (const file of files) {
    if (file && fs.existsSync(file)) {
      try {
        await fs.promises.unlink(file);
      } catch (err) {
        console.error(`Error deleting file ${file}:`, err);
      }
    }
  }
};

app.post("/compress", upload.single("file"), (req, res) => {
  if (!req.file) return res.status(400).send("No file uploaded.");

  const startTime = Date.now();
  const inputPath = req.file.path;
  const outputPath = path.join("outputs", req.file.filename + ".huff");

  const logData = {
    action: "compress",
    filename: req.file.originalname,
    file_type: path.extname(req.file.originalname),
    original_size: req.file.size,
    ip_address: req.headers["x-forwarded-for"] || req.socket.remoteAddress,
    user_agent: req.headers["user-agent"],
  };

  const args = [
    "-Djava.awt.headless=true",
    "-jar",
    JAVA_JAR,
    "compress",
    inputPath,
    outputPath,
  ];

  console.log("Running java with args:", args);

  execFile("java", args, async (error, stdout, stderr) => {
    console.log(`stdout: ${stdout}`);
    console.error(`stderr: ${stderr}`);
    if (error) {
      await cleanupFiles([inputPath, outputPath]);

      logOperation({
        ...logData,
        status: "failure",
        error_message: error.message,
        time_taken_ms: Date.now() - startTime,
      });

      return res.status(500).send("Compression failed.");
    }

    if (fs.existsSync(outputPath)) {
      logOperation({
        ...logData,
        status: "success",
        compressed_size: fs.statSync(outputPath).size,
        time_taken_ms: Date.now() - startTime,
      });

      res.download(outputPath, async () => {
        await cleanupFiles([inputPath, outputPath]);
      });
    } else {
      await cleanupFiles([inputPath]);

      logOperation({
        ...logData,
        status: "failure",
        error_message: "Output file not generated.",
        time_taken_ms: Date.now() - startTime,
      });

      res.status(500).send("Output file not generated.");
    }
  });
});

app.post("/decompress", upload.single("file"), (req, res) => {
  if (!req.file) return res.status(400).send("No file uploaded.");

  const startTime = Date.now();
  const inputPath = req.file.path;
  const outputPath = path.join("outputs", req.file.filename + ".decoded.txt");

  const logData = {
    action: "decompress",
    filename: req.file.originalname,
    file_type: path.extname(req.file.originalname),
    original_size: req.file.size,
    ip_address: req.headers["x-forwarded-for"] || req.socket.remoteAddress,
    user_agent: req.headers["user-agent"],
  };

  const args = [
    "-Djava.awt.headless=true",
    "-jar",
    JAVA_JAR,
    "decompress",
    inputPath,
    outputPath,
  ];

  console.log("Running java with args:", args);

  execFile("java", args, async (error, stdout, stderr) => {
    console.log(`stdout: ${stdout}`);
    console.error(`stderr: ${stderr}`);
    if (error) {
      await cleanupFiles([inputPath, outputPath]);

      logOperation({
        ...logData,
        status: "failure",
        error_message: error.message,
        time_taken_ms: Date.now() - startTime,
      });

      return res.status(500).send("Decompression failed.");
    }

    if (fs.existsSync(outputPath)) {
      logOperation({
        ...logData,
        status: "success",
        compressed_size: fs.statSync(outputPath).size,
        time_taken_ms: Date.now() - startTime,
      });

      res.download(outputPath, async () => {
        await cleanupFiles([inputPath, outputPath]);
      });
    } else {
      await cleanupFiles([inputPath]);

      logOperation({
        ...logData,
        status: "failure",
        error_message: "Output file not generated.",
        time_taken_ms: Date.now() - startTime,
      });

      res.status(500).send("Output file not generated.");
    }
  });
});

app.listen(port, () => {
  console.log(`Bridge server running at http://localhost:${port}`);
});
