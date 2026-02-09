package com.a_3_group_3.filecompressor.analysis;

public class ByteFrequencyCounter {

  public static int[] countFrequencies(byte[] data) {
    int[] frequency = new int[256];
    for (byte b : data)
      frequency[b & 0xFF]++;
    return frequency;
  }
}
