package com.aruidev.kanbeeapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;

@RestController
public class TestController {

    @Autowired
    private DataSource dataSource;

    @GetMapping("/test")
    public String test() {
        return "Kanbee API is working! 🐝";
    }

    @GetMapping("/test/db")
    public String testDatabase() {
        try (Connection connection = dataSource.getConnection()) {
            return "Database connection successful! ✅ Connected to: " +
                    connection.getMetaData().getURL();
        } catch (Exception e) {
            return "Database connection failed! ❌ Error: " + e.getMessage();
        }
    }
}