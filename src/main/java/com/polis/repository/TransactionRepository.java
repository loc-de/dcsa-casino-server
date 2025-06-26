package com.polis.repository;

import com.polis.model.Transaction;
import lombok.AllArgsConstructor;

import java.sql.*;

@AllArgsConstructor
public class TransactionRepository {

    private final Connection conn;

    public void insertTransaction(Transaction transaction) {
        try (PreparedStatement st = conn.prepareStatement("""
            insert into transactions(money_amount, user_id) values (?, ?)
            """, Statement.RETURN_GENERATED_KEYS
        )) {
            st.setBigDecimal(1, transaction.moneyAmount());
            st.setInt(2, transaction.userId());
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
