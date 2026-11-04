package com.uniajc.dao;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Paquete;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PaqueteDAO implements BaseDAO<Paquete> {

    private static final String SCHEMA = "ares.";

    @Override
    public boolean insertar(Paquete paquete) {
        String sql = "INSERT INTO " + SCHEMA + "paquete (envio_id, peso_kg, volumen, descripcion) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, paquete.getEnvioId());
            pstmt.setDouble(2, paquete.getPesoKg());
            pstmt.setDouble(3, paquete.getVolumen());
            pstmt.setString(4, paquete.getDescripcion());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Paquete paquete) {
        String sql = "UPDATE " + SCHEMA + "paquete SET envio_id = ?, peso_kg = ?, volumen = ?, descripcion = ? WHERE id_paquete = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, paquete.getEnvioId());
            pstmt.setDouble(2, paquete.getPesoKg());
            pstmt.setDouble(3, paquete.getVolumen());
            pstmt.setString(4, paquete.getDescripcion());
            pstmt.setInt(5, paquete.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + SCHEMA + "paquete WHERE id_paquete = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Paquete buscarPorId(int id) {
        String sql = "SELECT * FROM " + SCHEMA + "paquete WHERE id_paquete = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearPaquete(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Paquete> obtenerPorEnvioId(int envioId) {
        String sql = "SELECT * FROM " + SCHEMA + "paquete WHERE envio_id = ?";
        List<Paquete> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, envioId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPaquete(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    @Override
    public List<Paquete> listarTodos() {
        String sql = "SELECT * FROM " + SCHEMA + "paquete ORDER BY id_paquete ASC";
        List<Paquete> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearPaquete(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Paquete mapearPaquete(ResultSet rs) throws SQLException {
        Paquete p = new Paquete();
        p.setId(rs.getInt("id_paquete"));
        p.setEnvioId(rs.getInt("envio_id"));
        p.setPesoKg(rs.getDouble("peso_kg"));
        p.setVolumen(rs.getDouble("volumen"));
        p.setDescripcion(rs.getString("descripcion"));
        return p;
    }
}