package com.hkredis.commands;

import java.util.List;

public class UnknownCommand implements Command {

    private final String commandName;

    public UnknownCommand(String commandName) {
        this.commandName = commandName;
    }

    @Override
    public String execute(List<String> args) {
        return "-ERR unknown command '" + commandName + "'\r\n";
    }
}
