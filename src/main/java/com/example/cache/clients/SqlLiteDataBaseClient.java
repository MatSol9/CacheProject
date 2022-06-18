package com.example.cache.clients;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.slf4j.helpers.MessageFormatter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlLiteDataBaseClient implements DataBaseClient{
    private final String filePath;
    private final int timeout;
    private Connection connection;
    private static final Logger LOGGER = LogManager.getLogger(SqlLiteDataBaseClient.class);

    public SqlLiteDataBaseClient(String filePath, int timeout) {
        this.filePath = filePath;
        this.timeout = timeout;
        retryConnection();
    }

    @Override
    public boolean connectionStatus() {
        try {
            return connection.isValid(timeout);
        } catch (SQLException e) {
            LOGGER.error("Encountered exception while checking for connection: {}", e.getMessage());
            return false;
        }
    }

    @Override
    public boolean retryConnection() {
        LOGGER.info("Starting database connection with file path: {}", filePath);
        try {
            this.connection = DriverManager.getConnection(filePath);
        } catch (SQLException e) {
            LOGGER.error("Encountered error while establishing connection to database: {}" , e.getSQLState());
            return false;
        }
        return true;
    }

    @Override
    public boolean executeInsert(String key, String value) {
        String query = MessageFormatter.format("insert into Items values('{}', '{}')",
                key, value).getMessage();
        LOGGER.info("Inserting key: {}, value: {} into the database using query: {}", key, value, query);
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            statement.executeUpdate(query);
        } catch (SQLException e) {
            LOGGER.error("Error when inserting key: {}, value: {} into database: {}",
                    key, value, e.getSQLState());
            return false;
        }
        return true;
    }

    @Override
    public String executeGet(String key) {
        String query = MessageFormatter.format("select Value from Items where ItemID='{}'",
                key).getMessage();
        LOGGER.info("Getting value from the Database with key: {} and query: {}", key, query);
        try {
            Statement statement = connection.createStatement();
            statement.setQueryTimeout(timeout);
            ResultSet rs = statement.executeQuery(query);
            if(rs.next()) {
                return rs.getString("Value");
            }
            return null;
        } catch (SQLException e) {
            LOGGER.warn("Error while getting the Value from the database with key: {}, encountered exception: {}",
                    key, e.getMessage());
            return null;
        }
    }
}
