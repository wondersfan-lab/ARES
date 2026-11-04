package com.uniajc.dao;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Envio;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EnvioDAO implements BaseDAO<Envio> {

    private static final String SCHEMA = "ares.";

    @Override
    public boolean insertar(Envio envio) {
        String sql = "INSERT INTO " + SCHEMA + "envio (codigo_seguimiento, usuario_id, transportadora_id, costo_total, estado, destinatario) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, envio.getCodigoSeguimiento());
            pstmt.setInt(2, envio.getUsuarioId());
            pstmt.setInt(3, envio.getTransportadoraId());
            pstmt.setDouble(4, envio.getCostoTotal());
            pstmt.setString(5, envio.getEstado());
            pstmt.setString(6, envio.getDestinatario());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Envio envio) {
        String sql = "UPDATE " + SCHEMA + "envio SET codigo_seguimiento = ?, usuario_id = ?, transportadora_id = ?, costo_total = ?, estado = ?, destinatario = ? WHERE id_envio = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, envio.getCodigoSeguimiento());
            pstmt.setInt(2, envio.getUsuarioId());
            pstmt.setInt(3, envio.getTransportadoraId());
            pstmt.setDouble(4, envio.getCostoTotal());
            pstmt.setString(5, envio.getEstado());
            pstmt.setString(6, envio.getDestinatario());
            pstmt.setInt(7, envio.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + SCHEMA + "envio WHERE id_envio = ?";
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
    public Envio buscarPorId(int id) {
        String sql = "SELECT * FROM " + SCHEMA + "envio WHERE id_envio = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEnvio(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Envio obtenerPorCodigoSeguimiento(String codigo) {
        String sql = "SELECT * FROM " + SCHEMA + "envio WHERE codigo_seguimiento = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, codigo);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEnvio(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Envio> listarTodos() {
        String sql = "SELECT * FROM " + SCHEMA + "envio ORDER BY id_envio DESC";
        List<Envio> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearEnvio(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Envio mapearEnvio(ResultSet rs) throws SQLException {
        Envio e = new Envio();
        e.setId(rs.getInt("id_envio"));
        e.setCodigoSeguimiento(rs.getString("codigo_seguimiento"));
        e.setUsuarioId(rs.getInt("usuario_id"));
        e.setTransportadoraId(rs.getInt("transportadora_id"));
        e.setCostoTotal(rs.getDouble("costo_total"));
        e.setEstado(rs.getString("estado"));
        e.setDestinatario(rs.getString("destinatario"));
        return e;
    }
}