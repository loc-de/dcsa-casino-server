package com.polis.service;

import com.polis.model.User;
import com.polis.repository.UserRepository;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;
import java.util.Optional;

@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public boolean hasSufficientBalance(int userId, BigDecimal betAmount) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isEmpty()) {
            return false;
        }
        User user = optionalUser.get();
        return user.balance().compareTo(betAmount) >= 0;
    }
}
