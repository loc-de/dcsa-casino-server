package com.polis.db;

import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class DatabaseManager {

    private final Connection connection;

    public DatabaseManager(String connectionString, String username, String password) {
        System.out.println("Connecting to database: " + connectionString);

        try {
            connection = DriverManager.getConnection(connectionString, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
