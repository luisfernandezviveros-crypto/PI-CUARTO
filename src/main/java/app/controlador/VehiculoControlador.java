package app.controlador;

import app.dao.ConexionBD;
import app.dao.VehiculoDAO;
import app.modelo.Vehiculo;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class VehiculoControlador {

    private final VehiculoDAO vehiculoDao;

    public VehiculoControlador() {
        // Inicializa el DAO
        this.vehiculoDao = new VehiculoDAO();
    }

    /**
     * Registra un nuevo vehículo.
     *
     * @param nuevoVehiculo El objeto Vehiculo a registrar.
     * @return true si el registro fue exitoso.
     */
    public boolean registrarVehiculo(Vehiculo nuevoVehiculo) {

        Connection conexion = null;
        try {
            // 1. Obtener la conexión e INICIAR la transacción
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);

            // 2. Ejecutar la operación del DAO
            boolean exito = vehiculoDao.insertar(nuevoVehiculo, conexion);

            if (exito) {
                conexion.commit();
                System.out.println("Vehiculo con placa: " + nuevoVehiculo.getPlaca() + " registrado.");
                return true;
            } else {
                conexion.rollback();
                System.err.println("Error: No se pudo registrar el vehículo. Rollback ejecutado.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("ERROR SQL en el registro de vehículo: " + e.getMessage());
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error fatal al intentar hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            // 3. Cerrar la conexión y restaurar el AutoCommit
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
     * Actualiza un vehículo existente.
     *
     * @param vehiculoActualizado El objeto Vehiculo con los datos a actualizar.
     * @return true si la actualización fue exitosa.
     */
    public boolean actualizarVehiculo(Vehiculo vehiculoActualizado) {
        Connection conexion = null;
        try {
            // 1. Obtener la conexión e INICIAR la transacción
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);

            // 2. Ejecutar la operación del DAO
            boolean exito = vehiculoDao.actualizar(vehiculoActualizado, conexion);

            if (exito) {
                conexion.commit();
                System.out.println("Vehiculo con placa " + vehiculoActualizado.getPlaca() + " actualizado correctamente.");
                return true;
            } else {
                conexion.rollback();
                System.err.println("Error: No se encontró el vehículo o no hubo cambios. Rollback ejecutado.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("ERROR SQL al actualizar el vehículo: " + e.getMessage());
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
     * Elimina un vehículo existente.
     *
     * @param placa La placa del vehículo a eliminar.
     * @return true si la eliminación fue exitosa.
     */
    public boolean eliminarVehiculo(String placa) {
        Connection conexion = null;
        try {
            // 1. Obtener la conexión e INICIAR la transacción
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);

            // 2. Ejecutar la operación del DAO
            boolean exito = vehiculoDao.eliminar(placa, conexion);

            if (exito) {
                conexion.commit();
                System.out.println("Vehiculo con placa " + placa + " eliminado correctamente.");
                return true;
            } else {
                conexion.rollback();
                System.err.println("Error: No se encontró el vehículo. Rollback ejecutado.");
                return false;
            }

        } catch (SQLException e) {
            System.err.println("ERROR SQL al eliminar el vehículo: " + e.getMessage());
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

    public Vehiculo obtenerVehiculoPorPlaca(String placa) {
        Vehiculo vehiculo = null;
        Connection conexion = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            // Pasa la conexión para una consulta simple
            vehiculo = vehiculoDao.obtenerVehiculoPorPlaca(placa, conexion);
        } catch (SQLException e) {
            System.err.println("ERROR SQL al obtener vehículo por placa: " + e.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión de consulta: " + e.getMessage());
                }
            }
        }
        return vehiculo;
    }

    public List<Vehiculo> listarVehiculos() {
        try {
            // Este método del DAO maneja su propia conexión (es solo lectura)
            return vehiculoDao.listarVehiculos();
        } catch (SQLException e) {
            System.err.println("ERROR SQL al listar vehículos: " + e.getMessage());
            return List.of();
        }
    }

    public String obtenerEstadoVehiculo(String placa) {
        try {
            return vehiculoDao.obtenerEstado(placa); // Llama al DAO
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean actualizarEstadoVehiculo(String placa, String estado) {
        try {
            return vehiculoDao.actualizarEstado(placa, estado); // Llama al DAO
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Vehiculo> filtrarVehiculosDisponibles(String tipo, String marca, Double precioMax) {
        try {
            return vehiculoDao.listarVehiculosDisponibles(tipo, marca, precioMax);
        } catch (SQLException e) {
            System.err.println("ERROR al filtrar vehículos: " + e.getMessage());
            return List.of();
        }
    }

    public List<Vehiculo> listarVehiculosDisponibles() throws SQLException {
        return vehiculoDao.listarVehiculosPorEstado("Disponible");
    }

    public List<Vehiculo> listarVehiculosPorTipo(String tipo) {
        try {
            return vehiculoDao.obtenerVehiculosPorTipo(tipo);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
