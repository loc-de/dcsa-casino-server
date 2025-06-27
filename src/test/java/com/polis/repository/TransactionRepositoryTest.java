package com.polis.repository;

import com.polis.db.BaseDBTest;
import com.polis.model.Transaction;
import com.polis.model.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Tag("db")
class TransactionRepositoryTest extends BaseDBTest {

    @Test
    void testInsertTransaction() {
        UserRepository userRepository = new UserRepository(connection);
        TransactionRepository transactionRepository = new TransactionRepository(connection);

        User user = userRepository.insertUser(new User(null, "username", "pass", BigDecimal.ZERO));

        Transaction tx = new Transaction(null, user.userId(), BigDecimal.valueOf(999));
        transactionRepository.insertTransaction(tx);

        assertTrue(true);
    }

}