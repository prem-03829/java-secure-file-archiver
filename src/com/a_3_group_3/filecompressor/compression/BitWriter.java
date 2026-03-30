package com.a_3_group_3.filecompressor.compression;

import java.io.FileOutputStream;
import java.io.IOException;

public class BitWriter {

  public static void writeCompressedFile(String bitString, String outputPath) throws IOException {

    int padding = 8 - (bitString.length() % 8);
    if (padding != 8) {
      for (int i = 0; i < padding; i++) {
        bitString += "0";
      }
    } else {
      padding = 0;
    }

    byte[] bytes = new byte[bitString.length() / 8];

    for (int i = 0; i < bitString.length(); i += 8) {
      String byteStr = bitString.substring(i, i + 8);
      bytes[i / 8] = (byte) Integer.parseInt(byteStr, 2);
    }

    try (FileOutputStream fos = new FileOutputStream(outputPath)) {
      fos.write(padding); // store padding info
      fos.write(bytes);
    }
  }
}