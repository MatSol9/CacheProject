package com.example.cache.services;

import com.example.cache.clients.DataBaseClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

class CacheServiceTest {
    private static final int REDIS_EXPIRE_TIME = 10;
    private static final String KEY = "AA";
    private static final String VALUE = "VALUE";
    private final DataBaseClient dataBaseClient = mock(DataBaseClient.class);
    private final RedisService redisService = mock(RedisService.class);
    private final CacheService cacheService = new CacheService(dataBaseClient, redisService, REDIS_EXPIRE_TIME);

    @Test
    public void shouldReturnValueFromRedisIfValueExistsInRedis() {
        //given
        when(redisService.keyExistsInRedis(KEY)).thenReturn(true);
        when(redisService.getRedis(KEY)).thenReturn(VALUE);
        //when
        String result = cacheService.getItemWithCache(KEY);
        //then
        assertEquals(VALUE, result);
        verifyZeroInteractions(dataBaseClient);
    }

    @Test
    public void shouldReturnValueFromDataBaseIfValueNotInRedis() {
        //given
        when(redisService.keyExistsInRedis(KEY)).thenReturn(false);
        when(dataBaseClient.executeGet(KEY)).thenReturn(VALUE);
        when(redisService.setRedis(KEY, VALUE)).thenReturn(true);
        //when
        String result = cacheService.getItemWithCache(KEY);
        //then
        assertEquals(VALUE, result);
    }

    @Test
    public void shouldReturnNullWhenValueNotInDataBaseAndNotInRedis() {
        //given
        when(redisService.keyExistsInRedis(KEY)).thenReturn(false);
        when(dataBaseClient.executeGet(KEY)).thenReturn(null);
        //when
        String result = cacheService.getItemWithCache(KEY);
        //then
        verify(redisService).keyExistsInRedis(KEY);
        verifyNoMoreInteractions(redisService);
        assertNull(result);
    }
}