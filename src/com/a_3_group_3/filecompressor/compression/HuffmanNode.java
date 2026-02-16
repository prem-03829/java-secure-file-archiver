package com.a_3_group_3.filecompressor.compression;

public class HuffmanNode implements Comparable<HuffmanNode> {

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