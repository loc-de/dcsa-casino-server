package com.polis.repository;

import com.polis.db.BaseDBTest;
import com.polis.model.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Tag("db")
class UserRepositoryTest extends BaseDBTest {

    @Test
    void testInsertAndFindUser() {
        UserRepository userRepository = new UserRepository(connection);

        User user = new User(null, "username", "hashed_password", BigDecimal.valueOf(100));

        User inserted = userRepository.insertUser(user);
        assertNotNull(inserted.userId());
        assertEquals("username", inserted.username());

        var found = userRepository.findByUsername("username");
        assertTrue(found.isPresent());
        assertEquals(inserted.userId(), found.get().userId());

        var foundById = userRepository.findById(inserted.userId());
        assertTrue(foundById.isPresent());

        userRepository.updateBalance(inserted.userId(), BigDecimal.valueOf(50));
        var updated = userRepository.findById(inserted.userId());
        assertTrue(updated.isPresent());
        assertEquals(BigDecimal.valueOf(150), updated.get().balance());
    }

}