package app.dao;

import app.modelo.Empleado;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {
    
    // 🔹 Sentencias SQL
    private static final String SQL_INSERT_USUARIO =
        "INSERT INTO Usuario (cedula, nombre, apellido, correo, contrasena, direccion, rol) VALUES (?,?,?,?,?,?, 'Empleado')";
    
    private static final String SQL_INSERT_EMPLEADO =
        "INSERT INTO Empleado (idUsuario, cargo) VALUES (?, ?)";
    
    private static final String SQL_SELECT_BY_ID =
        "SELECT U.idUsuario, U.cedula, U.nombre, U.apellido, U.correo, U.contrasena, U.direccion, U.rol, " +
        "E.cargo " + 
        "FROM Usuario U JOIN Empleado E ON U.idUsuario = E.idUsuario WHERE U.idUsuario = ?";
    
    private static final String SQL_SELECT_ALL = 
        "SELECT U.idUsuario, U.cedula, U.nombre, U.apellido, U.correo, U.contrasena, U.direccion, U.rol, " +
        "E.cargo " + 
        "FROM Usuario U JOIN Empleado E ON U.idUsuario = E.idUsuario";
    
    private static final String SQL_UPDATE_USUARIO =
        "UPDATE Usuario SET cedula=?, nombre=?, apellido=?, correo=?, contrasena=?, direccion=? WHERE idUsuario=?";
    
    private static final String SQL_UPDATE_EMPLEADO =
        "UPDATE Empleado SET cargo=? WHERE idUsuario=?";
    
    private static final String SQL_DELETE_EMPLEADO = 
        "DELETE FROM Empleado WHERE idUsuario = ?";
    
    private static final String SQL_DELETE_USUARIO = 
        "DELETE FROM Usuario WHERE idUsuario = ?";
    
    // 🔹 Mapeo de ResultSet a objeto Empleado
    private Empleado mapearEmpleado(ResultSet rs) throws SQLException {
        Empleado e = new Empleado();
        e.setIdUsuario(rs.getInt("idUsuario"));
        e.setCedula(rs.getString("cedula"));
        e.setNombre(rs.getString("nombre"));
        e.setApellido(rs.getString("apellido"));
        e.setCorreo(rs.getString("correo"));
        e.setContrasena(rs.getString("contrasena"));
        e.setDireccion(rs.getString("direccion"));
        e.setRol(rs.getString("rol"));
        e.setCargo(rs.getString("cargo"));
        return e;
    }
   
    // 🔹 Inserta un nuevo Empleado (en Usuario y Empleado)
    public void insertar(Empleado e, Connection conexion) throws SQLException {
        // 1️⃣ Insertar en Usuario
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_USUARIO, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, e.getCedula());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getApellido());
            ps.setString(4, e.getCorreo());
            ps.setString(5, e.getContrasena());
            ps.setString(6, e.getDireccion());
            ps.executeUpdate();

            // Obtener idUsuario generado automáticamente
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                e.setIdUsuario(rs.getInt(1));
            }
        }
        
        // 2️⃣ Insertar en Empleado
        try (PreparedStatement ps = conexion.prepareStatement(SQL_INSERT_EMPLEADO)) {
            ps.setInt(1, e.getIdUsuario());
            ps.setString(2, e.getCargo());
            ps.executeUpdate();
        }
    }

    // 🔹 Actualiza un Empleado (en Usuario y Empleado)
    public void actualizar(Empleado e, Connection conexion) throws SQLException {
        // Actualizar Usuario
        try (PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_USUARIO)) {
            ps.setString(1, e.getCedula());
            ps.setString(2, e.getNombre());
            ps.setString(3, e.getApellido());
            ps.setString(4, e.getCorreo());
            ps.setString(5, e.getContrasena());
            ps.setString(6, e.getDireccion());
            ps.setInt(7, e.getIdUsuario());
            ps.executeUpdate();
        }
        
        // Actualizar Empleado
        try (PreparedStatement ps = conexion.prepareStatement(SQL_UPDATE_EMPLEADO)) {
            ps.setString(1, e.getCargo());
            ps.setInt(2, e.getIdUsuario());
            ps.executeUpdate();
        }
    }

    // 🔹 Elimina un Empleado (primero en Empleado, luego Usuario)
    public void eliminar(int idUsuario, Connection conexion) throws SQLException {
        // 1️⃣ Eliminar de Empleado
        try (PreparedStatement ps = conexion.prepareStatement(SQL_DELETE_EMPLEADO)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
        
        // 2️⃣ Eliminar de Usuario
        try (PreparedStatement ps = conexion.prepareStatement(SQL_DELETE_USUARIO)) {
            ps.setInt(1, idUsuario);
            ps.executeUpdate();
        }
    }

    // 🔹 Buscar empleado por ID
    public Empleado obtenerEmpleadoPorId(int idUsuario, Connection conexion) throws SQLException {
        Empleado empleado = null;
        try (PreparedStatement ps = conexion.prepareStatement(SQL_SELECT_BY_ID)) {
            ps.setInt(1, idUsuario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    empleado = mapearEmpleado(rs);
                }
            }
        }
        return empleado;
    }

    // 🔹 Listar todos los empleados
    public List<Empleado> listarEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement ps = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                empleados.add(mapearEmpleado(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error al listar empleados: " + e.getMessage());
        }
        return empleados;
    }
}
