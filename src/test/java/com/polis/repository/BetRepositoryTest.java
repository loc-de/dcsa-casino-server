package com.polis.repository;

import com.polis.db.BaseDBTest;
import com.polis.model.Bet;
import com.polis.model.User;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@Tag("db")
class BetRepositoryTest extends BaseDBTest {

    @Test
    void testInsertBet() {
        UserRepository userRepository = new UserRepository(connection);
        BetRepository betRepository = new BetRepository(connection);

        User user = userRepository.insertUser(new User(null, "username", "pass", BigDecimal.valueOf(50)));

        Bet bet = new Bet(null, user.userId(), 1, BigDecimal.valueOf(10));
        Bet saved = betRepository.insertBet(bet);

        assertNotNull(saved.userId());
        assertEquals(bet.balanceDiff(), saved.balanceDiff());
    }

}