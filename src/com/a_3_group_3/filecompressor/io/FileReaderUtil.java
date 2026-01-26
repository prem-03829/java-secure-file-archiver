package com.a_3_group_3.filecompressor.io;

import java.io.FileInputStream;
import java.io.IOException;

public class FileReaderUtil {

  public static byte[] readFile(String filePath) throws IOException {
    try (FileInputStream fis = new FileInputStream(filePath)) {
      return fis.readAllBytes();
    }
  }
}
