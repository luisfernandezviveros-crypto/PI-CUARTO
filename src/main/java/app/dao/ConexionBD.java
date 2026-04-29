
package app.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConexionBD {
    
    //Establecemos las constantes para los datos de conexion a la base de datos y la tabla
    //Aplicamos el principio de Encapsulamiento y Ocultamiento (constantes privadas)***
    private static final String URL = "jdbc:mysql://localhost:3306/RentaFacilDB";
    private static final String USER = "root";
    private static final String PASSWORD = "3145947536";
    
    //Metodo para validar la conexión a la base de datos
    //Método para obtener la conexión (Ocultamiento de detalles de conexión)**
    /*(Ocultamiento de detalles de conexión): El objetivo principal de este método es ocultar los 
    detalles específicos de cómo se establece la conexión a la base de datos. 
    Los detalles, como la URL, el usuario y la contraseña, están encapsulados en la clase y no son expuestos externamente. 
    Esto sigue el principio de ocultamiento (o encapsulamiento) y mejora la modularidad del código, 
    ya que los cambios en la conexión no afectarán a otras partes del programa.*/

    public static Connection obtenerConexion() {
        try {
            Connection conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("CONECTADO");
            return conexion;
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
            throw new RuntimeException("No se pudo establecer la conexión a la base de datos.");
        }
    }
    
    //Metodo para validar si existe un usuario por su id 
    public static boolean existeUsuario(Connection conexion, int idUsuario) throws SQLException {
        String consulta = "SELECT * FROM Usuario WHERE idUsuario = ?";
        try (PreparedStatement statement = conexion.prepareStatement(consulta)) {
            //statement.setString(1, idUsuario);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }
    
    
}
