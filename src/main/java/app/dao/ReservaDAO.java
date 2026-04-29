package app.dao;

import app.modelo.Reserva;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservaDAO {

    private static final String SQL_INSERT
            = "INSERT INTO Reserva (idCliente, placa, fechaInicio, fecha_entrega, metodoPago, costoTotal, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";

    private static final String SQL_SELECT_BY_ID
            = "SELECT idReserva, idCliente, placa, fechaInicio, fecha_entrega, metodoPago, costoTotal, estado FROM Reserva WHERE idReserva = ?";

    private static final String SQL_SELECT_ALL
            = "SELECT idReserva, idCliente, placa, fechaInicio, fecha_entrega, metodoPago, costoTotal, estado FROM Reserva";

    private static final String SQL_UPDATE
            = "UPDATE Reserva SET idCliente=?, placa=?, fechaInicio=?, fecha_entrega=?, metodoPago=?, costoTotal=?, estado=? WHERE idReserva=?";

    private static final String SQL_DELETE
            = "DELETE FROM Reserva WHERE idReserva = ?";

    public Reserva mapearReserva(ResultSet rs) throws SQLException {
        int idReserva = rs.getInt("idReserva");
        int idCliente = rs.getInt("idCliente");
        String placa = rs.getString("placa");
        LocalDate fechaInicio = rs.getDate("fechaInicio").toLocalDate();
        LocalDate fechaEntrega = rs.getDate("fecha_entrega").toLocalDate();
        String metodoPago = rs.getString("metodoPago");
        double costoTotal = rs.getDouble("costoTotal");
        String estado = rs.getString("estado");

        return new Reserva(idReserva, idCliente, placa, fechaInicio, fechaEntrega, metodoPago, costoTotal, estado);
    }

    public int insertar(Reserva r, Connection conexion) throws SQLException {
        int idGenerado = -1;
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, r.getIdCliente());
            stmt.setString(2, r.getPlaca());
            stmt.setDate(3, Date.valueOf(r.getFechaInicio()));
            stmt.setDate(4, Date.valueOf(r.getFechaEntrega()));
            stmt.setString(5, r.getMetodoPago());
            stmt.setDouble(6, r.getCostoTotal());
            stmt.setString(7, r.getEstado()); // nuevo

            int filas = stmt.executeUpdate();
            if (filas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1);
                        r.setIdReserva(idGenerado);
                    }
                }
            }
        }
        return idGenerado;
    }

    public boolean actualizar(Reserva r, Connection conexion) throws SQLException {
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_UPDATE)) {
            stmt.setInt(1, r.getIdCliente());
            stmt.setString(2, r.getPlaca());
            stmt.setDate(3, Date.valueOf(r.getFechaInicio()));
            stmt.setDate(4, Date.valueOf(r.getFechaEntrega()));
            stmt.setString(5, r.getMetodoPago());
            stmt.setDouble(6, r.getCostoTotal());
            stmt.setString(7, r.getEstado()); // <-- agregado
            stmt.setInt(8, r.getIdReserva());

            return stmt.executeUpdate() > 0;
        }
    }

    public boolean eliminar(int idReserva, Connection conexion) throws SQLException {
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_DELETE)) {
            stmt.setInt(1, idReserva);
            return stmt.executeUpdate() > 0;
        }
    }

    public Reserva obtenerReservaPorId(int idReserva, Connection conexion) throws SQLException {
        Reserva reserva = null;
        try (PreparedStatement stmt = conexion.prepareStatement(SQL_SELECT_BY_ID)) {
            stmt.setInt(1, idReserva);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    reserva = mapearReserva(rs);
                }
            }
        }
        return reserva;
    }

    public List<Reserva> listarReservas() throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                reservas.add(mapearReserva(rs));
            }
        }
        return reservas;
    }

    public List<Reserva> listarReservasPorUsuario(int idCliente) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT idReserva, idCliente, placa, fechaInicio, fecha_entrega, metodoPago, costoTotal, estado "
                + "FROM Reserva WHERE idCliente = ?";
        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(mapearReserva(rs));
                }
            }
        }
        return reservas;
    }
}
