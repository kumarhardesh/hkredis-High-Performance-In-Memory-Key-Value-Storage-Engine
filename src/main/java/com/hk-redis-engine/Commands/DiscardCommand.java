package com.hkredis.commands;

import com.hkredis.network.ClientContext;

public class DiscardCommand {
    public static String execute(ClientContext context) {
        if (!context.isInTransaction()) {
            return "-ERR DISCARD without MULTI\r\n";
        }
        context.resetTransaction();
        return "+OK\r\n";
    }
}