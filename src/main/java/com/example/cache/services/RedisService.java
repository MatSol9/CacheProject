package com.example.cache.services;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPooled;

@Service
public class RedisService {
    private final JedisPooled jedisClient;
    private static final Logger LOGGER = LogManager.getLogger(RedisService.class);

    public RedisService(@Autowired JedisPooled jedisClient) {
        this.jedisClient = jedisClient;
    }

    public boolean setRedis(String key, String value) {
        return ("OK".equals(jedisClient.set(key, value)));
    }

    public String getRedis(String key) {
        LOGGER.info("Checking Redis for key: {}", key);
        if (!keyExistsInRedis(key)) {
            LOGGER.info("Key: {} not found in Redis", key);
            return null;
        }
        return jedisClient.get(key);
    }

    public boolean keyExistsInRedis(String key) {
        return jedisClient.exists(key);
    }

    public void keyExpire(String key, int seconds) {
        jedisClient.expire(key, seconds);
    }
}
