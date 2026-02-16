package com.a_3_group_3.filecompressor;

import com.a_3_group_3.filecompressor.utils.FileUtils;
import java.io.IOException;
import java.util.Scanner;
import com.a_3_group_3.filecompressor.analysis.ByteFrequencyCounter;
import com.a_3_group_3.filecompressor.io.FileReaderUtil;
import com.a_3_group_3.filecompressor.compression.HuffmanNode;
import com.a_3_group_3.filecompressor.compression.HuffmanTreeBuilder;

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
      System.out.println("\nByte Frequencies (non-zero):");
      for (int i = 0; i < frequency.length; i++) {
        if (frequency[i] > 0) {
          System.out.println(i + " : " + frequency[i]);
        }
      }
      HuffmanNode root = HuffmanTreeBuilder.buildTree(frequency);
      System.out.println("\nHuffman Tree built successfully.");
      System.out.println("Root Frequency: " + root.frequency);

    } catch (IOException e) {
      System.out.println("Read Status: FAILED");
      System.out.println(e.getMessage());
    }
  }
}