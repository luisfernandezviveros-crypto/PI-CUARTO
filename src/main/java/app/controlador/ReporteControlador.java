package app.controlador;

import app.dao.ConexionBD;
import app.dao.ReporteDAO;
import app.modelo.Reporte;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class ReporteControlador {
    
    private final ReporteDAO reporteDao;

    public ReporteControlador() {
        this.reporteDao = new ReporteDAO();
    }
    
    
    /**
     * Logica simulada para generar el contenido del reporte 
     * En una aplicacion real, esto implicaria consultas SQL complejas.
     */
    private String generarContenidoReporte(String tipo) {
        // Simulacion: en produccion, esto haria SELECTs complejos a la BD
        return switch (tipo) {
            case "Ingresos" -> "Reporte de Ingresos generado para la fecha: " + LocalDate.now();
            case "Reservas" -> "Reporte detallado de Reservas pendientes.";
            case "Disponibilidad" -> "Listado de vehiculos con estado Disponible.";
            case "Clientes" -> "Listado de todos los clientes activos.";
            default -> "Tipo de reporte no valido.";
        };
    }
    
    /**
     * Genera un reporte y lo registra en la BD.
     * @param idEmpleado
     * @param tipo
     * @return 
     */
    public boolean generarYRegistrarReporte(int idEmpleado, String tipo) {
        
        //Generar contenido (logica de negocio)
        String contenido = generarContenidoReporte(tipo);
        if (contenido.contains("no valido")) {
            System.err.println("Error: " + contenido);
            return false;
        }
        
        //Crear objeto Reporte
        Reporte nuevoReporte = new Reporte(idEmpleado, tipo, LocalDate.now());
        
        //Registrar en BD (logica transaccional)
        Connection conexion = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false); 

            int idGenerado = reporteDao.insertar(nuevoReporte, conexion);
            
            if (idGenerado > 0) {
                conexion.commit(); 
                System.out.println("Reporte de " + tipo + " generado y registrado con ID: " + idGenerado);
                // Aqui podrias guardar el contenido en un archivo o enviarlo
                System.out.println("Contenido (simulado): " + contenido);
                return true;
            } else {
                conexion.rollback(); 
                System.err.println("Error: No se pudo registrar el reporte. Rollback ejecutado.");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("ERROR SQL en el registro de reporte: " + e.getMessage());
            if (conexion != null) {
                try {
                    conexion.rollback(); 
                } catch (SQLException ex) {
                    System.err.println("Error fatal al intentar hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexion: " + e.getMessage());
                }
            }
        }
    }
    
    // OPERACIONES CRUD
    
    public boolean eliminarReporte(int idReporte) {
        Connection conexion = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false); 
            
            boolean exito = reporteDao.eliminar(idReporte, conexion);
            
            if (exito) {
                conexion.commit(); 
                System.out.println("Reporte con ID " + idReporte + " eliminado correctamente.");
                return true;
            } else {
                conexion.rollback();
                System.err.println("Error: No se encontro el reporte. Rollback ejecutado.");
                return false;
            }
            
        } catch (SQLException e) {
            System.err.println("ERROR SQL al eliminar el reporte: " + e.getMessage());
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error fatal al intentar hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexion: " + e.getMessage());
                }
            }
        }
    }
    
    public List<Reporte> listarReportes() {
        try {
            return reporteDao.listarReportes();
        } catch (SQLException e) {
            System.err.println("ERROR SQL al listar reportes: " + e.getMessage());
            return List.of(); 
        }
    }
    
    public Reporte obtenerReportePorId(int idReporte) {
        Reporte reporte = null;
        Connection conexion = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            reporte = reporteDao.obtenerReportePorId(idReporte, conexion); 
        } catch (SQLException e) {
            System.err.println("ERROR SQL al obtener reporte por ID: " + e.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexion de consulta: " + e.getMessage());
                }
            }
        }
        return reporte;
    }
}
