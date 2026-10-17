package com.hkredis.commands;

import java.util.List;

public class EchoCommand implements Command {

    @Override

    public String execute(List<String> args) {
        if (args.size() < 2) {
            return "-ERR worng number of arguments 'echo'command \r\n ";

        }
        String message = args.get(1);
        return "$" + message.length() + "\r\n" + message + "\r\n";

    }
}
