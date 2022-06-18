package com.example.cache.clients;

public interface DataBaseClient {
    boolean retryConnection();
    boolean connectionStatus();
    boolean executeInsert(String key, String value);
    String executeGet(String key);
}
