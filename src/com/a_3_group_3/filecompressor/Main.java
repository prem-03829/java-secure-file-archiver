package com.a_3_group_3.filecompressor;

import com.a_3_group_3.filecompressor.utils.FileUtils;
import java.io.IOException;
import java.util.Scanner;

import com.a_3_group_3.filecompressor.io.FileReaderUtil;

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
      System.out.print("First 5 bytes: ");
      for (int i = 0; i < Math.min(5, data.length); i++) {
        System.out.print(data[i] + " ");
      }
      System.out.println();
      System.out.println("Read Status: SUCCESS");
    } catch (IOException e) {
      System.out.println("Read Status: FAILED");
      System.out.println(e.getMessage());
    }
  }
}