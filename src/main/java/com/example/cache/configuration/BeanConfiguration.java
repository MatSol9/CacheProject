package com.example.cache.configuration;

import com.example.cache.clients.DataBaseClient;
import com.example.cache.clients.SqlLiteDataBaseClient;
import com.example.cache.clients.TestDataBaseClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
public class BeanConfiguration {
    @Value("${redis.ip}")
    private String redisIp;
    @Value("${redis.port}")
    private int redisPort;
    @Value("${database.sqlite.location:}")
    private String sqlitePath;
    @Value("${database.sqlite.timeout:0}")
    private int timeout;

    @Bean
    public JedisPooled jedisClient() {
        return new JedisPooled(redisIp, redisPort);
    }

    @Bean
    @ConditionalOnProperty(prefix = "database.sqlite", name = "enabled", havingValue = "true")
    public DataBaseClient sqLiteDataBaseClient() {
        return new SqlLiteDataBaseClient(sqlitePath, timeout);
    }

    @Bean
    @ConditionalOnProperty(prefix = "database.sqlite", name = "enabled", havingValue = "false")
    public DataBaseClient testDataBaseClient() {
        return new TestDataBaseClient();
    }
}
