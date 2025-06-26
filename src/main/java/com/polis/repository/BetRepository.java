package com.polis.repository;

import com.polis.model.Bet;
import com.polis.model.User;
import lombok.AllArgsConstructor;

import java.sql.*;

@AllArgsConstructor
public class BetRepository {

    private final Connection conn;

    public Bet insertBet(Bet bet) {
        try (PreparedStatement st = conn.prepareStatement("""
            insert into bets(user_id, game_id, balance_diff) values (?, ?, ?)
            """, Statement.RETURN_GENERATED_KEYS
        )) {
            st.setInt(1, bet.userId());
            st.setInt(2, bet.gameId());
            st.setBigDecimal(3, bet.balanceDiff());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            int newId = rs.getInt(1);

            return new Bet(newId, bet.userId(), bet.gameId(), bet.balanceDiff());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
