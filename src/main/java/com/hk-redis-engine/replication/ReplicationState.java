package com.hkredis.replication;

import java.util.UUID;

public class ReplicationState {
    private static String role = "master";
    private static final String masterReplId = UUID.randomUUID().toString().replace("-", "");
    private static long masterReplOffset = 0;

    public static String getRole() {
        return role;
    }

    public static void setRole(String newRole) {
        role = newRole;
    }

    public static String getMasterReplId() {
        return masterReplId;
    }

    public static long getMasterReplOffset() {
        return masterReplOffset;
    }

    public static String getInfoReplication() {
        StringBuilder info = new StringBuilder();
        info.append("# Replication\r\n");
        info.append("role:").append(role).append("\r\n");
        info.append("master_replid:").append(masterReplId).append("\r\n");
        info.append("master_repl_offset:").append(masterReplOffset).append("\r\n");

        String infoStr = info.toString();
        return "$" + infoStr.length() + "\r\n" + infoStr + "\r\n";
    }
}