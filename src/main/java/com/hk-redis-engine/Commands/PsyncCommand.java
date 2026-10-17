package com.hkredis.commands;

import com.hkredis.replication.ReplicationState;
import java.util.List;

public class PsyncCommand implements Command {
    @Override
    public String execute(List<String> args) {
        // FULLRESYNC <master_replid> <offset>
        String replId = ReplicationState.getMasterReplId();
        long offset = ReplicationState.getMasterReplOffset();
        return "+FULLRESYNC " + replId + " " + offset + "\r\n";
    }
}