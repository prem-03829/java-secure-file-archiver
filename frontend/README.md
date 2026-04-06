# BytePress - Premium Huffman Compression SaaS

BytePress is a high-end, interactive web application demonstrating the power of Huffman Coding compression. Built with a focus on premium UI/UX, smooth animations, and interactive visualizations.

## 🚀 Features

- **Interactive Huffman Visualizer**: Step-by-step animation of frequency calculation, node merging, and tree building.
- **Premium UI**: Dark-themed, glassmorphic design with neon accents.
- **Custom Cursor**: Spring-based trailing cursor with context-aware states.
- **Simulated Compression/Decompression**: Experience the workflow with realistic stats and progress tracking.
- **Responsive Design**: Optimized for mobile, tablet, and desktop.
- **Framer Motion Animations**: Smooth page transitions and scroll-reveal effects.

## 🛠️ Tech Stack

- **Frontend**: React (Vite)
- **Styling**: Tailwind CSS
- **Animations**: Framer Motion
- **Icons**: Lucide React

## 📦 Getting Started

### Prerequisites
- Node.js (v18+)
- npm or yarn

### Installation

1. Install dependencies:
   ```bash
   npm install
   ```

2. Start the development server:
   ```bash
   npm run dev
   ```

3. Build for production:
   ```bash
   npm run build
   ```

## 📂 Project Structure

- `src/components/HuffmanVisualizer`: Core logic and UI for the tree visualization.
- `src/components/Cursor`: Custom interactive cursor implementation.
- `src/hooks/useHuffman.js`: Custom hook managing the Huffman algorithm steps.
- `src/pages`: Home, Compress, Decompress, and Download pages.
- `src/components/UI`: Reusable glassmorphic cards and neon buttons.

## ⚠️ Note
This is a frontend-focused implementation. Compression and decompression are simulated for demonstration purposes, but the Huffman tree logic in the visualizer is 100% accurate.
