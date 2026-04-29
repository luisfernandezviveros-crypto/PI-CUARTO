
package app.controlador;
import app.vista.GestionClientes;
import app.dao.ClienteDAO;
import app.dao.ConexionBD;
import app.modelo.Cliente;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import app.vista.GestionClientes;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ClienteControlador {
    
    Cliente cliente = new Cliente();
    ClienteDAO dao = new ClienteDAO();
    DefaultTableModel modelo = new DefaultTableModel();
    
    private final ClienteDAO clienteDao;

    public ClienteControlador() {
        this.clienteDao = new ClienteDAO();
    }
    
    // OPERACIONES CRUD CON TRANSACCIONES

    /**
     * Registra un nuevo cliente (Usuario y Cliente) en una única transacción.
     */
    public boolean registrarCliente(Cliente nuevoCliente) {
        Connection conexion = null;
        try {
            // 1. Obtener la conexión e INICIAR la transacción
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false); 

            // 2. Ejecutar la operación del DAO
            clienteDao.insertar(nuevoCliente, conexion);
            
            // 3. Confirmar transacción
            conexion.commit(); 
            System.out.println("Cliente con ID: " + nuevoCliente.getIdUsuario() + " registrado correctamente.");
            return true;
            
        } catch (SQLException e) {
            System.err.println("error SQL en el registro de cliente: " + e.getMessage());
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
    
    /**
     * Actualiza un cliente existente (Usuario y Cliente) en una única transacción.
     */
    public boolean actualizarCliente(Cliente clienteActualizado) {
        Connection conexion = null;
        try {
            // 1. Obtener la conexión e INICIAR la transacción
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false); 
            
            // 2. Ejecutar la operación del DAO
            clienteDao.actualizar(clienteActualizado, conexion);
            
            // 3. Confirmar transacción
            conexion.commit(); 
            System.out.println("Cliente con ID " + clienteActualizado.getIdUsuario() + " actualizado correctamente.");
            return true;
            
        } catch (SQLException e) {
            System.err.println("Error SQL al actualizar el cliente: " + e.getMessage());
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error fatal al intentar hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            // 4. Cerrar la conexión
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
     * Elimina un cliente existente (Cliente y Usuario) en una única transacción.
     */
    public boolean eliminarCliente(int idUsuario) {
        Connection conexion = null;
        try {
            // 1. Obtener la conexión e INICIAR la transacción
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false); 
            
            // 2. Ejecutar la operación del DAO
            clienteDao.eliminar(idUsuario, conexion);
            
            // 3. Confirmar transacción
            conexion.commit(); 
            System.out.println("Cliente con ID " + idUsuario + " eliminado correctamente.");
            return true;
            
        } catch (SQLException e) {
            System.err.println("❌  SQL al eliminar el cliente: " + e.getMessage());
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error fatal al intentar hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            // 4. Cerrar la conexión
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

    // OPERACIONES DE SOLO LECTURA

    public Cliente obtenerClientePorId(int idUsuario) {
        Cliente cliente = null;
        Connection conexion = null;
        try {
            conexion = ConexionBD.obtenerConexion();
            // Pasa la conexión para una consulta simple
            cliente = clienteDao.obtenerClientePorId(idUsuario, conexion); 
        } catch (SQLException e) {
            System.err.println("Error SQL al obtener cliente por ID: " + e.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión de consulta: " + e.getMessage());
                }
            }
        }
        return cliente;
    }
    

    public void mostrarTabla(JTable tabla) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("ID");
        modelo.addColumn("CEDULA");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("APELLIDO");
        modelo.addColumn("LICENCIA");
        modelo.addColumn("TELEFONO");

        ClienteDAO dao = new ClienteDAO();
        List<Cliente> lista = dao.listarClientes();

        for (Cliente cliente : lista) {
            Object[] datos = {
                cliente.getIdUsuario(),
                cliente.getCedula(),
                cliente.getNombre(),
                cliente.getApellido(),
                cliente.getLicenciaConduccion(),
                cliente.getTelefono()
            };
            modelo.addRow(datos);
        }

        tabla.setModel(modelo);
    }


    
    

        
}