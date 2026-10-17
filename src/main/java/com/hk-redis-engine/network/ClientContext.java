package com.hkredis.network;

import java.util.ArrayList;
import java.util.List;

public class ClientContext {
    private boolean inTransaction = false;
    private final List<List<String>> commandQueue = new ArrayList<>();

    public boolean isInTransaction() {
        return inTransaction;
    }

    public void startTransaction() {
        this.inTransaction = true;
        this.commandQueue.clear();
    }

    public void resetTransaction() {
        this.inTransaction = false;
        this.commandQueue.clear();
    }

    public void queueCommand(List<String> commandTokens) {
        this.commandQueue.add(commandTokens);
    }

    public List<List<String>> getCommandQueue() {
        return commandQueue;
    }
}