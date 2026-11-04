package com.uniajc.dao;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Transportadora;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransportadoraDAO implements BaseDAO<Transportadora> {

    private static final String SCHEMA = "ares.";

    @Override
    public boolean insertar(Transportadora transportadora) {
        String sql = "INSERT INTO " + SCHEMA + "transportadora (nit, razon_social, telefono, estado_activo) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, transportadora.getNit());
            pstmt.setString(2, transportadora.getRazonSocial());
            pstmt.setString(3, transportadora.getTelefono());
            pstmt.setBoolean(4, transportadora.isEstadoActivo());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Transportadora transportadora) {
        String sql = "UPDATE " + SCHEMA + "transportadora SET nit = ?, razon_social = ?, telefono = ?, estado_activo = ? WHERE id_transportadora = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, transportadora.getNit());
            pstmt.setString(2, transportadora.getRazonSocial());
            pstmt.setString(3, transportadora.getTelefono());
            pstmt.setBoolean(4, transportadora.isEstadoActivo());
            pstmt.setInt(5, transportadora.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + SCHEMA + "transportadora WHERE id_transportadora = ?";
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
    public Transportadora buscarPorId(int id) {
        String sql = "SELECT * FROM " + SCHEMA + "transportadora WHERE id_transportadora = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearTransportadora(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Transportadora> listarTodos() {
        String sql = "SELECT * FROM " + SCHEMA + "transportadora ORDER BY id_transportadora ASC";
        List<Transportadora> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearTransportadora(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Transportadora mapearTransportadora(ResultSet rs) throws SQLException {
        Transportadora t = new Transportadora();
        t.setId(rs.getInt("id_transportadora"));
        t.setNit(rs.getString("nit"));
        t.setRazonSocial(rs.getString("razon_social"));
        t.setTelefono(rs.getString("telefono"));
        t.setEstadoActivo(rs.getBoolean("estado_activo"));
        return t;
    }
}