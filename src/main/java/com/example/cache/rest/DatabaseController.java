package com.example.cache.rest;

import com.example.cache.clients.DataBaseClient;
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
public class DatabaseController {
    @Autowired
    private final DataBaseClient dataBaseClient;
    private static final Logger LOGGER = LogManager.getLogger(DatabaseController.class);

    public DatabaseController(@Autowired DataBaseClient dataBaseClient) {
        this.dataBaseClient = dataBaseClient;
    }

    @GetMapping("/database/get")
    public ResponseEntity<String> getItemByKey(@RequestParam(value="key") String key) {
        LOGGER.info("Received get request for item from database with key: {}", key);
        String result = dataBaseClient.executeGet(key);
        if(result == null) {
            LOGGER.warn("Item with key: {} not found in the database", key);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        LOGGER.info("Value for key: {} found: {}", key, result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/database/connection/up")
    public ResponseEntity<Boolean> checkIfDatabaseConnectionIsValid() {
        boolean result = dataBaseClient.connectionStatus();
        LOGGER.info("Received request to check if the database connection is valid, result: {}", result);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/database/insert")
    public ResponseEntity<?> insertItemIntoDB(@RequestParam(value="key") String key,
                                              @RequestParam(value="value") String value) {
        LOGGER.info("Inserting value: {} with key: {} into the DataBase", value, key);
        if (dataBaseClient.executeInsert(key, value)) {
            LOGGER.info("Inserting key: {}, value: {} into the DataBase was successful", key, value);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        LOGGER.warn("Failed to insert key: {}, value: {} into the DataBase", key, value);
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PostMapping("/database/connection/retry")
    public ResponseEntity<?> retryDatabaseConnection() {
        LOGGER.info("Retrying database connection");
        boolean result = dataBaseClient.retryConnection();
        if (result) {
            return new ResponseEntity<>(HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
