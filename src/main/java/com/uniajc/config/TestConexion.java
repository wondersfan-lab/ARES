package com.uniajc.config;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestConexion {
    public static void main(String[] args) {
        System.out.println("--- Conectando a Neon PostgreSQL ---");
        try (Connection con = ConexionPostgresDatabase.getConnection()) {
            System.out.println("¡CONEXION EXITOSA CON NEON!");

            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT current_user, current_database();");
            if (rs.next()) {
                System.out.println("Usuario conectado: " + rs.getString(1));
                System.out.println("Base de datos activa: " + rs.getString(2));
            }
        } catch (Exception e) {
            System.err.println("Fallo al conectar con la base de datos:");
            e.printStackTrace();
        }
    }
}