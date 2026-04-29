package app.vista;

import app.controlador.ReporteControlador;
import app.modelo.Reporte;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class VistaReporte {
    
    private final ReporteControlador controlador;
    private final Scanner sc;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public VistaReporte() {
        this.controlador = new ReporteControlador();
        this.sc = new Scanner(System.in);
    }

    public void menuReporte() {
        int opcion;
        do {
            System.out.println("\n*** Gestión de Reportes ***");
            System.out.println("1. Generar y Registrar Nuevo Reporte");
            System.out.println("2. Ver Reporte por ID");
            System.out.println("3. Listar Todos los Reportes");
            System.out.println("4. Eliminar Reporte");
            System.out.println("0. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            
            try {
                opcion = sc.nextInt();
                sc.nextLine(); 

                switch (opcion) {
                    case 1: generarReporte(); break;
                    case 2: verReportePorId(); break;
                    case 3: listarReportes(); break;
                    case 4: eliminarReporte(); break;
                    case 0: System.out.println("Volviendo..."); break;
                    default: System.out.println("Opción no válida.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Entrada invalida. Por favor ingrese un numero.");
                sc.nextLine(); 
                opcion = -1;
            }
        } while (opcion != 0);
    }
    
    private void generarReporte() {
        System.out.println("\n*** Generar Nuevo Reporte ***");
        try {
            System.out.print("ID del Empleado que genera el reporte: ");
            int idEmpleado = sc.nextInt();
            sc.nextLine(); 
            
            System.out.println("Tipos de Reporte disponibles: Ingresos, Reservas, Disponibilidad, Clientes");
            System.out.print("Ingrese el tipo de reporte a generar: ");
            String tipo = sc.nextLine().trim();

            controlador.generarYRegistrarReporte(idEmpleado, tipo); 

        } catch (InputMismatchException e) {
            System.err.println("Error de formato de entrada. Asegúrese de ingresar números donde corresponde.");
            sc.nextLine(); 
        }
    }
    
    private void verReportePorId() {
        System.out.println("\n*** Ver Reporte por ID ***");
        try {
            System.out.print("Ingrese ID del Reporte a buscar: ");
            int idReporte = sc.nextInt();
            sc.nextLine();
            
            Reporte reporte = controlador.obtenerReportePorId(idReporte);
            
            if (reporte != null) {
                System.out.println("\nReporte Encontrado:");
                imprimirReporte(reporte);
            } else {
                System.out.println("Reporte con ID " + idReporte + " no encontrado.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Entrada inválida. Ingrese un numero para el ID.");
            sc.nextLine();
        }
    }
    
    private void listarReportes() {
        System.out.println("\n*** Listado de Reportes ***");
        List<Reporte> reportes = controlador.listarReportes();
        
        if (reportes.isEmpty()) {
            System.out.println("No hay reportes registrados.");
            return;
        }
        
        System.out.printf("| %-4s | %-10s | %-15s | %-12s |\n", "ID", "Empleado", "Tipo", "Fecha");
        System.out.println("--------------------------------------------------");
        for (Reporte r : reportes) {
            System.out.printf("| %-4d | %-10d | %-15s | %-12s |\n", 
                r.getIdReporte(), 
                r.getIdEmpleado(), 
                r.getTipo(),
                r.getFechaGeneracion().format(DATE_FORMATTER));
        }
    }
    
    private void eliminarReporte() {
        System.out.println("\n*** Eliminar Reporte ***");
        try {
            System.out.print("Ingrese ID del Reporte a eliminar: ");
            int idReporte = sc.nextInt();
            sc.nextLine();

            System.out.print("¿Esta seguro que desea eliminar el reporte " + idReporte + "? (S/N): ");
            String confirmacion = sc.nextLine().trim().toUpperCase();

            if (confirmacion.equals("S")) {
                controlador.eliminarReporte(idReporte);
            } else {
                System.out.println("Eliminación cancelada.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Entrada inválida. Ingrese un número para el ID.");
            sc.nextLine();
        }
    }

    private void imprimirReporte(Reporte r) {
        System.out.println("  ID Reporte: " + r.getIdReporte());
        System.out.println("  ID Empleado: " + r.getIdEmpleado());
        System.out.println("  Tipo: " + r.getTipo());
        System.out.println("  Fecha Generación: " + r.getFechaGeneracion().format(DATE_FORMATTER));
    }
}