package com.hkredis.persistence;

import com.hkredis.commands.Command;
import com.hkredis.commands.CommandRegistry;
import com.hkredis.protocol.RespParser;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class AofManager {
    private static final String AOF_FILE = "appendonly.aof";
    private static PrintWriter writer;

    // Server startup par AOF Writer initialize karna
    public static synchronized void init() {
        try {
            FileWriter fw = new FileWriter(AOF_FILE, true);
            writer = new PrintWriter(new BufferedWriter(fw), true);
        } catch (IOException e) {
            System.err.println("Failed to initialize AOF file: " + e.getMessage());
        }
    }

    // Write command ko AOF file mein RESP format me write karna
    public static synchronized void logCommand(List<String> commandTokens) {
        if (writer == null) return;

        StringBuilder sb = new StringBuilder();
        sb.append("*").append(commandTokens.size()).append("\r\n");
        for (String token : commandTokens) {
            sb.append("$").append(token.length()).append("\r\n").append(token).append("\r\n");
        }

        writer.print(sb.toString());
        writer.flush();
    }

    // Startup par purani commands replay karke database state rebuild karna
    public static void loadAof() {
        File file = new File(AOF_FILE);
        if (!file.exists()) {
            return;
        }

        System.out.println("Loading AOF file for state recovery...");
        try (InputStream input = new FileInputStream(file)) {
            while (input.available() > 0) {
                List<String> commandTokens = RespParser.parseArray(input);
                if (commandTokens == null || commandTokens.isEmpty()) break;

                String commandName = commandTokens.get(0);
                Command command = CommandRegistry.getCommand(commandName);
                command.execute(commandTokens);
            }
            System.out.println("AOF Recovery complete!");
        } catch (Exception e) {
            System.err.println("Error reading AOF file: " + e.getMessage());
        }
    }
}