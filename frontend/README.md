# BytePress - High-Performance LZ77 + Huffman Archiver

BytePress is a full-stack, enterprise-grade file archiver combining the power of a Java-based compression core with a premium React-driven interface. It implements a hybrid compression strategy similar to the DEFLATE algorithm, utilizing LZ77 sliding window dictionaries followed by Canonical Huffman coding.

## 🚀 Features

- **Hybrid Compression Engine**: Implements LZ77 string matching and Canonical Huffman bitstream encoding for high-efficiency lossless storage.
- **Java Core**: High-performance backend capable of handling multi-threaded batch operations.
- **Interactive Huffman Visualizer**: Real-time animation of tree construction and frequency analysis.
- **Full-Stack Integration**: Real-time progress tracking via Node.js bridge to the Java execution core.
- **Premium UI**: Dark-themed, glassmorphic design with interactive neon elements and Abort-ready workflows.

## 🛠️ Tech Stack

- **Backend**: Java 17 (Compression Core)
- **Middleware**: Node.js (Express)
- **Frontend**: React (Vite)
- **Styling**: Tailwind CSS
- **Database**: PostgreSQL (Operational Logging)

## 📦 Getting Started

### Prerequisites
- Java Development Kit (JDK) 17+
- Node.js (v18+)
- PostgreSQL (Local or Supabase)

### Installation

1. **Backend Setup**:
   ```bash
   cd backend/server
   npm install
   ```

2. **Frontend Setup**:
   ```bash
   cd frontend
   npm install
   ```

3. **Database**:
   Configure `DATABASE_URL` in `backend/server/.env`.

4. **Run**:
   Start the backend and frontend servers to begin compressing.

## 📂 Project Structure

- `backend/src`: Java source for LZ77 and Huffman logic.
- `backend/server`: Node.js Express bridge and API layer.
- `frontend/src`: React application with interactive visualizers.
- `frontend/src/components/UI`: Custom-built glare-ready glassmorphic components.
