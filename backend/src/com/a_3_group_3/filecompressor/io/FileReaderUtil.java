package com.a_3_group_3.filecompressor.io;

import java.io.FileInputStream;
import java.io.IOException;

public class FileReaderUtil {

  public static byte[] readFile(String filePath) throws IOException {
    java.io.File file = new java.io.File(filePath);
    long length = file.length();
    
    // FIXED: Memory-safe check before allocation
    if (length > 100 * 1024 * 1024) { // Limit to 100MB for safety
        throw new IOException("File exceeds 100MB limit for in-memory processing.");
    }

    byte[] data = new byte[(int) length];
    try (java.io.FileInputStream fis = new java.io.FileInputStream(file);
         java.io.BufferedInputStream bis = new java.io.BufferedInputStream(fis)) {
      
      // IMPROVED: Buffered streaming read
      int bytesRead = 0;
      while (bytesRead < length) {
          int count = bis.read(data, bytesRead, (int) length - bytesRead);
          if (count == -1) break;
          bytesRead += count;
      }
    }
    return data;
  }
}
