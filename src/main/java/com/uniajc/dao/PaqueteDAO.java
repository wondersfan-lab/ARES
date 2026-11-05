package com.uniajc.dao;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Paquete;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla ares.paquete.
 * Las decisiones generales están explicadas en UsuarioDAO.
 */
public class PaqueteDAO implements BaseDAO<Paquete> {

    private static final String SCHEMA = "ares.";

    @Override
    public boolean insertar(Paquete p) {
        String sql = "INSERT INTO " + SCHEMA + "paquete (id_operacion, nombre_producto, descripcion, cantidad) "
                   + "VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, p.getIdOperacion());
            ps.setString(2, p.getNombreProducto());
            ps.setString(3, p.getDescripcion());
            ps.setInt(4, p.getCantidad());

            boolean insertado = ps.executeUpdate() > 0;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    p.setId(keys.getInt("id_paquete"));
                }
            }
            return insertado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar paquete: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Paquete p) {
        String sql = "UPDATE " + SCHEMA + "paquete SET id_operacion = ?, nombre_producto = ?, descripcion = ?, cantidad = ? "
                   + "WHERE id_paquete = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, p.getIdOperacion());
            ps.setString(2, p.getNombreProducto());
            ps.setString(3, p.getDescripcion());
            ps.setInt(4, p.getCantidad());
            ps.setInt(5, p.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar paquete: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + SCHEMA + "paquete WHERE id_paquete = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar paquete: " + e.getMessage(), e);
        }
    }

    @Override
    public Paquete buscarPorId(int id) {
        String sql = "SELECT * FROM " + SCHEMA + "paquete WHERE id_paquete = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPaquete(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar paquete: " + e.getMessage(), e);
        }
    }

    /** Todos los paquetes de una operación. */
    public List<Paquete> listarPorOperacion(int idOperacion) {
        String sql = "SELECT * FROM " + SCHEMA + "paquete WHERE id_operacion = ? ORDER BY id_paquete ASC";
        List<Paquete> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idOperacion);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPaquete(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar paquetes de la operación: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Paquete> listarTodos() {
        String sql = "SELECT * FROM " + SCHEMA + "paquete ORDER BY id_paquete ASC";
        List<Paquete> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearPaquete(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar paquetes: " + e.getMessage(), e);
        }
    }

    private Paquete mapearPaquete(ResultSet rs) throws SQLException {
        Paquete p = new Paquete();
        p.setId(rs.getInt("id_paquete"));
        p.setIdOperacion(rs.getInt("id_operacion"));
        p.setNombreProducto(rs.getString("nombre_producto"));
        p.setDescripcion(rs.getString("descripcion"));
        p.setCantidad(rs.getInt("cantidad"));
        return p;
    }
}
