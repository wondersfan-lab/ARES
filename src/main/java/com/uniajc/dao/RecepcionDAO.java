package com.uniajc.dao;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Recepcion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RecepcionDAO implements BaseDAO<Recepcion> {

    private static final String SCHEMA = "ares.";

    @Override
    public boolean insertar(Recepcion recepcion) {
        String sql = "INSERT INTO " + SCHEMA + "recepcion (envio_id, persona_recibe, observaciones) VALUES (?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recepcion.getEnvioId());
            pstmt.setString(2, recepcion.getPersonaRecibe());
            pstmt.setString(3, recepcion.getObservaciones());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Recepcion recepcion) {
        String sql = "UPDATE " + SCHEMA + "recepcion SET envio_id = ?, persona_recibe = ?, observaciones = ? WHERE id_recepcion = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, recepcion.getEnvioId());
            pstmt.setString(2, recepcion.getPersonaRecibe());
            pstmt.setString(3, recepcion.getObservaciones());
            pstmt.setInt(4, recepcion.getId());

            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + SCHEMA + "recepcion WHERE id_recepcion = ?";
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
    public Recepcion buscarPorId(int id) {
        String sql = "SELECT * FROM " + SCHEMA + "recepcion WHERE id_recepcion = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return mapearRecepcion(rs);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Recepcion> listarTodos() {
        String sql = "SELECT * FROM " + SCHEMA + "recepcion ORDER BY id_recepcion DESC";
        List<Recepcion> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearRecepcion(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    private Recepcion mapearRecepcion(ResultSet rs) throws SQLException {
        Recepcion r = new Recepcion();
        r.setId(rs.getInt("id_recepcion"));
        r.setEnvioId(rs.getInt("envio_id"));
        r.setPersonaRecibe(rs.getString("persona_recibe"));
        r.setObservaciones(rs.getString("observaciones"));
        return r;
    }
}