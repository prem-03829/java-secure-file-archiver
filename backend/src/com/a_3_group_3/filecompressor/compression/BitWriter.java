package com.a_3_group_3.filecompressor.compression;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class BitWriter {

  // NEW: Primary method for DEFLATE-like pipeline
  public static void writeCompressedFile(byte[] rawData, List<LZ77Encoder.Token> tokens, String outputPath, 
                                          byte[] litLens, byte[] distLens) throws IOException {
    
    ByteArrayOutputStream bitStream = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(bitStream);

    // 1. Write Header Metadata
    dos.writeInt(litLens.length);
    dos.write(litLens);
    
    // Re-generate codes for packing
    int[] f = new int[litLens.length];
    for (int i = 0; i < litLens.length; i++) if (litLens[i] > 0) f[i] = 1;
    Map<Integer, String> litCodes = HuffmanCodeGenerator.generateCanonicalCodes(f, new byte[litLens.length]);

    // 2. Bit-pack tokens
    long bitBuf = 0;
    int bitCount = 0;

    for (LZ77Encoder.Token t : tokens) {
        String code = litCodes.get(t.symbol);
        // FIXED: Data Corruption in BitWriter (don't skip, throw error)
        if (code == null) throw new IOException("Missing Huffman code for symbol: " + t.symbol);
        for (char ch : code.toCharArray()) {
            bitBuf = (bitBuf << 1) | (ch == '1' ? 1 : 0);
            if (++bitCount == 8) { dos.write((int)bitBuf); bitBuf = 0; bitCount = 0; }
        }
        if (t.symbol >= 257) {
            int d = t.distance;
            for (int i = 14; i >= 0; i--) {
                bitBuf = (bitBuf << 1) | ((d >> i) & 1);
                if (++bitCount == 8) { dos.write((int)bitBuf); bitBuf = 0; bitCount = 0; }
            }
        }
    }
    
    if (bitCount > 0) dos.write((int)(bitBuf << (8 - bitCount)));
    byte padding = (byte) ((8 - bitCount) % 8);
    
    // IMPROVED: Ensure dos is flushed/closed properly
    dos.flush();
    byte[] compressedData = bitStream.toByteArray();

    // IMPROVED: Added buffering to file writes
    try (DataOutputStream finalOut = new DataOutputStream(new java.io.BufferedOutputStream(new FileOutputStream(outputPath)))) {
      if (compressedData.length + 20 >= rawData.length) {
          finalOut.writeByte(0); // Flag: RAW
          finalOut.write(rawData);
      } else {
          finalOut.writeByte(1); // Flag: COMPRESSED
          finalOut.writeByte(padding);
          finalOut.write(compressedData);
      }
    }
  }

  // REPLACED: Updated signature to match the call in MainGUI
  public static void writeCompressedFile(byte[] rawData, List<LZ77Encoder.Token> tokens, 
                                          String outputPath, byte[] litLens) throws IOException {
      writeCompressedFile(rawData, tokens, outputPath, litLens, new byte[0]);
  }
}
