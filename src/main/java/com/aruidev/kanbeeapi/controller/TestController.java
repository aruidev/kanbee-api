package com.aruidev.kanbeeapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DatabaseMetaData;

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
            DatabaseMetaData metaData = connection.getMetaData();

            // ✅ SEGURO: Solo información no sensible
            String safeInfo = String.format(
                    "Database connection successful! ✅\n" +
                            "Database: %s\n" +
                            "Driver: %s\n" +
                            "Host: %s\n" +
                            "Schema: %s",
                    metaData.getDatabaseProductName(),
                    metaData.getDriverName(),
                    extractHostFromUrl(metaData.getURL()),
                    connection.getSchema()
            );

            return safeInfo;

        } catch (Exception e) {
            return "Database connection failed! ❌ Error: " + e.getMessage();
        }
    }

    private String extractHostFromUrl(String url) {
        try {
            // Remover protocolo
            String withoutProtocol = url.substring(url.indexOf("://") + 3);

            // Encontrar @ (después de credenciales)
            int atIndex = withoutProtocol.indexOf('@');
            if (atIndex != -1) {
                withoutProtocol = withoutProtocol.substring(atIndex + 1);
            }

            // Extraer host:puerto (antes del /)
            int slashIndex = withoutProtocol.indexOf('/');
            if (slashIndex != -1) {
                return withoutProtocol.substring(0, slashIndex);
            }

            return withoutProtocol;
        } catch (Exception e) {
            return "unknown-host";
        }
    }
}