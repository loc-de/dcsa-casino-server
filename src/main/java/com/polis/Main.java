package com.polis;

import com.polis.db.DatabaseManager;
import com.polis.handler.AuthHandler;
import com.polis.handler.GameHandler;
import com.polis.handler.UserHandler;
import com.sun.net.httpserver.HttpServer;
import lombok.SneakyThrows;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {
        HttpServer server = HttpServer.create(
                new InetSocketAddress(InetAddress.getByName(null), 18842),
                10
        );

        DatabaseManager dbManager = new DatabaseManager(
                "jdbc:postgresql://13.50.171.67:17239/casino",
                "postgres",
                "abc123"
        );

        ApplicationContext context = new ApplicationContext(dbManager.getConnection());

        server.createContext("/api/auth", new AuthHandler(context));
        server.createContext("/api/game", new GameHandler(context));
        server.createContext("/api/user", new UserHandler(context));
        server.start();
    }

}