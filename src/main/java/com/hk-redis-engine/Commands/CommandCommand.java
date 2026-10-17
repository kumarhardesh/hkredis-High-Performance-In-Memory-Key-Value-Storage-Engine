package com.hkredis.commands;

import java.util.List;

public class CommandCommand implements Command {
    @Override
    public String execute(List<String> args) {
        // Benchmark aur redis-cli client initialization support ke  Empty RESP Array
        return "*0\r\n";
    }
}