package com.hkredis.commands;

import com.hkredis.replication.ReplicationState;
import java.util.List;

public class InfoCommand implements Command {
    @Override
    public String execute(List<String> args) {
        if (args.size() > 1 && "replication".equalsIgnoreCase(args.get(1))) {
            return ReplicationState.getInfoReplication();
        }
        return ReplicationState.getInfoReplication();
    }
}