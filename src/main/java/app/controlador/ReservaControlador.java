package app.controlador;

import app.dao.ConexionBD;
import app.dao.ReservaDAO;
import app.dao.VehiculoDAO;
import app.modelo.Reserva;
import java.sql.Connection;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JTable;

public class ReservaControlador {

    private final ReservaDAO reservaDao;
    private final VehiculoDAO vehiculoDao; // 1. INSTANCIA DE VEHICULODAO AGREGADA

    public ReservaControlador() {
        this.reservaDao = new ReservaDAO();
        this.vehiculoDao = new VehiculoDAO(); // 2. INICIALIZACIÓN DE VEHICULODAO
    }

    /**
     * Calcula el costo total de la reserva basándose en los días y el precio
     * diario del vehículo (obtenido de la BD).
     *
     * @param placa La placa del vehículo.
     * @param fechaInicio Fecha de inicio de la reserva (LocalDate extraído de
     * LocalDateTime).
     * @param fechaEntrega Fecha de entrega de la reserva (LocalDate).
     * @return El costo total calculado, o 0.0 si el vehículo no existe o hay un
     * error.
     */
    private double calcularCostoTotal(String placa, LocalDate fechaInicio, LocalDate fechaEntrega) {

        // Se calcula la diferencia de días
        long dias = ChronoUnit.DAYS.between(fechaInicio, fechaEntrega);
        // Si las fechas son el mismo día, ChronoUnit.DAYS.between() devuelve 0. 
        // Se añade +1 para que el cálculo sea inclusivo (el día de inicio cuenta).
        dias = dias + 1;

        if (dias <= 0) {
            // Si por alguna razón la fecha de inicio es posterior a la entrega
            return 0.0;
        }

        double precioDia;
        try {
            // 3. LLAMADA REAL A LA BASE DE DATOS
            precioDia = vehiculoDao.obtenerPrecioDia(placa);

            // Si el DAO devuelve -1.0, significa que el vehículo no se encontró.
            if (precioDia < 0) {
                System.err.println("Error de lógica: Vehículo con placa " + placa + " no encontrado o precio inválido.");
                return 0.0;
            }

        } catch (SQLException e) {
            System.err.println("ERROR SQL al obtener precio diario: " + e.getMessage());
            return 0.0; // Retorna 0.0 en caso de fallo de BD
        }

        return dias * precioDia;
    }

    /**
     * Registra una nueva reserva (Calcula Costo, Inserta y recupera el ID
     * autogenerado).
     * @param nuevaReserva
     * @return 
     */
    public boolean registrarReserva(Reserva nuevaReserva) {
        // Validar método de pago
        List<String> metodosValidos = List.of("Tarjeta", "Efectivo", "Transferencia");
        if (!metodosValidos.contains(nuevaReserva.getMetodoPago())) {
            System.err.println("Error: Método de pago inválido: " + nuevaReserva.getMetodoPago());
            return false; // No se permite registrar si el método es incorrecto
        }

        Connection conexion = null;
        try {
            // 1. Lógica de Negocio: Calcular Costo REAL
            double costoTotal = calcularCostoTotal(
                    nuevaReserva.getPlaca(),
                    nuevaReserva.getFechaInicio(),
                    nuevaReserva.getFechaEntrega()
            );

            if (costoTotal <= 0) {
                System.err.println("Error: El costo total calculado es cero, la reserva es inválida o el vehículo no existe.");
                return false;
            }
            nuevaReserva.setCostoTotal(costoTotal);

            // 2. Obtener la conexión e INICIAR la transacción
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);

            // 3. Ejecutar la operación del DAO (el DAO recuperará el ID)
            int idGenerado = reservaDao.insertar(nuevaReserva, conexion);

            if (idGenerado > 0) {
                conexion.commit();
                System.out.println("Reserva con ID: " + idGenerado + " registrada. Costo total: " + costoTotal);
                return true;
            } else {
                conexion.rollback();
                System.err.println("Error: No se pudo registrar la reserva. Rollback ejecutado.");
                return false;
            }
        } catch (SQLException e) {
            System.err.println("ERROR SQL en el registro de reserva: " + e.getMessage());
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error fatal al intentar hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            // 4. Cerrar la conexión y restaurar el AutoCommit
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }
    
