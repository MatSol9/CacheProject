package com.example.cache.configuration;

import com.example.cache.clients.DataBaseClient;
import com.example.cache.clients.SqlDataBaseClient;
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
    @Value("${database.sql.location:}")
    private String sqlPath;
    @Value("${database.sql.auth.username:}")
    private String username;
    @Value("${database.sql.auth.password:}")
    private String password;
    @Value("${database.sql.auth.enabled: false}")
    private boolean enableAuthorisation;
    @Value("${database.sql.timeout:0}")
    private int timeout;

    @Bean
    public JedisPooled jedisClient() {
        return new JedisPooled(redisIp, redisPort);
    }

    @Bean
    @ConditionalOnProperty(prefix = "database.sql", name = "enabled", havingValue = "true")
    public DataBaseClient sqLiteDataBaseClient() {
        return new SqlDataBaseClient(sqlPath, timeout, username, password, enableAuthorisation);
    }

    @Bean
    @ConditionalOnProperty(prefix = "database.sql", name = "enabled", havingValue = "false")
    public DataBaseClient testDataBaseClient() {
        return new TestDataBaseClient();
    }
}
