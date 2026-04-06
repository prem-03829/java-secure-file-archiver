package com.a_3_group_3.filecompressor.utils;

import java.io.File;

public class FileUtils {

  public static String getFileName(String filePath) {
    return new File(filePath).getName();
  }

  public static String getFileExtension(String filePath) {
    String name = getFileName(filePath);
    int dotIndex = name.lastIndexOf('.');
    if (dotIndex == -1)
      return "unknown";
    return name.substring(dotIndex + 1);
  }

  public static long getFileSize(String filePath) {
    return new File(filePath).length();
  }
}
