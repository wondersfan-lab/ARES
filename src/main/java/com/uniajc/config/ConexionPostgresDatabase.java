package com.uniajc.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexionPostgresDatabase {

    private static final String CONFIG_FILE = "config.properties";

    public static Connection getConnection() throws SQLException {
        Properties props = new Properties();

        try (FileInputStream fis = new FileInputStream(CONFIG_FILE)) {
            props.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("No se encontró o no se pudo leer el archivo " + CONFIG_FILE, e);
        }

        String host = props.getProperty("db.host");
        String port = props.getProperty("db.port", "5432");
        String dbName = props.getProperty("db.name");
        String user = props.getProperty("db.user");
        String password = props.getProperty("db.password");
        String sslMode = props.getProperty("db.sslmode", "require");

        String url = String.format("jdbc:postgresql://%s:%s/%s?sslmode=%s", host, port, dbName, sslMode);

        return DriverManager.getConnection(url, user, password);
    }
}