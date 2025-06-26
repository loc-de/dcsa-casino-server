package com.polis;

import com.polis.repository.BetRepository;
import com.polis.repository.TransactionRepository;
import com.polis.repository.UserRepository;
import com.polis.security.JwtService;
import com.polis.service.UserService;
import lombok.Getter;

import java.sql.Connection;

@Getter
public class ApplicationContext {

    private final UserRepository userRepository;
    private final UserService userService;
    private final BetRepository betRepository;
    private final TransactionRepository transactionRepository;
    private final JwtService jwtService;

    public ApplicationContext(Connection connection) {
        this.userRepository = new UserRepository(connection);
        this.userService = new UserService(userRepository);
        this.betRepository = new BetRepository(connection);
        this.transactionRepository = new TransactionRepository(connection);
        this.jwtService = new JwtService(userRepository);
    }

}
