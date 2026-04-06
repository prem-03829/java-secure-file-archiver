package com.a_3_group_3.filecompressor.compression;

import java.io.*;
import java.util.*;

public class HuffmanDecoder {
  
  public static void decodeFile(String inputPath, String outputPath) throws IOException {
    try (DataInputStream dis = new DataInputStream(new FileInputStream(inputPath))) {
      int flag = dis.readByte();
      
      if (flag == 0) {
        // RAW mode
        // IMPROVED: Buffering to file writes
        try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(outputPath))) {
          dis.transferTo(fos);
        }
        return;
      }
      
      // COMPRESSED mode
      int padding = dis.readByte() & 0xFF;
      
      // 1. Read Metadata (Canonical lengths)
      int litLenCount = dis.readInt();
      byte[] litLens = new byte[litLenCount];
      dis.readFully(litLens);
      
      // Reconstruct Canonical Codes
      Map<Integer, String> codes = HuffmanCodeGenerator.generateCanonicalCodes(freqsFromLens(litLens), new byte[litLenCount]);
      Map<String, Integer> decodeMap = new HashMap<>();
      for (Map.Entry<Integer, String> e : codes.entrySet()) decodeMap.put(e.getValue(), e.getKey());

      // 2. Decode Bitstream -> LZ77 Tokens -> Original Data
      byte[] window = new byte[32768];
      int winPos = 0;
      
      // IMPROVED: Buffering to file writes
      try (BufferedOutputStream fos = new BufferedOutputStream(new FileOutputStream(outputPath))) {
          BitReader br = new BitReader(dis, padding);
          while (br.hasMore()) {
              String currentCode = "";
              Integer symbol = null;
              while (br.hasMore()) {
                  currentCode += br.readBit() ? "1" : "0";
                  symbol = decodeMap.get(currentCode);
                  if (symbol != null) break;
              }
              
              if (symbol == null) break;
              
              if (symbol < 256) {
                  // Literal
                  fos.write(symbol);
                  // FIXED: Integer Overflow risk (using bitwise AND instead of modulo)
                  window[winPos] = (byte) symbol.intValue();
                  winPos = (winPos + 1) & 32767;
              } else {
                  // Match Length
                  int len = (symbol - 257) + 3;
                  int dist = 0;
                  for (int i = 0; i < 15; i++) {
                      dist = (dist << 1) | (br.readBit() ? 1 : 0);
                  }
                  
                  for (int i = 0; i < len; i++) {
                      // FIXED: Integer Overflow risk (using bitwise AND instead of modulo)
                      byte b = window[(winPos - dist) & 32767];
                      fos.write(b);
                      window[winPos] = b;
                      winPos = (winPos + 1) & 32767;
                  }
              }
          }
      }
    }
  }

  private static int[] freqsFromLens(byte[] lens) {
      int[] f = new int[lens.length];
      for (int i = 0; i < lens.length; i++) if (lens[i] > 0) f[i] = 1;
      return f;
  }

  // Helper BitReader class
  private static class BitReader {
      private DataInputStream dis;
      private int currentByte;
      private int bitsLeft;
      private int padding;
      private boolean eof = false;

      public BitReader(DataInputStream dis, int padding) {
          this.dis = dis;
          this.padding = padding;
          this.bitsLeft = 0;
      }

      public boolean readBit() throws IOException {
          if (bitsLeft == 0) {
              currentByte = dis.read();
              if (currentByte == -1) { eof = true; return false; }
              bitsLeft = 8;
          }
          boolean bit = ((currentByte >> (bitsLeft - 1)) & 1) == 1;
          bitsLeft--;
          return bit;
      }

      public boolean hasMore() throws IOException {
          if (eof) return false;
          if (bitsLeft > 0) return true;
          return dis.available() > 0;
      }
  }

  // Original method for compatibility
  public static void decodeFile(String compressedPath, HuffmanNode root, String outputPath) throws IOException {
    decodeFile(compressedPath, outputPath);
  }
}
