package app.controlador;

import app.dao.ConexionBD;
import app.modelo.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Controlador de Autenticacion
 * Maneja el login, registro y validacion de usuarios en el sistema.
 */
public class AutenticacionControlador {
    
    // Sentencias SQL
    private static final String SQL_LOGIN = 
        "SELECT idUsuario, cedula, nombre, apellido, correo, contrasena, direccion, rol " +
        "FROM Usuario WHERE correo = ? AND contrasena = ?";
    
    private static final String SQL_VERIFICAR_CORREO = 
        "SELECT COUNT(*) as total FROM Usuario WHERE correo = ?";
    
    private static final String SQL_REGISTRAR_USUARIO = 
        "INSERT INTO Usuario (cedula, nombre, apellido, correo, contrasena, direccion, rol) " +
        "VALUES (?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_REGISTRAR_CLIENTE = 
        "INSERT INTO Cliente (idUsuario, licenciaConduccion, telefono) VALUES (?, ?, ?)";
    
    private static final String SQL_REGISTRAR_EMPLEADO = 
        "INSERT INTO Empleado (idUsuario, cargo) VALUES (?, ?)";
    
    // ============================================================
    // LOGIN
    // ============================================================
    public Usuario login(String correo, String contrasena) {
        Usuario usuario = null;
        Connection conexion = null;
        
        try {
            conexion = ConexionBD.obtenerConexion();
            
            try (PreparedStatement statement = conexion.prepareStatement(SQL_LOGIN)) {
                statement.setString(1, correo);
                statement.setString(2, contrasena);
                
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        usuario = new Usuario();
                        usuario.setIdUsuario(rs.getInt("idUsuario"));
                        usuario.setCedula(rs.getString("cedula"));
                        usuario.setNombre(rs.getString("nombre"));
                        usuario.setApellido(rs.getString("apellido"));
                        usuario.setCorreo(rs.getString("correo"));
                        usuario.setContrasena(rs.getString("contrasena"));
                        usuario.setDireccion(rs.getString("direccion"));
                        usuario.setRol(rs.getString("rol"));
                        
                        System.out.println("Login exitoso: " + usuario.getNombre() + " " + usuario.getApellido() + " (" + usuario.getRol() + ")");
                    } else {
                        System.err.println("Credenciales inválidas para el correo: " + correo);
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("ERROR SQL en el login: " + e.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexion: " + e.getMessage());
                }
            }
        }
        
        return usuario;
    }

