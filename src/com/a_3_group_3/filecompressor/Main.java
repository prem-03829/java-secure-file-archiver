package com.a_3_group_3.filecompressor;
import com.a_3_group_3.filecompressor.utils.FileUtils;
import java.io.IOException;
import java.util.Scanner;
import com.a_3_group_3.filecompressor.analysis.ByteFrequencyCounter;
import com.a_3_group_3.filecompressor.io.FileReaderUtil;
import com.a_3_group_3.filecompressor.compression.HuffmanNode;
import com.a_3_group_3.filecompressor.compression.HuffmanTreeBuilder;
import com.a_3_group_3.filecompressor.compression.HuffmanCodeGenerator;
import com.a_3_group_3.filecompressor.compression.HuffmanEncoder;
import java.util.Map;

public class Main {
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    System.out.print("Enter the file path: ");
    String filePath = sc.nextLine();
    sc.close();
    try {
      byte[] data = FileReaderUtil.readFile(filePath);
      System.out.println("\nFile Name: " + FileUtils.getFileName(filePath));
      System.out.println("File Type: " + FileUtils.getFileExtension(filePath));
      System.out.println("File size: " + data.length + " bytes");
      System.out.println("Read Status: SUCCESS");
      int[] frequency = ByteFrequencyCounter.countFrequencies(data);
      HuffmanNode root = HuffmanTreeBuilder.buildTree(frequency);
      System.out.println("\nHuffman Tree built successfully.");
      System.out.println("Root Frequency: " + root.frequency);
      Map<Integer, String> codes = HuffmanCodeGenerator.generateCodes(root);
      System.out.println("\nGenerated Huffman Codes:");
      for (Map.Entry<Integer, String> entry : codes.entrySet()) {
        System.out.println(entry.getKey() + " : " + entry.getValue());
      }
      String encodedData = HuffmanEncoder.encode(data, codes);
      System.out.println("\nEncoded Data (first 100 bits):");
      if (encodedData.length() > 100) {
        System.out.println(encodedData.substring(0, 100) + "...");
      } else {
        System.out.println(encodedData);
      }
      System.out.println("\nOriginal Size (bits): " + data.length * 8);
      System.out.println("Encoded Size (bits): " + encodedData.length());

    } catch (IOException e) {
      System.out.println("Read Status: FAILED");
      System.out.println(e.getMessage());
    }
  }
}