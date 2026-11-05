package com.uniajc.dao;

import com.uniajc.config.ConexionPostgresDatabase;
import com.uniajc.modelo.Tarifa;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Acceso a datos de la tabla ares.tarifa.
 * Las decisiones generales están explicadas en UsuarioDAO.
 */
public class TarifaDAO implements BaseDAO<Tarifa> {

    private static final String SCHEMA = "ares.";

    @Override
    public boolean insertar(Tarifa t) {
        String sql = "INSERT INTO " + SCHEMA + "tarifa (id_transportadora, peso_min, peso_max, valor) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, t.getIdTransportadora());
            ps.setBigDecimal(2, t.getPesoMin());
            ps.setBigDecimal(3, t.getPesoMax());
            ps.setBigDecimal(4, t.getValor());

            boolean insertado = ps.executeUpdate() > 0;
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    t.setId(keys.getInt("id_tarifa"));
                }
            }
            return insertado;
        } catch (SQLException e) {
            throw new RuntimeException("Error al insertar tarifa: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizar(Tarifa t) {
        String sql = "UPDATE " + SCHEMA + "tarifa SET id_transportadora = ?, peso_min = ?, peso_max = ?, valor = ? "
                   + "WHERE id_tarifa = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, t.getIdTransportadora());
            ps.setBigDecimal(2, t.getPesoMin());
            ps.setBigDecimal(3, t.getPesoMax());
            ps.setBigDecimal(4, t.getValor());
            ps.setInt(5, t.getId());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al actualizar tarifa: " + e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminar(int id) {
        String sql = "DELETE FROM " + SCHEMA + "tarifa WHERE id_tarifa = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error al eliminar tarifa: " + e.getMessage(), e);
        }
    }

    @Override
    public Tarifa buscarPorId(int id) {
        String sql = "SELECT * FROM " + SCHEMA + "tarifa WHERE id_tarifa = ?";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearTarifa(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tarifa: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Tarifa> listarTodos() {
        String sql = "SELECT * FROM " + SCHEMA + "tarifa ORDER BY id_tarifa ASC";
        List<Tarifa> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(mapearTarifa(rs));
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar tarifas: " + e.getMessage(), e);
        }
    }

    /** Todas las tarifas de un operador, ordenadas por peso. */
    public List<Tarifa> listarPorTransportadora(int idTransportadora) {
        String sql = "SELECT * FROM " + SCHEMA + "tarifa WHERE id_transportadora = ? ORDER BY peso_min ASC";
        List<Tarifa> lista = new ArrayList<>();
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTransportadora);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearTarifa(rs));
                }
            }
            return lista;
        } catch (SQLException e) {
            throw new RuntimeException("Error al listar tarifas de la transportadora: " + e.getMessage(), e);
        }
    }

    /**
     * Busca la tarifa que le corresponde a un peso: peso mínimo excluido y
     * máximo incluido (5 kg cae en el rango 0-5). Devuelve null si ningún
     * rango cubre ese peso. Sirve para calcular el costo de un envío.
     */
    public Tarifa buscarTarifaParaPeso(int idTransportadora, BigDecimal peso) {
        String sql = "SELECT * FROM " + SCHEMA + "tarifa "
                   + "WHERE id_transportadora = ? AND ? > peso_min AND ? <= peso_max "
                   + "ORDER BY peso_min ASC LIMIT 1";
        try (Connection conn = ConexionPostgresDatabase.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idTransportadora);
            ps.setBigDecimal(2, peso);
            ps.setBigDecimal(3, peso);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearTarifa(rs);
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error al buscar tarifa por peso: " + e.getMessage(), e);
        }
    }

    private Tarifa mapearTarifa(ResultSet rs) throws SQLException {
        Tarifa t = new Tarifa();
        t.setId(rs.getInt("id_tarifa"));
        t.setIdTransportadora(rs.getInt("id_transportadora"));
        t.setPesoMin(rs.getBigDecimal("peso_min"));
        t.setPesoMax(rs.getBigDecimal("peso_max"));
        t.setValor(rs.getBigDecimal("valor"));
        return t;
    }
}
