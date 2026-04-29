package app.dao;

import app.modelo.Reporte;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReporteDAO {

    // Sentencias SQL
    private static final String SQL_INSERT = 
        "INSERT INTO Reporte (idEmpleado, tipo, fechaGeneracion) VALUES (?, ?, ?)";
    private static final String SQL_SELECT_BY_ID = 
        "SELECT idReporte, idEmpleado, tipo, fechaGeneracion FROM Reporte WHERE idReporte = ?";
    private static final String SQL_SELECT_ALL = 
        "SELECT idReporte, idEmpleado, tipo, fechaGeneracion FROM Reporte";
    private static final String SQL_DELETE = 
        "DELETE FROM Reporte WHERE idReporte = ?";

    // MAPEO DE RESULTADO
    private Reporte mapearReporte(ResultSet rs) throws SQLException {
        Reporte r = new Reporte();
        r.setIdReporte(rs.getInt("idReporte"));
        r.setIdEmpleado(rs.getInt("idEmpleado"));
        r.setTipo(rs.getString("tipo"));
        r.setFechaGeneracion(rs.getDate("fechaGeneracion").toLocalDate());
        return r;
    }
    
    // OPERACIONES CRUD TRANSACCIONALES (Reciben la conexión)

    /**
     * Inserta un nuevo Reporte y recupera el ID autogenerado.
     */
    public int insertar(Reporte r, Connection conexion) throws SQLException {
        int idGenerado = -1;
        
        try (PreparedStatement statement = conexion.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, r.getIdEmpleado());
            statement.setString(2, r.getTipo());
            statement.setDate(3, java.sql.Date.valueOf(r.getFechaGeneracion())); 
            
            int filasAfectadas = statement.executeUpdate();
            
            if (filasAfectadas > 0) {
                try (ResultSet rs = statement.getGeneratedKeys()) {
                    if (rs.next()) {
                        idGenerado = rs.getInt(1); 
                        r.setIdReporte(idGenerado); 
                    }
                }
            }
        }
        return idGenerado;
    }

    /**
     * Elimina el registro de un Reporte.
     */
    public boolean eliminar(int idReporte, Connection conexion) throws SQLException {
        int filasAfectadas = 0;
        try (PreparedStatement statement = conexion.prepareStatement(SQL_DELETE)) {
            statement.setInt(1, idReporte);
            filasAfectadas = statement.executeUpdate();
        }
        return filasAfectadas > 0;
    }
    

    public Reporte obtenerReportePorId(int idReporte, Connection conexion) throws SQLException {
        Reporte reporte = null;
        
        try (PreparedStatement statement = conexion.prepareStatement(SQL_SELECT_BY_ID)) {
            statement.setInt(1, idReporte);
            
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    reporte = mapearReporte(rs);
                }
            }
        }
        return reporte;
    }

    public List<Reporte> listarReportes() throws SQLException {
        List<Reporte> reportes = new ArrayList<>();
        
        try (Connection conn = ConexionBD.obtenerConexion(); 
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                reportes.add(mapearReporte(rs));
            }
        }
        return reportes;
    }
}