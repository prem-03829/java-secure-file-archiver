package com.a_3_group_3.filecompressor.compression;
import java.util.Map;

public class HuffmanEncoder {
  public static String encode(byte[] data, Map<Integer, String> codes) {
    StringBuilder encodedData = new StringBuilder();
    for (byte b : data) {
      int value = b & 0xFF;
      encodedData.append(codes.get(value));
    }
    return encodedData.toString();
  }
}