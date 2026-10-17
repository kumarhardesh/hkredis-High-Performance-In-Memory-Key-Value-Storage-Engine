package com.hkredis.commands;

import com.hkredis.persistence.AofManager;
import com.hkredis.storage.DbStore;
import java.util.List;

public class SetCommand implements Command {
    @Override
    public String execute(List<String> args) {
        if (args.size() < 3) {
            return "-ERR wrong number of arguments for 'set' command\r\n";
        }

        String key = args.get(1);
        String value = args.get(2);
        Long ttlMs = null;

        for (int i = 3; i < args.size(); i++) {
            String option = args.get(i).toUpperCase();
            if ("EX".equals(option) && i + 1 < args.size()) {
                try {
                    ttlMs = Long.parseLong(args.get(i + 1)) * 1000;
                } catch (NumberFormatException e) {
                    return "-ERR value is not an integer or out of range\r\n";
                }
                break;
            } else if ("PX".equals(option) && i + 1 < args.size()) {
                try {
                    ttlMs = Long.parseLong(args.get(i + 1));
                } catch (NumberFormatException e) {
                    return "-ERR value is not an integer or out of range\r\n";
                }
                break;
            }
        }

        DbStore.set(key, value, ttlMs);

        // AOF file me command log karein
        AofManager.logCommand(args);

        return "+OK\r\n";
    }
}