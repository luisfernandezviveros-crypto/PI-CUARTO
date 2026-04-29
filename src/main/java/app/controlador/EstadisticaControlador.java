package app.controlador;

import app.dao.EstadisticaDAO;
import java.util.Map;

public class EstadisticaControlador {

    private final EstadisticaDAO dao = new EstadisticaDAO();

    public void mostrarEstadisticasConsola() {
        System.out.println("===== ESTADÍSTICAS DEL SISTEMA =====");

        System.out.println("\n➡️ Reservas por tipo de vehículo:");
        Map<String, Integer> reservas = dao.obtenerReservasPorTipo();
        reservas.forEach((tipo, cantidad) -> 
            System.out.println(" - " + tipo + ": " + cantidad + " reservas"));

        System.out.println("\n💰 Ingresos totales por tipo de vehículo:");
        Map<String, Double> ingresos = dao.obtenerIngresosPorTipo();
        ingresos.forEach((tipo, total) -> 
            System.out.println(" - " + tipo + ": $" + total));

        System.out.println("\n👥 Clientes más frecuentes:");
        Map<String, Integer> clientes = dao.obtenerClientesFrecuentes();
        clientes.forEach((nombre, total) -> 
            System.out.println(" - " + nombre + ": " + total + " reservas"));
    }
}