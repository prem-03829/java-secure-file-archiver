package com.a_3_group_3.filecompressor.compression;

import java.io.Serializable;

public class HuffmanNode implements Comparable<HuffmanNode>, Serializable {
  private static final long serialVersionUID = 1L;

  public int byteValue;
  public int frequency;
  public HuffmanNode left;
  public HuffmanNode right;

  public HuffmanNode(int byteValue, int frequency) {
    this.byteValue = byteValue;
    this.frequency = frequency;
  }

  public HuffmanNode(int frequency, HuffmanNode left, HuffmanNode right) {
    this.byteValue = -1;
    this.frequency = frequency;
    this.left = left;
    this.right = right;
  }

  @Override
  public int compareTo(HuffmanNode other) {
    return this.frequency - other.frequency;
  }
}
