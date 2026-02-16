package com.a_3_group_3.filecompressor.compression;

import java.util.PriorityQueue;

public class HuffmanTreeBuilder {
  public static HuffmanNode buildTree(int[] frequency) {
    PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
    for (int i = 0; i < frequency.length; i++) {
      if (frequency[i] > 0) {
        pq.add(new HuffmanNode(i, frequency[i]));
      }
    }
    if (pq.isEmpty())
      return null;
    while (pq.size() > 1) {
      HuffmanNode left = pq.poll();
      HuffmanNode right = pq.poll();
      HuffmanNode parent = new HuffmanNode(
          left.frequency + right.frequency,
          left,
          right);
      pq.add(parent);
    }
    return pq.poll();
  }
}