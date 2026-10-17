package com.hkredis.commands;

import com.hkredis.storage.DbStore;
import java.util.List;

public class GetCommand implements Command {

    @Override
    public String execute(List<String> args) {
        if (args.size() < 2) {
            return "-ERR wrong number of arguments for 'get' command\r\n";
        }

        String key = args.get(1);
        String value = DbStore.get(key);

        if (value == null) {
            // Redis NULL Bulk String format
            return "$-1\r\n";
        }

        return "$" + value.length() + "\r\n" + value + "\r\n";
    }
}
