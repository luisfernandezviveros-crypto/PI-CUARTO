package app.dao;

import app.modelo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// **AVISO**: Se eliminó toda la lógica de Scanner y la gestión de la conexión 
// en los métodos CRUD para que el controlador pueda manejar las transacciones.

public class ClienteDAO {
    ConexionBD conexion = new ConexionBD();
    // Sentencias SQL
    // Se corrige el rol en la inserción de Usuario (debe ser 'Cliente')
    private static final String SQL_INSERT_USUARIO =
            "INSERT INTO Usuario (idUsuario, cedula, nombre, apellido, correo, contrasena, direccion, rol) VALUES (?,?,?,?,?,?,?, 'Cliente')";
    private static final String SQL_INSERT_CLIENTE =
            "INSERT INTO Cliente (idUsuario, licenciaConduccion, telefono) VALUES (?,?,?)";
    
    private static final String SQL_SELECT_BY_ID =
        "SELECT U.idUsuario, U.cedula, U.nombre, U.apellido, U.correo, U.contrasena, U.direccion, U.rol, " +
        "C.licenciaConduccion, C.telefono " +
        "FROM Usuario U JOIN Cliente C ON U.idUsuario = C.idUsuario WHERE U.idUsuario = ?";
    
    private static final String SQL_SELECT_ALL = 
        "SELECT U.idUsuario, U.cedula, U.nombre, U.apellido, U.correo, U.contrasena, U.direccion, U.rol, " +
        "C.licenciaConduccion, C.telefono " +
        "FROM Usuario U JOIN Cliente C ON U.idUsuario = C.idUsuario";
    
    private static final String SQL_UPDATE_USUARIO =
            "UPDATE Usuario SET cedula=?, nombre=?, apellido=?, correo=?, contrasena=?, direccion=? WHERE idUsuario=?";
    private static final String SQL_UPDATE_CLIENTE =
            "UPDATE Cliente SET licenciaConduccion=?, telefono=? WHERE idUsuario=?";
    
    private static final String SQL_DELETE_CLIENTE = "DELETE FROM Cliente WHERE idUsuario = ?";
    private static final String SQL_DELETE_USUARIO = "DELETE FROM Usuario WHERE idUsuario = ?";

    // MAPEO DE RESULTADO
    private Cliente mapearCliente(ResultSet rs) throws SQLException {
        Cliente c = new Cliente();
        c.setIdUsuario(rs.getInt("idUsuario"));
        c.setCedula(rs.getString("cedula"));
        c.setNombre(rs.getString("nombre"));
        c.setApellido(rs.getString("apellido"));
        c.setCorreo(rs.getString("correo"));
        c.setContrasena(rs.getString("contrasena"));
        c.setDireccion(rs.getString("direccion"));
        c.setRol(rs.getString("rol"));
        c.setLicenciaConduccion(rs.getString("licenciaConduccion"));
        c.setTelefono(rs.getString("telefono"));
        return c;
    }
    
    // OPERACIONES CRUD TRANSACCIONALES (Reciben la conexión)

    /**
     * Inserta un nuevo Cliente (en Usuario y Cliente).
     * @param c
     * @param conexion
     * @throws java.sql.SQLException
     */
    public void insertar(Cliente c, Connection conexion) throws SQLException {
        // Insertar en Usuario
        try (PreparedStatement statement = conexion.prepareStatement(SQL_INSERT_USUARIO)) {
            statement.setInt(1, c.getIdUsuario());
            statement.setString(2, c.getCedula());
            statement.setString(3, c.getNombre());
            statement.setString(4, c.getApellido());
            statement.setString(5, c.getCorreo());
            statement.setString(6, c.getContrasena());
            statement.setString(7, c.getDireccion());
            // El rol 'Cliente' está hardcodeado en la SQL
            statement.executeUpdate();
        }
        
        //Insertar en Cliente
        try (PreparedStatement statement = conexion.prepareStatement(SQL_INSERT_CLIENTE)) {
            statement.setInt(1, c.getIdUsuario());
            statement.setString(2, c.getLicenciaConduccion());
            statement.setString(3, c.getTelefono());
            statement.executeUpdate();
        }
    }

    /**
     * Actualiza un Cliente (en Usuario y Cliente).
     */
    public void actualizar(Cliente c, Connection conexion) throws SQLException {
        //  Actualizar Usuario
        try (PreparedStatement statement = conexion.prepareStatement(SQL_UPDATE_USUARIO)) {
            statement.setString(1, c.getCedula());
            statement.setString(2, c.getNombre());
            statement.setString(3, c.getApellido());
            statement.setString(4, c.getCorreo());
            statement.setString(5, c.getContrasena());
            statement.setString(6, c.getDireccion());
            statement.setInt(7, c.getIdUsuario()); // WHERE
            statement.executeUpdate();
        }
        
        //  Actualizar Cliente
        try (PreparedStatement statement = conexion.prepareStatement(SQL_UPDATE_CLIENTE)) {
            statement.setString(1, c.getLicenciaConduccion());
            statement.setString(2, c.getTelefono());
            statement.setInt(3, c.getIdUsuario()); // WHERE
            statement.executeUpdate();
        }
    }

    /**
     * Elimina un Cliente (primero Cliente, luego Usuario por FOREIGN KEY).
     */
    public void eliminar(int idUsuario, Connection conexion) throws SQLException {
        // 1. Eliminar de Cliente
        try (PreparedStatement statement = conexion.prepareStatement(SQL_DELETE_CLIENTE)) {
            statement.setInt(1, idUsuario);
            statement.executeUpdate();
        }
        
        // 2. Eliminar de Usuario
        try (PreparedStatement statement = conexion.prepareStatement(SQL_DELETE_USUARIO)) {
            statement.setInt(1, idUsuario);
            statement.executeUpdate();
        }
    }
    
  
    public Cliente obtenerClientePorId(int idUsuario, Connection conexion) throws SQLException {
        Cliente cliente = null;
        try (PreparedStatement statement = conexion.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, idUsuario);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    cliente = mapearCliente(rs);
                }
            }
        }
        return cliente;
    }

    public List<Cliente> listarClientes() {
        List<Cliente> lista = new ArrayList<>();
        String sql = "SELECT u.idUsuario, u.cedula, u.nombre, u.apellido, c.licenciaConduccion, c.telefono "
                   + "FROM Usuario u INNER JOIN Cliente c ON u.idUsuario = c.idUsuario";

        try (Connection con = conexion.obtenerConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setIdUsuario(rs.getInt("idUsuario"));
                cliente.setCedula(rs.getString("cedula"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setLicenciaConduccion(rs.getString("licenciaConduccion"));
                cliente.setTelefono(rs.getString("telefono"));
                lista.add(cliente);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar clientes: " + e.getMessage());
        }
        return lista;
    }

}