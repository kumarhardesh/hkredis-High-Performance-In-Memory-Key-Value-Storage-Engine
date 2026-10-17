package com.hkredis.storage;

public class ValueEntry {
    private final String value;
    private final Long expiryTimeMs; // Absolute epoch timestamp in ms (null if no expiration)

    public ValueEntry(String value, Long expiryTimeMs) {
        this.value = value;
        this.expiryTimeMs = expiryTimeMs;
    }

    public String getValue() {
        return value;
    }

    public boolean isExpired() {
        if (expiryTimeMs == null) {
            return false;
        }
        return System.currentTimeMillis() > expiryTimeMs;
    }
}