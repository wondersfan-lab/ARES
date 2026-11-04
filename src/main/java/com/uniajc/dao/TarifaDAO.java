package com.uniajc.dao;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Tarifa;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TarifaDAO implements BaseDAO<Tarifa> {

    private static final String SCHEMA = "ares.";

    @Override
    public boolean insertar(Tarifa tarifa) {
        String sql = "INSERT INTO " + SCHEMA + "tarifa (transportadora_id, costo_base, valor_kilo_adicional) VALUES (?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tarifa.getTransportadoraId());
            pstmt.setDouble(2, tarifa.getCostoBase());
            pstmt.setDouble(3, tarifa.getValorKiloAdicional());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Tarifa tarifa) {
        String sql = "UPDATE " + SCHEMA + "tarifa SET transportadora_id = ?, costo_base = ?, valor_kilo_adicional = ? WHERE id_tarifa = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, tarifa.getTransportadoraId());
            pstmt.setDouble(2, tarifa.getCostoBase());
            pstmt.setDouble(3, tarifa.getValorKiloAdicional());
            pstmt.setInt(4, tarifa.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + SCHEMA + "tarifa WHERE id_tarifa = ?";
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
    public Tarifa buscarPorId(int id) {
        String sql = "SELECT * FROM " + SCHEMA + "tarifa WHERE id_tarifa = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearTarifa(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Tarifa obtenerPorTransportadoraId(int transportadoraId) {
        String sql = "SELECT * FROM " + SCHEMA + "tarifa WHERE transportadora_id = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, transportadoraId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearTarifa(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Tarifa> listarTodos() {
        String sql = "SELECT * FROM " + SCHEMA + "tarifa ORDER BY id_tarifa ASC";
        List<Tarifa> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearTarifa(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Tarifa mapearTarifa(ResultSet rs) throws SQLException {
        Tarifa t = new Tarifa();
        t.setId(rs.getInt("id_tarifa"));
        t.setTransportadoraId(rs.getInt("transportadora_id"));
        t.setCostoBase(rs.getDouble("costo_base"));
        t.setValorKiloAdicional(rs.getDouble("valor_kilo_adicional"));
        return t;
    }
}