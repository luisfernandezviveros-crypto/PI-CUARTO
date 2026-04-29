
package app.modelo;


import java.time.LocalDate;

public class Reporte {
    private int idReporte;          
    private int idEmpleado;          
    private String tipo;  // Ingresos, Reservas, Disponibilidad o Clientes
    private LocalDate fechaGeneracion; // Fecha en la que se generó el reporte

    // constructor sin idReporte (para INSERT en BD)
    public Reporte(int idEmpleado, String tipo, LocalDate fechaGeneracion) {
        this.idEmpleado = idEmpleado;
        this.tipo = tipo;
        this.fechaGeneracion = fechaGeneracion;
    }
    
    public Reporte(){}

    // Constructor completo (para SELECT desde BD)
    public Reporte(int idReporte, int idEmpleado, String tipo, LocalDate fechaGeneracion) {
        this.idReporte = idReporte;
        this.idEmpleado = idEmpleado;
        this.tipo = tipo;
        this.fechaGeneracion = fechaGeneracion;
    }

    // setters y guetters 
    public int getIdReporte() {
        return idReporte;
    }

    public void setIdReporte(int idReporte) {
        this.idReporte = idReporte;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

   
}

