package com.hkredis.commands;

import java.util.List;

public class PingCommand implements Command {
    @Override
    public String execute(List<String> args) {
        if (args.size() > 1) {
            String message = args.get(1);
            return "$" + message.length() + "\r\n" + message + "\r\n";
        }
        return "+PONG\r\n";
    }
}