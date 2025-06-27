package com.polis.db;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

@Tag("db")
class DatabaseManagerTest extends BaseDBTest {

    @Test
    void testConnectionIsOpen() throws SQLException {
        assertFalse(connection.isClosed(), "Connection should be open");
    }

}