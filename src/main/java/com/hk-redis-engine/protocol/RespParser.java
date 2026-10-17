package com.hkredis.protocol;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RespParser {

    // RAW Bytes ko Padh kar Java List ["PING"] ya ["ECHO", "hello"] mein convert karta hai
    public static List<String> parseArray(InputStream input) throws Exception {
        int firstByte = input.read();
        if (firstByte == -1) {
            return null; // Client disconnect ho gaya
        }
        // Line read karne ke liye simple buffer
        String line = readLine(input);
        int count = Integer.parseInt(line); // Kitne words hain (e.g. 2)

        List<String> words = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            input.read(); // '$' symbol ko skip karein
            int wordLength = Integer.parseInt(readLine(input)); // Word ki length

            byte[] wordBytes = new byte[wordLength];
            input.read(wordBytes); // Actual word read karein (e.g. ECHO)

            input.read(); // '\r' skip
            input.read(); // '\n' skip

            words.add(new String(wordBytes));
        }

        return words; // Standard Java List return karein
    }

    private static String readLine(InputStream input) throws Exception {
        StringBuilder sb = new StringBuilder();
        int c;
        while ((c = input.read()) != -1) {
            if (c == '\r') {
                input.read(); // '\n' ko drop karein
                break;
            }
            sb.append((char) c);
        }
        return sb.toString();
    }
}
