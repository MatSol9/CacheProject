package com.example.cache.clients;

public class TestDataBaseClient implements DataBaseClient{
    private static String VALUE = "VALUE";

    @Override
    public boolean retryConnection() {
        return true;
    }

    @Override
    public boolean connectionStatus() {
        return true;
    }

    @Override
    public boolean executeInsert(String key, String value) {
        return true;
    }

    @Override
    public String executeGet(String key) {
        return VALUE;
    }
}
