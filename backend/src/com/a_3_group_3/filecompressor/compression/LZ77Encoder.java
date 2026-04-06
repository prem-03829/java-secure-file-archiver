package com.a_3_group_3.filecompressor.compression;

import java.util.*;

public class LZ77Encoder {
    public static class Token {
        public final int symbol; // 0-255 (Literal), 256+ (Length)
        public final int distance; // 1-32768, 0 if literal

        public Token(int symbol, int distance) {
            this.symbol = symbol;
            this.distance = distance;
        }
    }

    public static List<Token> encode(byte[] data) {
        List<Token> tokens = new ArrayList<>();
        int pos = 0;
        int n = data.length;

        // FIXED: LZ77 Performance (Hash Table lookup to replace naive nested loop)
        int[] head = new int[65536];
        int[] prev = new int[n];
        Arrays.fill(head, -1);

        while (pos < n) {
            int matchLen = 0;
            int matchDist = 0;

            if (pos + 2 < n) {
                int hash = ((data[pos] & 0xFF) << 8) ^ (data[pos + 1] & 0xFF) ^ ((data[pos + 2] & 0xFF) << 4);
                hash &= 0xFFFF;
                int searchStart = Math.max(0, pos - 32768);
                int limit = 100; // max chain length to prevent worst-case slowdown
                
                for (int i = head[hash]; i >= searchStart && limit > 0; i = prev[i]) {
                    limit--;
                    int len = 0;
                    while (len < 258 && pos + len < n && data[i + len] == data[pos + len]) {
                        len++;
                    }
                    if (len >= 3 && len > matchLen) {
                        matchLen = len;
                        matchDist = pos - i;
                        if (len == 258) break;
                    }
                }
                
                prev[pos] = head[hash];
                head[hash] = pos;
            }

            if (matchLen >= 3) {
                // Insert all positions skipped by the match
                for (int i = 1; i < matchLen; i++) {
                    if (pos + i + 2 < n) {
                        int hash = ((data[pos + i] & 0xFF) << 8) ^ (data[pos + i + 1] & 0xFF) ^ ((data[pos + i + 2] & 0xFF) << 4);
                        hash &= 0xFFFF;
                        prev[pos + i] = head[hash];
                        head[hash] = pos + i;
                    }
                }
                tokens.add(new Token(257 + (matchLen - 3), matchDist));
                pos += matchLen;
            } else {
                tokens.add(new Token(data[pos] & 0xFF, 0));
                pos++;
            }
        }
        return tokens;
    }
}
