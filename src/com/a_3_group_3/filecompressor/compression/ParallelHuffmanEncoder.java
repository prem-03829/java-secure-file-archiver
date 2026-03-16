package com.a_3_group_3.filecompressor.compression;

import java.util.Map;
import java.util.concurrent.*;

public class ParallelHuffmanEncoder {

  public static String parallelEncode(byte[] data, Map<Integer, String> codes)
      throws InterruptedException, ExecutionException {

    int threads = Runtime.getRuntime().availableProcessors();
    ExecutorService executor = Executors.newFixedThreadPool(threads);

    int chunkSize = data.length / threads;
    Future<String>[] futures = new Future[threads];

    for (int i = 0; i < threads; i++) {
      int start = i * chunkSize;
      int end = (i == threads - 1) ? data.length : (i + 1) * chunkSize;

      futures[i] = executor.submit(() -> {

        System.out.println("Thread " + Thread.currentThread().getName()
            + " processing chunk from " + start + " to " + end);

        StringBuilder sb = new StringBuilder();

        for (int j = start; j < end; j++) {
          sb.append(codes.get(data[j] & 0xFF));
        }

        return sb.toString();
      });
    }

    StringBuilder finalEncoded = new StringBuilder();

    for (Future<String> f : futures) {
      finalEncoded.append(f.get());
    }

    executor.shutdown();
    return finalEncoded.toString();
  }
}