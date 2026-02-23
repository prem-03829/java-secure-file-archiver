package com.a_3_group_3.filecompressor.compression;
import java.util.HashMap;
import java.util.Map;

public class HuffmanCodeGenerator {
  public static Map<Integer, String> generateCodes(HuffmanNode root) {
    Map<Integer, String> codeMap = new HashMap<>();
    generateCodesHelper(root, "", codeMap);
    return codeMap;
  }

  private static void generateCodesHelper(HuffmanNode node, String code, Map<Integer, String> codeMap) {
    if (node == null)
      return;
    if (node.left == null && node.right == null) {
      codeMap.put(node.byteValue, code);
      return;
    }
    generateCodesHelper(node.left, code + "0", codeMap);
    generateCodesHelper(node.right, code + "1", codeMap);
  }
}