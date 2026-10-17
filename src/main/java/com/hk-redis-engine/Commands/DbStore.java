package com.hkredis.storage;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DbStore {
    // ConcurrentHashMap se multi-threaded read/write thread-safe ho jata hai
    private static final Map<String, ValueEntry> store = new ConcurrentHashMap<>();

    public static void set(String key, String value, Long ttlMs) {
        Long expiryTime = null;
        if (ttlMs != null) {
            expiryTime = System.currentTimeMillis() + ttlMs;
        }
        store.put(key, new ValueEntry(value, expiryTime));
    }

    public static String get(String key) {
        ValueEntry entry = store.get(key);
        if (entry == null) {
            return null;
        }

        // Lazy Expiration check
        if (entry.isExpired()) {
            store.remove(key);
            return null;
        }

        return entry.getValue();
    }
}