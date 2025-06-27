package com.polis.db;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.Connection;
import java.sql.SQLException;

public abstract class BaseDBTest {

    protected static final PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>();

    protected static Connection connection;

    @BeforeAll
    static void setUp() {
        postgres.start();

        DatabaseManager dbManager = new DatabaseManager(
                postgres.getJdbcUrl(),
                postgres.getUsername(),
                postgres.getPassword()
        );

        connection = dbManager.getConnection();

        createTables();
    }

    @AfterAll
    static void tearDown() {
        postgres.stop();
    }

    private static void createTables() {
        try (var st = connection.createStatement()) {
            st.execute("""
                create table if not exists users (
                    user_id serial primary key,
                    username varchar(25) not null unique,
                    hash_password varchar(255) not null,
                    balance decimal(12, 2) default 0.00
                )
            """);

            st.execute("""
                create table if not exists bets(
                       bet_id serial primary key,
                    user_id int not null,
                    game_id int not null,
                    balance_diff decimal(10,2) not null,
                    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
                    FOREIGN KEY (game_id) REFERENCES games(game_id) ON DELETE CASCADE
                )
            """);

            st.execute("""
                create table if not exists transactions(
                     transaction_id serial primary key,
                     money_amount decimal(12, 2) not null,
                     user_id int not null,
                     FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE
                 )
            """);

            st.execute("""
                create table if not exists games(
                    game_id serial primary key,
                    name varchar(50) not null,
                    description TEXT,
                    income decimal(10,2) default 0.00
                )
            """);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
