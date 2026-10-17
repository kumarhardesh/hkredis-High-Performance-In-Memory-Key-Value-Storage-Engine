package com.hkredis.commands;

import com.hkredis.network.ClientContext;
import java.util.ArrayList;
import java.util.List;

public class ExecCommand {
    public static String execute(ClientContext context) {
        if (!context.isInTransaction()) {
            return "-ERR EXEC without MULTI\r\n";
        }

        List<List<String>> queue = new ArrayList<>(context.getCommandQueue());
        context.resetTransaction(); // Exit transaction state

        StringBuilder responseBuilder = new StringBuilder();
        responseBuilder.append("*").append(queue.size()).append("\r\n");

        for (List<String> commandTokens : queue) {
            String commandName = commandTokens.get(0);
            Command command = CommandRegistry.getCommand(commandName);
            String singleResponse = command.execute(commandTokens);
            responseBuilder.append(singleResponse);
        }

        return responseBuilder.toString();
    }
}