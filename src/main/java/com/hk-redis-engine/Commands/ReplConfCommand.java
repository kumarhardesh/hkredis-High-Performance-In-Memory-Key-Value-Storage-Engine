package com.hkredis.commands;

import java.util.List;

public class ReplConfCommand implements Command {
    @Override
    public String execute(List<String> args) {
        // Simple ACK handshake for replication setup
        return "+OK\r\n";
    }
}