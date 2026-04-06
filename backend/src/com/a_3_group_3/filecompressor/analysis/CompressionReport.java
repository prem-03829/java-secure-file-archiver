package com.a_3_group_3.filecompressor.analysis;

import java.util.Date;
import java.util.Map;

public class CompressionReport {
    // REUSED
    public String fileName;
    public String fileType;
    public long originalSize;
    public long compressedSize;
    public Date timestamp;
    public String status;

    // REUSED / EXTENDED
    public double compressionRatio;
    public double spaceSaved;
    public double bitsPerByte;

    // REUSED / EXTENDED
    public int totalSymbols;
    public int uniqueSymbols;
    
    // REUSED / EXTENDED
    public int minCodeLength;
    public int maxCodeLength;
    public double avgCodeLength;

    // REUSED / EXTENDED
    public int matchesCount;
    public int literalsCount;
    public double avgMatchLength;
    public int windowSize = 32768;

    // REUSED
    public int threadsUsed;
    public long executionTime;

    // EXTENDED
    public long totalBitsWritten;
    public int paddingBits;

    // EXTENDED
    public long headerSize;
    public long huffmanTableSize;
    public long totalMetadataSize;

    // REUSED
    public boolean fallbackUsed;
    public String fallbackReason;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========================================\n");
        sb.append("       DETAILED COMPRESSION REPORT      \n");
        sb.append("========================================\n\n");

        sb.append("[1] FILE INFORMATION\n");
        sb.append("Name          : ").append(fileName).append("\n");
        sb.append("Type          : ").append(fileType).append("\n");
        sb.append("Original Size : ").append(originalSize).append(" bytes\n");
        sb.append("Compressed    : ").append(compressedSize).append(" bytes\n");
        sb.append("Timestamp     : ").append(timestamp).append("\n");
        sb.append("Status        : ").append(status).append("\n\n");

        sb.append("[2] SIZE METRICS\n");
        sb.append("Compression Ratio : ").append(String.format("%.2f", compressionRatio)).append(":1\n");
        sb.append("Space Saved       : ").append(String.format("%.2f", spaceSaved)).append("%\n");
        sb.append("Bits Per Byte     : ").append(String.format("%.2f", bitsPerByte)).append("\n\n");

        sb.append("[3] FREQUENCY & HUFFMAN\n");
        sb.append("Total Symbols     : ").append(totalSymbols).append("\n");
        sb.append("Unique Symbols    : ").append(uniqueSymbols).append("\n");
        sb.append("Code Lengths (min/max): ").append(minCodeLength).append(" / ").append(maxCodeLength).append("\n");
        sb.append("Avg Code Length   : ").append(String.format("%.2f", avgCodeLength)).append(" bits\n\n");

        sb.append("[4] LZ77 DETAILS\n");
        sb.append("Matches Count     : ").append(matchesCount).append("\n");
        sb.append("Literals Count    : ").append(literalsCount).append("\n");
        sb.append("Avg Match Length  : ").append(String.format("%.2f", avgMatchLength)).append("\n");
        sb.append("Window Size       : ").append(windowSize).append(" bytes\n\n");

        sb.append("[5] ENCODING & METADATA\n");
        sb.append("Header Size       : ").append(headerSize).append(" bytes\n");
        sb.append("Huffman Table     : ").append(huffmanTableSize).append(" bytes\n");
        sb.append("Total Metadata    : ").append(totalMetadataSize).append(" bytes\n");
        sb.append("Padding Bits      : ").append(paddingBits).append("\n");
        sb.append("Total Bits Written: ").append(totalBitsWritten).append("\n\n");

        sb.append("[6] SYSTEM & EXECUTION\n");
        sb.append("Threads Used      : ").append(threadsUsed).append("\n");
        sb.append("Execution Time    : ").append(executionTime).append(" ms\n");
        sb.append("Fallback Triggered: ").append(fallbackUsed ? "YES (" + fallbackReason + ")" : "NO").append("\n");
        sb.append("========================================\n");
        
        return sb.toString();
    }
}
