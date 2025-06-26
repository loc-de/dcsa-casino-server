package com.polis.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.polis.model.User;
import com.polis.repository.UserRepository;

import java.util.Optional;

public class JwtService {

    private final String SECRET = "secret_abc123";
    private final Algorithm algorithm =  Algorithm.HMAC256(SECRET);
    private final UserRepository userRepository;

    public JwtService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String generate(String username) {
        return JWT.create()
                .withIssuer("my-server")
                .withSubject(username)
                .sign(algorithm);
    }

    public String parse(String token) {
        JWTVerifier verifier = JWT.require(algorithm)
                .withIssuer("my-server")
                .build();
        return verifier.verify(token).getSubject();
    }

    public boolean isValidToken(String token) {
        String username = parse(token);
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.isPresent();
    }

}