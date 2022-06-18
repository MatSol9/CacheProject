package com.example.cache.rest;

import com.example.cache.services.CacheService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CacheController {
    @Autowired
    private final CacheService cacheService;
    private static final Logger LOGGER = LogManager.getLogger(CacheController.class);

    public CacheController(@Autowired CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/cache/get")
    public ResponseEntity<String> getCachedValue(@RequestParam(value="key") String key) {
        LOGGER.info("Received get request for cached value with key: {}", key);
        String result = cacheService.getItemWithCache(key);
        if (result==null) {
            LOGGER.info("Value with key: {} not found", key);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LOGGER.info("Found value: {} for key: {}", result, key);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
