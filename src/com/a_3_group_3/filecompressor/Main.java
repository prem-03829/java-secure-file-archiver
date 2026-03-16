package com.a_3_group_3.filecompressor;

import com.a_3_group_3.filecompressor.utils.FileUtils;
import com.a_3_group_3.filecompressor.analysis.ByteFrequencyCounter;
import com.a_3_group_3.filecompressor.io.FileReaderUtil;
import com.a_3_group_3.filecompressor.compression.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

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
      if (root == null) {
        System.out.println("File is empty. Nothing to compress.");
        return;
      }
      System.out.println("\nHuffman Tree built successfully.");
      System.out.println("Root Frequency: " + root.frequency);
      Map<Integer, String> codes = HuffmanCodeGenerator.generateCodes(root);
      System.out.println("\nGenerated Huffman Codes:");
      for (Map.Entry<Integer, String> entry : codes.entrySet()) {
        System.out.println(entry.getKey() + " : " + entry.getValue());
      }
      String encodedData = ParallelHuffmanEncoder.parallelEncode(data, codes);
      System.out.println("\nEncoded Data (first 100 bits):");
      if (encodedData.length() > 100) {
        System.out.println(encodedData.substring(0, 100) + "...");
      } else {
        System.out.println(encodedData);
      }
      System.out.println("\nOriginal Size (bits): " + data.length * 8);
      System.out.println("Encoded Size (bits): " + encodedData.length());
      String outputFile = filePath + ".huff";
      BitWriter.writeCompressedFile(encodedData, outputFile);
      File compressed = new File(outputFile);
      System.out.println("\nCompressed File Created: " + outputFile);
      System.out.println("Compressed File Size: " + compressed.length() + " bytes");
    } catch (IOException | InterruptedException | ExecutionException e) {
      System.out.println("Error occurred: " + e.getMessage());
    }
  }
}