    // REGISTRO DE USUARIO GENERAL (Administrador o básico)
    public boolean registrarUsuarioBasico(String cedula, String nombre, String apellido, 
                                          String correo, String contrasena, String direccion, 
                                          String rol) {
        Connection conexion = null;
        
        try {
            if (correoExiste(correo)) {
                System.err.println("Error: El correo '" + correo + "' ya está registrado.");
                return false;
            }
            
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);
            
            try (PreparedStatement statement = conexion.prepareStatement(SQL_REGISTRAR_USUARIO)) {
                statement.setString(1, cedula);
                statement.setString(2, nombre);
                statement.setString(3, apellido);
                statement.setString(4, correo);
                statement.setString(5, contrasena);
                statement.setString(6, direccion);
                statement.setString(7, rol);
                
                int filasAfectadas = statement.executeUpdate();
                
                if (filasAfectadas > 0) {
                    conexion.commit();
                    System.out.println("Usuario registrado correctamente: " + nombre + " " + apellido + " (Rol: " + rol + ")");
                    return true;
                } else {
                    conexion.rollback();
                    System.err.println("Error: No se pudo registrar el usuario.");
                    return false;
                }
            }
            
        } catch (SQLException e) {
            System.err.println("ERROR SQL en el registro básico: " + e.getMessage());
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al intentar hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
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

    // ============================================================
    // REGISTRO CLIENTE COMPLETO
    // ============================================================
    public boolean registrarClienteCompleto(String cedula, String nombre, String apellido, 
                                            String correo, String contrasena, String direccion,
                                            String licenciaConduccion, String telefono) {
        Connection conexion = null;
        
        try {
            if (correoExiste(correo)) {
                System.err.println("Error: El correo '" + correo + "' ya está registrado.");
                return false;
            }
            
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);
            
            int idUsuarioGenerado = -1;
            try (PreparedStatement statement = conexion.prepareStatement(
                    SQL_REGISTRAR_USUARIO, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, cedula);
                statement.setString(2, nombre);
                statement.setString(3, apellido);
                statement.setString(4, correo);
                statement.setString(5, contrasena);
                statement.setString(6, direccion);
                statement.setString(7, "Cliente");
                
                int filasAfectadas = statement.executeUpdate();
                
                if (filasAfectadas > 0) {
                    try (ResultSet rs = statement.getGeneratedKeys()) {
                        if (rs.next()) {
                            idUsuarioGenerado = rs.getInt(1);
                        }
                    }
                } else {
                    throw new SQLException("No se pudo insertar el usuario.");
                }
            }
            
            try (PreparedStatement statement = conexion.prepareStatement(SQL_REGISTRAR_CLIENTE)) {
                statement.setInt(1, idUsuarioGenerado);
                statement.setString(2, licenciaConduccion);
                statement.setString(3, telefono);
                statement.executeUpdate();
            }
            
            conexion.commit();
            System.out.println("Cliente registrado correctamente: " + nombre + " " + apellido + " (ID: " + idUsuarioGenerado + ")");
            return true;
            
        } catch (SQLException e) {
            System.err.println("ERROR SQL en el registro de cliente: " + e.getMessage());
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al intentar hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
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

    // ============================================================
    // REGISTRO EMPLEADO COMPLETO
    // ============================================================
    public boolean registrarEmpleadoCompleto(String cedula, String nombre, String apellido, 
                                             String correo, String contrasena, String direccion,
                                             String cargo) {
        Connection conexion = null;
        
        try {
            if (correoExiste(correo)) {
                System.err.println("Error: El correo '" + correo + "' ya está registrado.");
                return false;
            }
            
            conexion = ConexionBD.obtenerConexion();
            conexion.setAutoCommit(false);
            
            int idUsuarioGenerado = -1;
            try (PreparedStatement statement = conexion.prepareStatement(
                    SQL_REGISTRAR_USUARIO, Statement.RETURN_GENERATED_KEYS)) {
                statement.setString(1, cedula);
                statement.setString(2, nombre);
                statement.setString(3, apellido);
                statement.setString(4, correo);
                statement.setString(5, contrasena);
                statement.setString(6, direccion);
                statement.setString(7, "Empleado");
                
                int filasAfectadas = statement.executeUpdate();
                
                if (filasAfectadas > 0) {
                    try (ResultSet rs = statement.getGeneratedKeys()) {
                        if (rs.next()) {
                            idUsuarioGenerado = rs.getInt(1);
                        }
                    }
                } else {
                    throw new SQLException("No se pudo insertar el usuario.");
                }
            }
            
            try (PreparedStatement statement = conexion.prepareStatement(SQL_REGISTRAR_EMPLEADO)) {
                statement.setInt(1, idUsuarioGenerado);
                statement.setString(2, cargo);
                statement.executeUpdate();
            }
            
            conexion.commit();
            System.out.println("Empleado registrado correctamente: " + nombre + " " + apellido + " (ID: " + idUsuarioGenerado + ")");
            return true;
            
        } catch (SQLException e) {
            System.err.println("ERROR SQL en el registro de empleado: " + e.getMessage());
            if (conexion != null) {
                try {
                    conexion.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al intentar hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
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

    // MÉTODOS DE APOYO
    private boolean correoExiste(String correo) {
        Connection conexion = null;
        
        try {
            conexion = ConexionBD.obtenerConexion();
            
            try (PreparedStatement statement = conexion.prepareStatement(SQL_VERIFICAR_CORREO)) {
                statement.setString(1, correo);
                
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        return rs.getInt("total") > 0;
                    }
                }
            }
            
        } catch (SQLException e) {
            System.err.println("ERROR SQL al verificar correo: " + e.getMessage());
        } finally {
            if (conexion != null) {
                try {
                    conexion.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
        }
        
        return false;
    }
    
    public boolean validarFormatoCorreo(String correo) {
        if (correo == null || correo.trim().isEmpty()) return false;
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return correo.matches(regex);
    }
    
    public boolean validarContrasena(String contrasena) {
        if (contrasena == null || contrasena.length() < 6) {
            System.err.println("La contraseña debe tener al menos 6 caracteres.");
            return false;
        }
        return true;
    }
    
    public void logout(Usuario usuario) {
        if (usuario != null) {
            System.out.println("Sesión cerrada: " + usuario.getNombre() + " " + usuario.getApellido());
        }
    }
}
