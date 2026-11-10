package com.uniajc.dao;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Operacion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


public class OperacionDAO implements BaseDAO<Operacion> {

    private static final String SCHEMA = "ares.";

    @Override
    public boolean insertar(Operacion o) {
        String sql = "INSERT INTO " + SCHEMA + "operacion (id_transportadora, id_usuario, tipo, guia, fecha, hora, "
                   + "estado, remitente, destinatario, peso, costo, observaciones) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            asignarParametros(ps, o);

            boolean insertado = ps.executeUpdate() > 0;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    o.setId(keys.getInt("id_operacion"));
                }
            }
            return insertado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar operación: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Operacion o) {
        String sql = "UPDATE " + SCHEMA + "operacion SET id_transportadora = ?, id_usuario = ?, tipo = ?, guia = ?, "
                   + "fecha = ?, hora = ?, estado = ?, remitente = ?, destinatario = ?, peso = ?, costo = ?, "
                   + "observaciones = ? WHERE id_operacion = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            asignarParametros(ps, o);
            ps.setInt(13, o.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar operación: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(int id) {
        
        String sql = "DELETE FROM " + SCHEMA + "operacion WHERE id_operacion = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar operación: " + e.getMessage(), e);
        }
    }

    @Override
    public Operacion buscarPorId(int id) {
        String sql = "SELECT * FROM " + SCHEMA + "operacion WHERE id_operacion = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearOperacion(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar operación: " + e.getMessage(), e);
        }
    }

    
    public List<Operacion> buscarPorGuia(String guia) {
        String sql = "SELECT * FROM " + SCHEMA + "operacion WHERE guia = ? ORDER BY id_operacion ASC";
        return consultarLista(sql, guia);
    }

    
    public List<Operacion> listarPorFecha(LocalDate fecha) {
        String sql = "SELECT * FROM " + SCHEMA + "operacion WHERE fecha = ? ORDER BY hora ASC, id_operacion ASC";
        return consultarLista(sql, fecha);
    }

    @Override
    public List<Operacion> listarTodos() {
        String sql = "SELECT * FROM " + SCHEMA + "operacion ORDER BY fecha DESC, hora DESC, id_operacion DESC";
        return consultarLista(sql, null);
    }

    
    private List<Operacion> consultarLista(String sql, Object parametro) {
        List<Operacion> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (parametro != null) {
                ps.setObject(1, parametro);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearOperacion(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar operaciones: " + e.getMessage(), e);
        }
    }

   
    private void asignarParametros(PreparedStatement ps, Operacion o) throws SQLException {
        ps.setInt(1, o.getIdTransportadora());
        ps.setInt(2, o.getIdUsuario());
        ps.setString(3, o.getTipo());
        ps.setString(4, o.getGuia());
        ps.setObject(5, o.getFecha());
        ps.setObject(6, o.getHora());
        ps.setString(7, o.getEstado());
        ps.setString(8, o.getRemitente());
        ps.setString(9, o.getDestinatario());
        ps.setBigDecimal(10, o.getPeso());   // puede ser null (recepciones)
        ps.setBigDecimal(11, o.getCosto());  // puede ser null (recepciones)
        ps.setString(12, o.getObservaciones());
    }

    private Operacion mapearOperacion(ResultSet rs) throws SQLException {
        Operacion o = new Operacion();
        o.setId(rs.getInt("id_operacion"));
        o.setIdTransportadora(rs.getInt("id_transportadora"));
        o.setIdUsuario(rs.getInt("id_usuario"));
        o.setTipo(rs.getString("tipo"));
        o.setGuia(rs.getString("guia"));
        o.setFecha(rs.getObject("fecha", LocalDate.class));
        o.setHora(rs.getObject("hora", LocalTime.class));
        o.setEstado(rs.getString("estado"));
        o.setRemitente(rs.getString("remitente"));
        o.setDestinatario(rs.getString("destinatario"));
        o.setPeso(rs.getBigDecimal("peso"));
        o.setCosto(rs.getBigDecimal("costo"));
        o.setObservaciones(rs.getString("observaciones"));
        return o;
    }
}
