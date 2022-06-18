package com.example.cache.rest;

import com.example.cache.services.RedisService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RedisController {
    private final RedisService redisService;
    private static final Logger LOGGER = LogManager.getLogger(RedisController.class);

    public RedisController(@Autowired RedisService redisService) {
        this.redisService = redisService;
    }

    @GetMapping("/redis/get")
    public ResponseEntity<String> getKeyValue(@RequestParam(value = "key") String key) {
        LOGGER.info("Received /redis/get request for key: {}", key);
        String result = redisService.getRedis(key);
        if (result==null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(redisService.getRedis(key), HttpStatus.OK);
    }

    @GetMapping("/redis/exists")
    public ResponseEntity<?> keyExistsInRedis(@RequestParam(value = "key") String key) {
        LOGGER.info("Checking if key: {} exists in redis", key);
        if (redisService.keyExistsInRedis(key)) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/redis/set")
    public ResponseEntity<?> setKeyValue(@RequestParam(value="key") String key,
                              @RequestParam(value="value") String value,
                              @RequestParam(value = "expire", defaultValue = "30") String expire) {
        LOGGER.info("Received put request for redis with key: {} and value: {}", key, value);
        boolean result = redisService.setRedis(key, value);
        if (!result) {
            LOGGER.warn("Failed to set key: {} with value: {}", key, value);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        redisService.keyExpire(key,Integer.parseInt(expire));
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
