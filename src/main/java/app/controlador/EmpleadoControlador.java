package app.controlador;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import app.dao.ConexionBD;
import app.dao.EmpleadoDAO;
import app.modelo.Empleado;

public class EmpleadoControlador {
    
    private final EmpleadoDAO empleadoDao;

    public EmpleadoControlador() {
        this.empleadoDao = new EmpleadoDAO();
    }

    // -------------------------------------------------------
    // 🔹 REGISTRAR NUEVO EMPLEADO (Usuario + Empleado)
    // -------------------------------------------------------
    public boolean registrarEmpleado(Empleado nuevoEmpleado) {
        Connection conexion = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false); // Iniciar transacción

            empleadoDao.insertar(nuevoEmpleado, conexion);

            conexion.commit(); // Confirmar transacción
            System.out.println(" Empleado con ID " + nuevoEmpleado.getIdUsuario() + " registrado correctamente.");
            return true;
        } catch (SQLException e) {
            System.err.println(" Error SQL al registrar empleado: " + e.getMessage());
            if (conexion != null) {
                try { conexion.rollback(); } 
                catch (SQLException ex) { 
                    System.err.println(" Error al hacer rollback: " + ex.getMessage()); 
                }
            }
            return false;
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println(" Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    // -------------------------------------------------------
    // 🔹 ACTUALIZAR EMPLEADO (Usuario + Empleado)
    // -------------------------------------------------------
    public boolean actualizarEmpleado(Empleado empleadoActualizado) {
        Connection conexion = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false); 

            empleadoDao.actualizar(empleadoActualizado, conexion);

            conexion.commit();
            System.out.println(" Empleado con ID " + empleadoActualizado.getIdUsuario() + " actualizado correctamente.");
            return true;
        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar empleado: " + e.getMessage());
            if (conexion != null) {
                try { conexion.rollback(); } 
                catch (SQLException ex) { 
                    System.err.println(" Error al hacer rollback: " + ex.getMessage()); 
                }
            }
            return false;
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println(" Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    // -------------------------------------------------------
    // 🔹 ELIMINAR EMPLEADO (Empleado + Usuario)
    // -------------------------------------------------------
    public boolean eliminarEmpleado(int idUsuario) {
        Connection conexion = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false); 

            empleadoDao.eliminar(idUsuario, conexion);

            conexion.commit();
            System.out.println(" Empleado con ID " + idUsuario + " eliminado correctamente.");
            return true;
        } catch (SQLException e) {
            System.err.println(" Error SQL al eliminar empleado: " + e.getMessage());
            if (conexion != null) {
                try { conexion.rollback(); } 
                catch (SQLException ex) { 
                    System.err.println(" Error al hacer rollback: " + ex.getMessage()); 
                }
            }
            return false;
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println(" Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
    }

    // -------------------------------------------------------
    // 🔹 OBTENER EMPLEADO POR ID
    // -------------------------------------------------------
    public Empleado obtenerEmpleadoPorId(int idUsuario) {
        Connection conexion = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            return empleadoDao.obtenerEmpleadoPorId(idUsuario, conexion);
        } catch (SQLException e) {
            System.err.println(" Error SQL al obtener empleado por ID: " + e.getMessage());
            return null;
        } finally {
            if (conexion != null) {
                try { conexion.close(); } 
                catch (SQLException e) { 
                    System.err.println(" Error al cerrar conexión: " + e.getMessage()); 
                }
            }
        }
    }

    // -------------------------------------------------------
    // 🔹 LISTAR TODOS LOS EMPLEADOS
    // -------------------------------------------------------
    public List<Empleado> listarEmpleados() {
        return empleadoDao.listarEmpleados();
    }
}
