package app.modelo;

import java.time.LocalDate;

public class Reserva {
    // Atributos
    private int idReserva;
    private int idCliente;      // Relación con Cliente
    private String placa;       // Relación con Vehiculo
    private LocalDate fechaInicio;
    private LocalDate fechaEntrega;
    private String metodoPago;
    private double costoTotal;
    private String estado;

    public Reserva(){}
    // Constructor para registro (ID y costoTotal autogenerados/calculados)
    public Reserva(int idCliente, String placa, LocalDate fechaInicio, LocalDate fechaEntrega, String metodoPago, String estado) {
        this.idCliente = idCliente;
        this.placa = placa;
        this.fechaInicio = fechaInicio;
        this.fechaEntrega = fechaEntrega;
        this.metodoPago = metodoPago;
        this.estado = estado;
    }

    // Constructor completo (usado para obtener reservas desde la BD)
    public Reserva(int idReserva, int idCliente, String placa, LocalDate fechaInicio, LocalDate fechaEntrega, String metodoPago, double costoTotal, String estado) {
        this.idReserva = idReserva;
        this.idCliente = idCliente;
        this.placa = placa;
        this.fechaInicio = fechaInicio;
        this.fechaEntrega = fechaEntrega;
        this.metodoPago = metodoPago;
        this.costoTotal = costoTotal;
        this.estado = estado;
    }

    // Getters y Setters
    public int getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(int idReserva) {
        this.idReserva = idReserva;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaEntrega() {
        return fechaEntrega;
    }

    public void setFechaEntrega(LocalDate fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    public String getMetodoPago() {
        return metodoPago;
    }

    public void setMetodoPago(String metodoPago) {
        this.metodoPago = metodoPago;
    }

    public double getCostoTotal() {
        return costoTotal;
    }

    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}