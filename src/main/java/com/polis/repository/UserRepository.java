package com.polis.repository;

import com.polis.model.User;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Optional;

@AllArgsConstructor
public class UserRepository {

    private final Connection conn;

    public Optional<User> findByUsername(String username) {
        try (PreparedStatement st = conn.prepareStatement("select * from users where username = ?")) {
            st.setString(1, username);
            ResultSet result = st.executeQuery();
            if (result.next()) {
                User user = new User(
                        result.getInt("user_id"),
                        result.getString("username"),
                        result.getString("hash_password"),
                        result.getBigDecimal("balance")
                );
                return Optional.of(user);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Optional<User> findById(int id) {
        try (PreparedStatement st = conn.prepareStatement("select * from users where user_id = ?")) {
            st.setInt(1, id);
            ResultSet result = st.executeQuery();
            if (result.next()) {
                User user = new User(
                        result.getInt("user_id"),
                        result.getString("username"),
                        result.getString("hash_password"),
                        result.getBigDecimal("balance")
                );
                return Optional.of(user);
            } else {
                return Optional.empty();
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public User insertUser(User user) {
        try (PreparedStatement st = conn.prepareStatement("""
            insert into users(username, hash_password, balance) values (?, ?, ?)
            """, Statement.RETURN_GENERATED_KEYS
        )) {
            st.setString(1, user.username());
            st.setString(2, user.hashPassword());
            st.setBigDecimal(3, user.balance());
            st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            int newId = rs.getInt(1);

            return new User(newId, user.username(), user.hashPassword(), user.balance());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateBalance(int userId, BigDecimal delta) {
        try (PreparedStatement st = conn.prepareStatement("""
            update users
            set balance = balance + ?
            where user_id = ?
        """)) {
            st.setBigDecimal(1, delta);
            st.setInt(2, userId);
            st.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
