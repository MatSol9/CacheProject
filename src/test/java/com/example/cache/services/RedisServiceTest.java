package com.example.cache.services;

import org.junit.jupiter.api.Test;
import redis.clients.jedis.JedisPooled;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RedisServiceTest {
    private final JedisPooled jedisPooled = mock(JedisPooled.class);
    private final RedisService redisService = new RedisService(jedisPooled);
    private static final String KEY = "AA";
    private static final String VALUE = "VALUE";

    @Test
    public void shouldReturnTrueWhenSucceedingToSetValueInRedis() {
        //given
        when(jedisPooled.set(KEY, VALUE)).thenReturn("OK");
        //when
        boolean result = redisService.setRedis(KEY, VALUE);
        //then
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenFailedToSetValueInRedis() {
        //given
        when(jedisPooled.set(KEY, VALUE)).thenReturn("FAILED");
        //when
        boolean result = redisService.setRedis(KEY, VALUE);
        //then
        assertFalse(result);
    }

    @Test
    public void shouldReturnTrueWhenCheckingIfKeyExistsInRedisAndItExists() {
        //given
        when(jedisPooled.exists(KEY)).thenReturn(true);
        //when
        boolean result = redisService.keyExistsInRedis(KEY);
        //then
        assertTrue(result);
    }

    @Test
    public void shouldReturnFalseWhenCheckingIfKeyExistsInRedisAndItDoesNotExist() {
        //given
        when(jedisPooled.exists(KEY)).thenReturn(false);
        //when
        boolean result = redisService.keyExistsInRedis(KEY);
        //then
        assertFalse(result);
    }

    @Test
    public void shouldReturnValueWhenGettingValueFromRedisForKey() {
        //given
        when(jedisPooled.exists(KEY)).thenReturn(true);
        when(jedisPooled.get(KEY)).thenReturn(VALUE);
        //when
        String result = redisService.getRedis(KEY);
        //then
        assertEquals(VALUE, result);
    }

    @Test
    public void shouldReturnNullWhenGettingValueFromRedisForKeyAndValueDoesNotExist() {
        //given
        when(jedisPooled.exists(KEY)).thenReturn(false);
        //when
        String result = redisService.getRedis(KEY);
        //then
        assertNull(result);
    }
}