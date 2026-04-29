package app.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class EstadisticaDAO {

    /**
     * Estadística 1: Reservas por tipo de vehículo
     */
    public Map<String, Integer> obtenerReservasPorTipo() {
        Map<String, Integer> resultados = new HashMap<>();

        String sql = """
            SELECT v.tipo, COUNT(r.idReserva) AS cantidad
            FROM Reserva r
            INNER JOIN Vehiculo v ON r.placa = v.placa
            GROUP BY v.tipo;
        """;

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultados.put(rs.getString("tipo"), rs.getInt("cantidad"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener reservas por tipo: " + e.getMessage());
        }
        return resultados;
    }

    /**
     * Estadística 2: Ingresos por tipo de vehículo
     */
    public Map<String, Double> obtenerIngresosPorTipo() {
        Map<String, Double> resultados = new HashMap<>();

        String sql = """
            SELECT v.tipo, SUM(r.costoTotal) AS ingresos
            FROM Reserva r
            INNER JOIN Vehiculo v ON r.placa = v.placa
            WHERE r.estado = 'Finalizada'
            GROUP BY v.tipo;
        """;

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultados.put(rs.getString("tipo"), rs.getDouble("ingresos"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener ingresos por tipo: " + e.getMessage());
        }
        return resultados;
    }

    /**
     * Estadística 3: Clientes frecuentes (más de 1 reserva)
     */
    public Map<String, Integer> obtenerClientesFrecuentes() {
        Map<String, Integer> resultados = new HashMap<>();

        String sql = """
            SELECT CONCAT(u.nombre, ' ', u.apellido) AS cliente, COUNT(r.idReserva) AS reservas
            FROM Reserva r
            INNER JOIN Cliente c ON r.idCliente = c.idUsuario
            INNER JOIN Usuario u ON c.idUsuario = u.idUsuario
            GROUP BY u.idUsuario
            HAVING COUNT(r.idReserva) > 1
            ORDER BY reservas DESC;
        """;

        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                resultados.put(rs.getString("cliente"), rs.getInt("reservas"));
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener clientes frecuentes: " + e.getMessage());
        }
        return resultados;
    }
}