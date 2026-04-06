package com.a_3_group_3.filecompressor.compression;

import java.util.*;

public class HuffmanCodeGenerator {

  // REPLACED: Updated to produce Canonical Huffman bit-depths and codes
  public static Map<Integer, String> generateCanonicalCodes(int[] frequencies, byte[] outLens) {
    PriorityQueue<HuffmanNode> pq = new PriorityQueue<>();
    for (int i = 0; i < frequencies.length; i++) {
        if (frequencies[i] > 0) pq.add(new HuffmanNode(i, frequencies[i]));
    }
    
    if (pq.isEmpty()) return new HashMap<>();
    
    while (pq.size() > 1) {
        HuffmanNode left = pq.poll();
        HuffmanNode right = pq.poll();
        pq.add(new HuffmanNode(left.frequency + right.frequency, left, right));
    }
    
    Map<Integer, Integer> depthMap = new HashMap<>();
    getDepths(pq.poll(), 0, depthMap);
    
    // Convert to canonical representation
    List<Integer> symbols = new ArrayList<>(depthMap.keySet());
    Collections.sort(symbols, (a, b) -> {
        int diff = depthMap.get(a) - depthMap.get(b);
        return (diff != 0) ? diff : Integer.compare(a, b);
    });
    
    Map<Integer, String> canonicalCodes = new HashMap<>();
    int code = 0;
    int lastLen = depthMap.get(symbols.get(0));
    
    for (int symbol : symbols) {
        int curLen = depthMap.get(symbol);
        code <<= (curLen - lastLen);
        String bitStr = Integer.toBinaryString(code);
        while (bitStr.length() < curLen) bitStr = "0" + bitStr;
        canonicalCodes.put(symbol, bitStr);
        outLens[symbol] = (byte) curLen;
        code++;
        lastLen = curLen;
    }
    return canonicalCodes;
  }

  private static void getDepths(HuffmanNode node, int depth, Map<Integer, Integer> map) {
    if (node.left == null && node.right == null) {
        map.put(node.byteValue, depth);
        return;
    }
    if (node.left != null) getDepths(node.left, depth + 1, map);
    if (node.right != null) getDepths(node.right, depth + 1, map);
  }
}
