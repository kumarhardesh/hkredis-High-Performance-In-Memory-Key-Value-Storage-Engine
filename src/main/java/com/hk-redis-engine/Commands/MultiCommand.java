package com.hkredis.commands;

import com.hkredis.network.ClientContext;
import java.util.List;

public class MultiCommand {
    public static String execute(ClientContext context) {
        if (context.isInTransaction()) {
            return "-ERR MULTI calls cannot be nested\r\n";
        }
        context.startTransaction();
        return "+OK\r\n";
    }
}