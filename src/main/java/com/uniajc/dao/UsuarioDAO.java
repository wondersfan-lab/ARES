package com.uniajc.dao;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla ares.usuario.
 *
 * Decisiones generales (iguales en todos los DAO):
 *  - Se usa PreparedStatement con "?" y nunca se concatenan valores en el SQL,
 *    para evitar inyección SQL.
 *  - try-with-resources cierra Connection, PreparedStatement y ResultSet solos.
 *  - Los errores de SQL se convierten en RuntimeException con un mensaje claro.
 *  - Al insertar se pide la llave generada (RETURN_GENERATED_KEYS) y se copia
 *    al objeto, así después de insertar el objeto ya tiene su id.
 */
public class UsuarioDAO implements BaseDAO<Usuario> {

    private static final String SCHEMA = "ares.";

    @Override
    public boolean insertar(Usuario u) {
        String sql = "INSERT INTO " + SCHEMA + "usuario (nombre, telefono, email, usuario, contrasena, rol, activo) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getTelefono());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getLogin());
            ps.setString(5, u.getContrasena());
            ps.setString(6, u.getRol());
            ps.setBoolean(7, u.isActivo());

            boolean insertado = ps.executeUpdate() > 0;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    u.setId(keys.getInt("id_usuario"));
                }
            }
            return insertado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Usuario u) {
        String sql = "UPDATE " + SCHEMA + "usuario SET nombre = ?, telefono = ?, email = ?, usuario = ?, "
                   + "contrasena = ?, rol = ?, activo = ? WHERE id_usuario = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getNombre());
            ps.setString(2, u.getTelefono());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getLogin());
            ps.setString(5, u.getContrasena());
            ps.setString(6, u.getRol());
            ps.setBoolean(7, u.isActivo());
            ps.setInt(8, u.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + SCHEMA + "usuario WHERE id_usuario = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            // Falla (por llave foránea) si el usuario ya registró operaciones: en ese caso conviene desactivarlo.
            throw new RuntimeException("Error al eliminar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public Usuario buscarPorId(int id) {
        String sql = "SELECT * FROM " + SCHEMA + "usuario WHERE id_usuario = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar usuario: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Usuario> listarTodos() {
        String sql = "SELECT * FROM " + SCHEMA + "usuario ORDER BY id_usuario ASC";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearUsuario(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar usuarios: " + e.getMessage(), e);
        }
    }

    /**
     * Inicio de sesión: devuelve el usuario si el login y la contraseña
     * coinciden y el usuario está activo; si no, devuelve null.
     */
    public Usuario autenticar(String login, String contrasena) {
        String sql = "SELECT * FROM " + SCHEMA + "usuario WHERE usuario = ? AND contrasena = ? AND activo = TRUE";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, login);
            ps.setString(2, contrasena);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearUsuario(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al autenticar usuario: " + e.getMessage(), e);
        }
    }

    /** Convierte una fila de la tabla en un objeto Usuario. */
    private Usuario mapearUsuario(ResultSet rs) throws SQLException {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id_usuario"));
        u.setNombre(rs.getString("nombre"));
        u.setTelefono(rs.getString("telefono"));
        u.setEmail(rs.getString("email"));
        u.setLogin(rs.getString("usuario"));
        u.setContrasena(rs.getString("contrasena"));
        u.setRol(rs.getString("rol"));
        u.setActivo(rs.getBoolean("activo"));
        return u;
    }
}
