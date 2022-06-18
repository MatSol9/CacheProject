package com.example.cache.services;

import com.example.cache.clients.DataBaseClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CacheService {
    private final DataBaseClient dataBaseClient;
    private final RedisService redisService;
    private final int redisExpireTime;
    private static final Logger LOGGER = LogManager.getLogger(CacheService.class);

    public CacheService(@Autowired DataBaseClient dataBaseClient, @Autowired RedisService redisService,
                        @Value("${redis.expire:10}") int redisExpireTime) {
        this.dataBaseClient = dataBaseClient;
        this.redisService = redisService;
        this.redisExpireTime = redisExpireTime;
    }

    public String getItemWithCache(String key) {
        LOGGER.info("Checking cache for an item with key: {}", key);
        if (redisService.keyExistsInRedis(key)) {
            String value = redisService.getRedis(key);
            LOGGER.info("Value: {} for key: {} exists in cache", value, key);
            return value;
        } else {
            LOGGER.info("Key: {} not found in cache, executing database search", key);
            String value = dataBaseClient.executeGet(key);
            if (value != null) {
                LOGGER.info("Value: {} for key: {} found in the database, setting it in Redis", value, key);
                if (!redisService.setRedis(key, value)) {
                    LOGGER.warn("Failed to store key: {}, value: {} in Redis", key, value);
                }
                redisService.keyExpire(key, redisExpireTime);
                return value;
            } else {
                LOGGER.info("Value for key: {} not found", key);
                return null;
            }
        }
    }
}
