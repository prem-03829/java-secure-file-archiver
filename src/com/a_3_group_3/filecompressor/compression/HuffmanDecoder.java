package com.a_3_group_3.filecompressor.compression;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class HuffmanDecoder {
  public static void decodeFile(String compressedPath,
      HuffmanNode root,
      String outputPath) throws IOException {
    FileInputStream fis = new FileInputStream(compressedPath);
    int padding = fis.read(); // first byte is padding info
    byte[] data = fis.readAllBytes();
    fis.close();
    StringBuilder bitString = new StringBuilder();
    for (byte b : data) {
      String binary = String.format("%8s",
          Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
      bitString.append(binary);
    }
    if (padding > 0) {
      bitString.setLength(bitString.length() - padding);
    }
    FileOutputStream fos = new FileOutputStream(outputPath);
    HuffmanNode current = root;
    for (int i = 0; i < bitString.length(); i++) {
      if (bitString.charAt(i) == '0')
        current = current.left;
      else
        current = current.right;
      if (current.left == null && current.right == null) {
        fos.write(current.byteValue);
        current = root;
      }
    }
    fos.close();
  }
}