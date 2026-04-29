package app.dao;

import app.modelo.Vehiculo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    // Sentencias SQL actualizadas
    private static final String SQL_INSERT =
        "INSERT INTO Vehiculo (placa, marca, modelo, anio, precioDia, estado, tipo, combustible, autonomiaKm, automatica, imagen) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_SELECT_BY_PLACA =
        "SELECT placa, marca, modelo, anio, precioDia, estado, tipo, combustible, autonomiaKm, automatica, imagen FROM Vehiculo WHERE placa = ?";
    
    private static final String SQL_SELECT_ALL =
        "SELECT placa, marca, modelo, anio, precioDia, estado, tipo, combustible, autonomiaKm, automatica, imagen FROM Vehiculo";
    
    private static final String SQL_UPDATE =
        "UPDATE Vehiculo SET marca=?, modelo=?, anio=?, precioDia=?, estado=?, tipo=?, combustible=?, autonomiaKm=?, automatica=?, imagen=? WHERE placa=?";
    
    private static final String SQL_DELETE =
        "DELETE FROM Vehiculo WHERE placa = ?";
    
    private static final String SQL_SELECT_PRECIO_DIA =
        "SELECT precioDia FROM Vehiculo WHERE placa = ?";


    // 🧩 Mapeo de un ResultSet a un objeto Vehiculo
    private Vehiculo mapearVehiculo(ResultSet rs) throws SQLException {
        Vehiculo v = new Vehiculo(
                rs.getString("placa"),
                rs.getString("marca"),
                rs.getString("modelo"),
                rs.getInt("anio"),
                rs.getDouble("precioDia"),
                rs.getString("estado"),
                rs.getString("tipo"),
                rs.getString("combustible"),
                rs.getObject("autonomiaKm") != null ? rs.getInt("autonomiaKm") : null,
                rs.getObject("automatica") != null ? rs.getBoolean("automatica") : null,
                rs.getString("imagen")
        );
        return v;
    }


    public double obtenerPrecioDia(String placa) throws SQLException {
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_PRECIO_DIA)) {
            stmt.setString(1, placa);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("precioDia");
                }
            }
        }
        return -1.0;
    }


    public boolean insertar(Vehiculo v, Connection conexion) throws SQLException {
        try (PreparedStatement statement = conexion.prepareStatement(SQL_INSERT)) {
            statement.setString(1, v.getPlaca());
            statement.setString(2, v.getMarca());
            statement.setString(3, v.getModelo());
            statement.setInt(4, v.getAnio());
            statement.setDouble(5, v.getPrecioDia());
            statement.setString(6, v.getEstado());
            statement.setString(7, v.getTipo());

            // Manejo de nulls
            if (v.getCombustible() != null) {
                statement.setString(8, v.getCombustible());
            } else {
                statement.setNull(8, java.sql.Types.VARCHAR);
            }

            if (v.getAutonomiaKm() != null) {
                statement.setInt(9, v.getAutonomiaKm());
            } else {
                statement.setNull(9, java.sql.Types.INTEGER);
            }

            if (v.getAutomatica() != null) {
                statement.setBoolean(10, v.getAutomatica());
            } else {
                statement.setNull(10, java.sql.Types.BOOLEAN);
            }

            if (v.getImagen() != null) {
                statement.setString(11, v.getImagen());
            } else {
                statement.setNull(11, java.sql.Types.VARCHAR);
            }

            return statement.executeUpdate() > 0;
        }
    }


    public boolean actualizar(Vehiculo v, Connection conexion) throws SQLException {
        try (PreparedStatement statement = conexion.prepareStatement(SQL_UPDATE)) {
            statement.setString(1, v.getMarca());
            statement.setString(2, v.getModelo());
            statement.setInt(3, v.getAnio());
            statement.setDouble(4, v.getPrecioDia());
            statement.setString(5, v.getEstado());
            statement.setString(6, v.getTipo());

            if (v.getCombustible() != null) {
                statement.setString(7, v.getCombustible());
            } else {
                statement.setNull(7, java.sql.Types.VARCHAR);
            }

            if (v.getAutonomiaKm() != null) {
                statement.setInt(8, v.getAutonomiaKm());
            } else {
                statement.setNull(8, java.sql.Types.INTEGER);
            }

            if (v.getAutomatica() != null) {
                statement.setBoolean(9, v.getAutomatica());
            } else {
                statement.setNull(9, java.sql.Types.BOOLEAN);
            }

            if (v.getImagen() != null) {
                statement.setString(10, v.getImagen());
            } else {
                statement.setNull(10, java.sql.Types.VARCHAR);
            }

            statement.setString(11, v.getPlaca());
            return statement.executeUpdate() > 0;
        }
    }

    public boolean eliminar(String placa, Connection conexion) throws SQLException {
        try (PreparedStatement statement = conexion.prepareStatement(SQL_DELETE)) {
            statement.setString(1, placa);
            return statement.executeUpdate() > 0;
        }
    }


    public Vehiculo obtenerVehiculoPorPlaca(String placa, Connection conexion) throws SQLException {
        Vehiculo vehiculo = null;
        try (PreparedStatement statement = conexion.prepareStatement(SQL_SELECT_BY_PLACA)) {
            statement.setString(1, placa);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    vehiculo = mapearVehiculo(rs);
                }
            }
        }
        return vehiculo;
    }


    // 🔹 Listar todos los vehículos
    public List<Vehiculo> listarVehiculos() throws SQLException {
        List<Vehiculo> vehiculos = new ArrayList<>();
        try (Connection conn = ConexionBD.obtenerConexion();
             PreparedStatement stmt = conn.prepareStatement(SQL_SELECT_ALL);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                vehiculos.add(mapearVehiculo(rs));
            }
        }
        return vehiculos;
    }
   
    public String obtenerEstado(String placa) {
        String estado = null;
        String sql = "SELECT estado FROM Vehiculo WHERE placa = ?";

        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, placa);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    estado = rs.getString("estado");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return estado;
    }
 
    public boolean actualizarEstado(String placa, String estado) {
        String sql = "UPDATE Vehiculo SET estado = ? WHERE placa = ?";

        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, estado);
            ps.setString(2, placa);
            int filas = ps.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public List<Vehiculo> listarVehiculosDisponibles(String tipo, String marca, Double precioMax) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM Vehiculo WHERE estado = 'Disponible'");

        // Agregamos filtros condicionales
        if (tipo != null && !tipo.isEmpty()) {
            sql.append(" AND tipo = ?");
        }
        if (marca != null && !marca.isEmpty()) {
            sql.append(" AND marca LIKE ?");
        }
        if (precioMax != null) {
            sql.append(" AND precioDia <= ?");
        }

        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            int index = 1;
            if (tipo != null && !tipo.isEmpty()) {
                ps.setString(index++, tipo);
            }
            if (marca != null && !marca.isEmpty()) {
                ps.setString(index++, "%" + marca + "%");
            }
            if (precioMax != null) {
                ps.setDouble(index++, precioMax);
            }

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                lista.add(mapearVehiculo(rs)); // tu método que convierte ResultSet a Vehiculo
            }
        }

        return lista;
    }
    public List<Vehiculo> listarVehiculosPorEstado(String estado) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Vehiculo WHERE estado = ?";

        try (Connection conn = ConexionBD.obtenerConexion(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, estado);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vehiculo v = new Vehiculo();
                v.setPlaca(rs.getString("placa"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));
                v.setAnio(rs.getInt("anio"));
                v.setPrecioDia(rs.getDouble("precioDia"));
                v.setEstado(rs.getString("estado"));
                v.setTipo(rs.getString("tipo"));
                v.setCombustible(rs.getString("combustible"));
                v.setAutonomiaKm(rs.getInt("autonomiaKm"));
                v.setAutomatica(rs.getBoolean("automatica"));
                v.setImagen(rs.getString("imagen")); // ruta de la imagen
                lista.add(v);
            }
        }
        return lista;
    }
    
    public List<Vehiculo> obtenerVehiculosPorTipo(String tipo) throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT * FROM Vehiculo WHERE tipo = ? AND estado = 'Disponible'";

        try (Connection con = ConexionBD.obtenerConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipo);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Vehiculo v = new Vehiculo();
                v.setPlaca(rs.getString("placa"));
                v.setMarca(rs.getString("marca"));
                v.setModelo(rs.getString("modelo"));
                v.setPrecioDia(rs.getDouble("precioDia"));
                v.setTipo(rs.getString("tipo"));
                v.setImagen(rs.getString("imagen"));
                lista.add(v);
            }
        }
        return lista;
    }
}
