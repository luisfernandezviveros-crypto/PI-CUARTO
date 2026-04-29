

package app.vista;

import app.controlador.ReservaControlador;
import app.controlador.VehiculoControlador; 
import app.modelo.Reserva;
import app.modelo.Vehiculo; 
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors; // Necesario para el filtrado de lista

public class VistaReserva {
    
    private final ReservaControlador controlador;
    private final VehiculoControlador vehiculoControlador; // Controlador de Vehículos
    private final Scanner sc;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-DD HH:mm");

    public VistaReserva() {
        this.controlador = new ReservaControlador();
        this.vehiculoControlador = new VehiculoControlador(); // Inicialización
        this.sc = new Scanner(System.in);
    }

    public void menuReserva() {
        int opcion;
        do {
            System.out.println("\n*** Gestion de Reservas ***");
            System.out.println("1. Registrar Nueva Reserva");
            System.out.println("2. Ver Reserva por ID");
            System.out.println("3. Listar Todas las Reservas");
            System.out.println("4. Actualizar Reserva");
            System.out.println("5. Eliminar Reserva");
            System.out.println("0. Volver al Menu Principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = sc.nextInt();
                sc.nextLine(); 

                switch (opcion) {
                    case 1: crearReserva(); break;
                    case 2: verReservaPorId(); break;
                    case 3: listarReservas(); break;
                    case 4: actualizarReserva(); break;
                    case 5: eliminarReserva(); break;
                    case 0: System.out.println("Volviendo..."); break;
                    default: System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Entrada inválida. Por favor, ingrese un número.");
                sc.nextLine(); 
                opcion = -1;
            }
        } while (opcion != 0);
    }

    
    /**
     * Muestra una lista de vehículos con estado "Disponible".
     */
    private void mostrarVehiculosDisponibles() {
        System.out.println("\n*** Vehiculos Disponibles para Reserva ***");
        
        List<Vehiculo> vehiculos = vehiculoControlador.listarVehiculos();
        
        // Filtrar solo los disponibles
        List<Vehiculo> disponibles = vehiculos.stream()
            .filter(v -> "Disponible".equalsIgnoreCase(v.getEstado()))
            .collect(Collectors.toList());

        if (disponibles.isEmpty()) {
            System.out.println("Actualmente no hay vehiculos disponibles para reserva.");
            return;
        }
        
        System.out.printf("| %-10s | %-10s | %-15s | %-5s | %-12s |\n", "Placa", "Marca", "Modelo", "Año", "Precio/Día");
        System.out.println("---------------------------------------------------------------");
        for (Vehiculo v : disponibles) {
            System.out.printf("| %-10s | %-10s | %-15s | %-5d | %-12.2f |\n", 
                v.getPlaca(), 
                v.getMarca(), 
                v.getModelo(),
                v.getAnio(),
                v.getPrecioDia());
        }
        System.out.println("");
    }
    

    // metodo para crear una reserva
    private void crearReserva() {
    System.out.println("\n*** Registro de Nueva Reserva ***");
    try {
        // Entrada de Datos (Cliente y Vehículo) 
        
        System.out.print("Ingrese ID del cliente: ");
        int idCliente = sc.nextInt();
        sc.nextLine(); 
        
        mostrarVehiculosDisponibles(); 
        
        System.out.print("Ingrese la Placa del Vehículo a reservar: ");
        String placa = sc.nextLine();
        
        System.out.print("Ingrese la cantidad de días del alquiler: ");
        long diasDuracion = sc.nextLong(); 
        sc.nextLine(); 
        
        if (diasDuracion <= 0) {
             System.err.println("Error: La reserva debe durar al menos un día.");
             return;
        }

        // CÁLCULO AUTOMÁTICO DE FECHAS (Usando la fecha del sistema) 
        
        // aFecha de Inicio: Fecha y hora exacta del sistema (tu computador)
        LocalDateTime fechaInicio = LocalDateTime.now();
        
        // b. Fecha de Entrega: Se suma la duración a la fecha de inicio.
        //    Se usa `diasDuracion - 1` para ser consistente con la lógica de costo
        //    de tu controlador (`ChronoUnit.DAYS.between + 1`).
        LocalDate fechaEntrega = fechaInicio.toLocalDate().plusDays(diasDuracion - 1);
        
        // Muestra las fechas calculadas al usuario
        System.out.println("\n Fecha de Inicio (automática): " + fechaInicio.format(DATETIME_FORMATTER));
        System.out.println(" Fecha de Entrega calculada: " + fechaEntrega.format(DATE_FORMATTER));
        
        // --- 3. Llamada al Controlador ---
        
        Reserva nuevaReserva = new Reserva(idCliente, placa, fechaInicio, fechaEntrega, metodoPago, "Activa");
        nuevaReserva.setIdCliente(idCliente);
        nuevaReserva.setPlaca(placa);
        nuevaReserva.setFechaInicio(fechaInicio); 
        nuevaReserva.setFechaEntrega(fechaEntrega); 

        // El controlador calcula el costo y registra la transaccion
        controlador.registrarReserva(nuevaReserva); 

    } catch (InputMismatchException e) {
        System.err.println("Error de formato de entrada. Verifique que el ID de cliente y la cantidad de días sean números.");
        sc.nextLine(); 
    } catch (Exception e) {
        System.err.println("Error al procesar la reserva: " + e.getMessage());
    }
}
    
    
   private void verReservaPorId() {
        System.out.println("\n*** Ver Reserva por ID ***");
        try {
            System.out.print("Ingrese ID de la Reserva a buscar: ");
            int idReserva = sc.nextInt();
            sc.nextLine();
            
            Reserva reserva = controlador.obtenerReservaPorId(idReserva);
            
            if (reserva != null) {
                System.out.println("\nReserva Encontrada:");
                imprimirReserva(reserva);
            } else {
                System.out.println("Reserva con ID " + idReserva + " no encontrada.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Entrada invalida. Ingrese un numero para el ID.");
            sc.nextLine();
        }
    }
    
    private void listarReservas() {
        System.out.println("\n*** Listado de Reservas ***");
        List<Reserva> reservas = controlador.listarReservas(tablaReservas);
        
        if (reservas.isEmpty()) {
            System.out.println("No hay reservas registradas.");
            return;
        }
        
        System.out.printf("| %-4s | %-10s | %-10s | %-20s | %-12s | %-10s |\n", "ID", "Cliente", "Placa", "Fecha Inicio", "Costo Total", "Estado");
        System.out.println("");
        for (Reserva r : reservas) {
            System.out.printf("| %-4d | %-10d | %-10s | %-20s | %-12.2f | %-10s |\n", 
                r.getIdReserva(), 
                r.getIdCliente(), 
                r.getPlaca(),
                r.getFechaInicio().format(DATETIME_FORMATTER),
                r.getCostoTotal(),
                r.getEstado());
        }
    }

    private void actualizarReserva() {
        System.out.println("\n*** Actualizar ***");
        try {
            System.out.print("Ingrese ID de la Reserva a actualizar: ");
            int idReserva = sc.nextInt();
            sc.nextLine();

            Reserva reservaActual = controlador.obtenerReservaPorId(idReserva);

            if (reservaActual == null) {
                System.out.println("Reserva con ID " + idReserva + " no encontrada.");
                return;
            }

            System.out.println("\nDatos actuales de la reserva:");
            imprimirReserva(reservaActual);
            
            //solo actualizar fecha de entrega y estado
            System.out.printf("Nueva Fecha de Entrega (YYYY-MM-DD) [%s]: ", reservaActual.getFechaEntrega().format(DATE_FORMATTER));
            String fechaEntregaStr = sc.nextLine().trim();
            if (!fechaEntregaStr.isEmpty()) {
                reservaActual.setFechaEntrega(LocalDate.parse(fechaEntregaStr, DATE_FORMATTER));
            }
            
            System.out.printf("Nuevo Estado (Activa, Completada, Cancelada) [%s]: ", reservaActual.getEstado());
            String estado = sc.nextLine().trim();
            if (!estado.isEmpty()) reservaActual.setEstado(estado);

            // El costo se recalcula automáticamente en el controlador
            controlador.actualizarReserva(reservaActual);
            
        } catch (Exception e) {
            System.err.println("Error al actualizar la reserva: " + e.getMessage());
        }
    }
    
    private void eliminarReserva() {
        System.out.println("\n*** Eliminar Reserva ***");
        try {
            System.out.print("Ingrese ID de la Reserva a eliminar: ");
            int idReserva = sc.nextInt();
            sc.nextLine();

            System.out.print("¿Está seguro que desea eliminar la reserva " + idReserva + "? (S/N): ");
            String confirmacion = sc.nextLine().trim().toUpperCase();

            if (confirmacion.equals("S")) {
                controlador.eliminarReserva(idReserva);
            } else {
                System.out.println("Eliminacion cancelada.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Entrada invalida. Ingrese un numero para el ID.");
            sc.nextLine();
        }
    }

    private void imprimirReserva(Reserva r) {
        System.out.println("  ID Reserva: " + r.getIdReserva());
        System.out.println("  ID Cliente: " + r.getIdCliente());
        System.out.println("  Placa Vehículo: " + r.getPlaca());
        System.out.println("  Fecha Inicio: " + r.getFechaInicio().format(DATETIME_FORMATTER));
        System.out.println("  Fecha Entrega: " + r.getFechaEntrega().format(DATE_FORMATTER));
        System.out.println("  Costo Total: " + r.getCostoTotal());
        System.out.println("  Estado: " + r.getEstado());
    }
}