    public List<Reserva> obtenerReservasPorCliente(int idCliente) {
        List<Reserva> lista = new ArrayList<>();
        String sql = "SELECT * FROM Reserva WHERE idCliente = ? ORDER BY fechaInicio DESC";

        try (Connection cn = ConexionBD.obtenerConexion();
             PreparedStatement ps = cn.prepareStatement(sql)) {

            ps.setInt(1, idCliente);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Reserva r = new Reserva(
                    rs.getInt("idReserva"),
                    rs.getInt("idCliente"),
                    rs.getString("placa"),
                    rs.getDate("fechaInicio").toLocalDate(),
                    rs.getDate("fecha_entrega").toLocalDate(),
                    rs.getString("metodoPago"),
                    rs.getDouble("costoTotal"),
                    rs.getString("estado")
                );
                lista.add(r);
            }

        } catch (SQLException e) {
            System.err.println("ERROR al obtener reservas del cliente: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Actualiza una reserva existente (Recalcula Costo y Actualiza).
     */
    public boolean actualizarReserva(Reserva reservaActualizada) {
        Connection conexion = null;
        try {
            // Lógica de Negocio: Recalcular Costo REAL
            double nuevoCosto = calcularCostoTotal(
                    reservaActualizada.getPlaca(),
                    reservaActualizada.getFechaInicio(),
                    reservaActualizada.getFechaEntrega()
            );

            if (nuevoCosto <= 0) {
                System.err.println("Error: Recálculo de costo fallido al actualizar.");
                return false;
            }
            reservaActualizada.setCostoTotal(nuevoCosto);

            // 1. Obtener la conexión e INICIAR la transacción
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);

            // 2. Ejecutar la operación del DAO
            boolean exito = reservaDao.actualizar(reservaActualizada, conexion);

            if (exito) {
                conexion.commit();
                System.out.println("Reserva con ID " + reservaActualizada.getIdReserva() + " actualizada correctamente. Nuevo costo: " + nuevoCosto);
                return true;
            } else {
                conexion.rollback();
                System.err.println("Error: No se encontró la reserva o no hubo cambios. Rollback ejecutado.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("ERROR SQL al actualizar la reserva: " + e.getMessage());
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error fatal al intentar hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            // 3. Cerrar la conexión
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Elimina una reserva existente.
     * @param idReserva
     * @return 
     */
    public boolean eliminarReserva(int idReserva) {
        Connection conexion = null;
        try {
            // 1. Obtener la conexión e INICIAR la transacción
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);

            // 2. Ejecutar la operación del DAO
            boolean exito = reservaDao.eliminar(idReserva, conexion);

            if (exito) {
                conexion.commit();
                System.out.println("Reserva con ID " + idReserva + " eliminada correctamente.");
                return true;
            } else {
                conexion.rollback();
                System.err.println("Error: No se encontró la reserva. Rollback ejecutado.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("ERROR SQL al eliminar la reserva: " + e.getMessage());
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error fatal al intentar hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            // 3. Cerrar la conexión
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    public Reserva obtenerReservaPorId(int idReserva) {
        Reserva reserva = null;
        Connection conexion = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            reserva = reservaDao.obtenerReservaPorId(idReserva, conexion);
        } catch (SQLException e) {
            System.err.println("ERROR SQL al obtener reserva por ID: " + e.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión de consulta: " + e.getMessage());
                }
            }
        }
        return reserva;
    }

    public List<Reserva> listarReservas(JTable tablaReservas) {
        try {
            return reservaDao.listarReservas();
        } catch (SQLException e) {
            System.err.println("ERROR SQL al listar reservas: " + e.getMessage());
            return List.of();
        }
    }

    public List<Reserva> listarReservasPorUsuario(int idCliente) throws SQLException {
        List<Reserva> reservas = new ArrayList<>();
        String sql = "SELECT idReserva, idCliente, placa, fechaInicio, fecha_entrega, metodoPago, costoTotal FROM Reserva WHERE idCliente = ?";

        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    reservas.add(reservaDao.mapearReserva(rs));
                }
            }
        }
        return reservas;
    }

    public boolean cancelarReservaPorPlaca(String placa) {
        // Primero obtenemos la reserva
        Reserva reserva = null;
        try {
            // Suponiendo que solo hay UNA reserva activa por placa
            List<Reserva> reservas = reservaDao.listarReservas();
            for (Reserva r : reservas) {
                if (r.getPlaca().equals(placa) && !r.getEstado().equalsIgnoreCase("Cancelada")) {
                    reserva = r;
                    break;
                }
            }
            if (reserva == null) {
                System.err.println("No se encontró ninguna reserva activa para la placa: " + placa);
                return false;
            }

            // Cambiamos estado a "Cancelada"
            reserva.setEstado("Cancelada");
            return actualizarReserva(reserva); // Reusa tu método existente
        } catch (SQLException e) {
            System.err.println("ERROR SQL al cancelar reserva: " + e.getMessage());
            return false;
        }
    }

    
}
