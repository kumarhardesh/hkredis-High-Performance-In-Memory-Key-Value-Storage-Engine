package com.hkredis.commands;

import java.util.HashMap;
import java.util.Map;

public class CommandRegistry {
    private static final Map<String, Command> commands = new HashMap<>();

    static {
        commands.put("PING", new PingCommand());
        commands.put("ECHO", new EchoCommand());
        commands.put("SET", new SetCommand());
        commands.put("GET", new GetCommand());
        commands.put("INFO", new InfoCommand());
        commands.put("REPLCONF", new ReplConfCommand());
        commands.put("PSYNC", new PsyncCommand());
        commands.put("COMMAND", new CommandCommand());
    }

    public static Command getCommand(String commandName) {
        if (commandName == null) {
            return new UnknownCommand("null");
        }
        Command cmd = commands.get(commandName.toUpperCase());
        return cmd != null ? cmd : new UnknownCommand(commandName);
    }
}