package com.uniajc.dao;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Transportadora;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla ares.transportadora (los operadores).
 * Las decisiones generales están explicadas en UsuarioDAO.
 */
public class TransportadoraDAO implements BaseDAO<Transportadora> {

    private static final String SCHEMA = "ares.";

    @Override
    public boolean insertar(Transportadora t) {
        String sql = "INSERT INTO " + SCHEMA + "transportadora (nombre, nit, telefono_contacto, direccion, tipo_servicio, activo) "
                   + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, t.getNombre());
            ps.setString(2, t.getNit());
            ps.setString(3, t.getTelefonoContacto());
            ps.setString(4, t.getDireccion());
            ps.setString(5, t.getTipoServicio());
            ps.setBoolean(6, t.isActivo());

            boolean insertado = ps.executeUpdate() > 0;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    t.setId(keys.getInt("id_transportadora"));
                }
            }
            return insertado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar transportadora: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Transportadora t) {
        String sql = "UPDATE " + SCHEMA + "transportadora SET nombre = ?, nit = ?, telefono_contacto = ?, "
                   + "direccion = ?, tipo_servicio = ?, activo = ? WHERE id_transportadora = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, t.getNombre());
            ps.setString(2, t.getNit());
            ps.setString(3, t.getTelefonoContacto());
            ps.setString(4, t.getDireccion());
            ps.setString(5, t.getTipoServicio());
            ps.setBoolean(6, t.isActivo());
            ps.setInt(7, t.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar transportadora: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + SCHEMA + "transportadora WHERE id_transportadora = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Falla si ya tiene operaciones registradas: en ese caso conviene desactivarla (activo = false).
            throw new RuntimeException("Error al eliminar transportadora: " + e.getMessage(), e);
        }
    }

    @Override
    public Transportadora buscarPorId(int id) {
        String sql = "SELECT * FROM " + SCHEMA + "transportadora WHERE id_transportadora = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearTransportadora(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar transportadora: " + e.getMessage(), e);
        }
    }

    /** Busca un operador por su nombre exacto (por ejemplo "Coordinadora"). */
    public Transportadora buscarPorNombre(String nombre) {
        String sql = "SELECT * FROM " + SCHEMA + "transportadora WHERE nombre = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nombre);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearTransportadora(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar transportadora por nombre: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Transportadora> listarTodos() {
        String sql = "SELECT * FROM " + SCHEMA + "transportadora ORDER BY id_transportadora ASC";
        List<Transportadora> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearTransportadora(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar transportadoras: " + e.getMessage(), e);
        }
    }

    private Transportadora mapearTransportadora(ResultSet rs) throws SQLException {
        Transportadora t = new Transportadora();
        t.setId(rs.getInt("id_transportadora"));
        t.setNombre(rs.getString("nombre"));
        t.setNit(rs.getString("nit"));
        t.setTelefonoContacto(rs.getString("telefono_contacto"));
        t.setDireccion(rs.getString("direccion"));
        t.setTipoServicio(rs.getString("tipo_servicio"));
        t.setActivo(rs.getBoolean("activo"));
        return t;
    }
}
