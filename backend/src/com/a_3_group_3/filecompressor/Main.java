package com.a_3_group_3.filecompressor;

import com.a_3_group_3.filecompressor.utils.FileUtils;
import com.a_3_group_3.filecompressor.io.FileReaderUtil;
import com.a_3_group_3.filecompressor.compression.*;
import com.a_3_group_3.filecompressor.config.DebugConfig;
import com.a_3_group_3.filecompressor.gui.MainGUI;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Main {
  public static void main(String[] args) {
    if (args.length >= 3) {
      String command = args[0];
      String inputPath = args[1];
      String outputPath = args[2];
      try {
        if (command.equalsIgnoreCase("compress")) {
          compressFile(inputPath, outputPath);
        } else if (command.equalsIgnoreCase("decompress")) {
          decompressFile(inputPath, outputPath);
        }
        System.exit(0);
      } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
        System.exit(1);
      }
    } else {
      try {
        javax.swing.SwingUtilities.invokeLater(() -> {
          try {
            new MainGUI().setVisible(true);
          } catch (Exception e) {
            System.err.println("GUI launch failed: " + e.getMessage());
            System.exit(1);
          }
        });
      } catch (Exception e) {
        System.err.println("Headless mode detected or GUI failed: " + e.getMessage());
        System.exit(1);
      }
    }
  }

  private static void compressFile(String inputPath, String outputPath) throws IOException {
    byte[] data = FileReaderUtil.readFile(inputPath);
    if (data.length == 0) {
        System.out.println("File is empty.");
        return;
    }

    System.out.println("\nFile Name: " + FileUtils.getFileName(inputPath));
    System.out.println("File size: " + data.length + " bytes");

    // 1. LZ77 Pass
    List<LZ77Encoder.Token> tokens = LZ77Encoder.encode(data);
    
    // 2. Canonical Huffman Pass
    int[] freqs = new int[1024]; // Safe size for all symbols
    for (LZ77Encoder.Token t : tokens) {
        freqs[t.symbol]++;
    }
    
    byte[] litLens = new byte[1024];
    HuffmanCodeGenerator.generateCanonicalCodes(freqs, litLens);

    // 3. Smart Write
    BitWriter.writeCompressedFile(data, tokens, outputPath, litLens);
    
    File compressed = new File(outputPath);
    System.out.println("\n--- Compression Summary ---");
    System.out.println("Original Size: " + data.length + " bytes");
    System.out.println("Compressed File: " + compressed.length() + " bytes");
    
    double ratio = ((double) (data.length - compressed.length()) / data.length) * 100;
    System.out.println("Status: " + (compressed.length() < data.length ? "COMPRESSED" : "STORED RAW"));
    System.out.println("Reduction Ratio: " + String.format("%.2f", ratio) + "%");
  }

  private static void decompressFile(String inputPath, String outputPath) throws IOException {
    HuffmanDecoder.decodeFile(inputPath, outputPath);
    System.out.println("\nDecompressed File Created: " + outputPath);
  }
}
