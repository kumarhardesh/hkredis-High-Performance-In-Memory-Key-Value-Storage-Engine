package com.hkredis;

import com.hkredis.commands.*;
import com.hkredis.network.ClientContext;
import com.hkredis.persistence.AofManager;
import com.hkredis.protocol.RespParser;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    private static final int DEFAULT_PORT = 6379;

    public static void main(String[] args) {
        int port = DEFAULT_PORT;

        for (int i = 0; i < args.length; i++) {
            if ("--port".equals(args[i]) && i + 1 < args.length) {
                port = Integer.parseInt(args[i + 1]);
            }
        }

        // Initialize AOF Engine & Restore State
        AofManager.init();
        AofManager.loadAof();

        System.out.println("Starting HK-Redis Engine on port " + port + "...");
        ExecutorService threadPool = Executors.newCachedThreadPool();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                threadPool.submit(() -> handleClient(clientSocket));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadPool.shutdown();
        }
    }

    private static void handleClient(Socket clientSocket) {
        ClientContext context = new ClientContext();

        try (
            InputStream input = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream()
        ) {
            while (true) {
                List<String> commandTokens = RespParser.parseArray(input);
                if (commandTokens == null || commandTokens.isEmpty()) {
                    break;
                }

                String commandName = commandTokens.get(0).toUpperCase();

                if ("MULTI".equals(commandName)) {
                    output.write(MultiCommand.execute(context).getBytes());
                    output.flush();
                    continue;
                } else if ("DISCARD".equals(commandName)) {
                    output.write(DiscardCommand.execute(context).getBytes());
                    output.flush();
                    continue;
                } else if ("EXEC".equals(commandName)) {
                    output.write(ExecCommand.execute(context).getBytes());
                    output.flush();
                    continue;
                }

                if (context.isInTransaction()) {
                    context.queueCommand(commandTokens);
                    output.write("+QUEUED\r\n".getBytes());
                } else {
                    Command command = CommandRegistry.getCommand(commandName);
                    String response = command.execute(commandTokens);
                    output.write(response.getBytes());
                }

                output.flush();
            }
        } catch (Exception e) {
            System.out.println("Client disconnected.");
        } finally {
            try {
                clientSocket.close();
            } catch (Exception ignored) {}
        }
    }
}