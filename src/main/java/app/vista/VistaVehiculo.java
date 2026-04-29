package app.vista;

import app.controlador.VehiculoControlador;
import app.modelo.Vehiculo;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class VistaVehiculo {
    
    private final VehiculoControlador controlador;
    private final Scanner sc;

    public VistaVehiculo() {
        this.controlador = new VehiculoControlador();
        this.sc = new Scanner(System.in);
    }

    public void menuVehiculo() {
        int opcion;
        do {
            System.out.println("\n*** Gestion de Vehiculos ***");
            System.out.println("1. Registrar Nuevo Vehiculo");
            System.out.println("2. Ver Vehiculo por Placa");
            System.out.println("3. Listar Todos los Vehículos");
            System.out.println("4. Actualizar Vehículo");
            System.out.println("5. Eliminar Vehiculo");
            System.out.println("0. Volver al Menu Principal");
            System.out.print("Seleccione una opcion: ");
            
            try {
                opcion = sc.nextInt();
                sc.nextLine(); 

                switch (opcion) {
                    case 1: crearVehiculo(); break;
                    case 2: verVehiculoPorPlaca(); break;
                    case 3: listarVehiculos(); break;
                    case 4: actualizarVehiculo(); break;
                    case 5: eliminarVehiculo(); break;
                    case 0: System.out.println("Volviendo..."); break;
                    default: System.out.println("Opcion no valida.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Entrada inválida. Por favor, ingrese un número.");
                sc.nextLine(); 
                opcion = -1;
            }
        } while (opcion != 0);
    }
    
    private void crearVehiculo() {
        System.out.println("\n*** Registro de Nuevo Vehículo ***");
        try {
            System.out.print("Placa: ");
            String placa = sc.nextLine();
            System.out.print("Marca: ");
            String marca = sc.nextLine();
            System.out.print("Modelo: ");
            String modelo = sc.nextLine();
            System.out.print("Año: ");
            int anio = sc.nextInt();
            System.out.print("Precio por Dia: ");
            double precioDia = sc.nextDouble();
            sc.nextLine(); 
            String estado = "Disponible";
            
            System.out.print("Tipo (Carro, Bicicleta Electrica, Monopatin electrico): ");
            String tipo = sc.nextLine();
            
            // Campos opcionales (combustible, autonomia, automática)
            System.out.print("Combustible (dejar vacío si no aplica): ");
            String combustibleStr = sc.nextLine().trim();
            String combustible = combustibleStr.isEmpty() ? null : combustibleStr;
            
            System.out.print("Autonomia Km (dejar 0 si no aplica): ");
            int autonomia = sc.nextInt();
            Integer autonomiaKm = (autonomia == 0) ? null : autonomia;
            sc.nextLine();
            
            System.out.print("¿Es automatico? (S/N): ");
            String auto = sc.nextLine().trim().toUpperCase();
            Boolean automatica = auto.equals("S") ? true : (auto.equals("N") ? false : null);

            // El estado inicial siempre es "Disponible"
            Vehiculo nuevoVehiculo = new Vehiculo(tipo, auto, auto, 0, auto, auto, auto);
            controlador.registrarVehiculo(nuevoVehiculo); 
            

        } catch (InputMismatchException e) {
            System.err.println("Error de formato de entrada. Asegurese de ingresar numeros donde corresponde.");
            sc.nextLine(); 
        }
    }
    
    private void verVehiculoPorPlaca() {
        System.out.println("\n*** Ver Vehiculo por Placa ***");
        System.out.print("Ingrese Placa del Vehiculo a buscar: ");
        String placa = sc.nextLine();
        
        Vehiculo vehiculo = controlador.obtenerVehiculoPorPlaca(placa);
        
        if (vehiculo != null) {
            System.out.println("\n Vehiculo Encontrado:");
            imprimirVehiculo(vehiculo);
        } else {
            System.out.println("Vehiculo con Placa " + placa + " no encontrado.");
        }
    }
    
    private void listarVehiculos() {
        System.out.println("\n*** Listado de Vehiculos ***");
        List<Vehiculo> vehiculos = controlador.listarVehiculos();
        
        if (vehiculos.isEmpty()) {
            System.out.println("No hay vehiculos registrados.");
            return;
        }
        
        System.out.printf("| %-10s | %-10s | %-15s | %-5s | %-12s | %-12s |\n", "Placa", "Marca", "Modelo", "Año", "Precio/Dia", "Estado");
        System.out.println("----------------------------------------------------------------------------------");
        for (Vehiculo v : vehiculos) {
            System.out.printf("| %-10s | %-10s | %-15s | %-5d | %-12.2f | %-12s |\n", 
                v.getPlaca(), 
                v.getMarca(), 
                v.getModelo(),
                v.getAnio(),
                v.getPrecioDia(),
                v.getEstado());
        }
    }
    // actualizar un vehiculo
    private void actualizarVehiculo() {
        System.out.println("\n*** Actualizar Vehiculo ***");
        try {
            System.out.print("Ingrese Placa del Vehículo a actualizar: ");
            String placa = sc.nextLine();

            Vehiculo vehiculoActual = controlador.obtenerVehiculoPorPlaca(placa);

            if (vehiculoActual == null) {
                System.out.println("Vehiculo con Placa " + placa + " no encontrado.");
                return;
            }

            System.out.println("\nDatos actuales del vehiculo:");
            imprimirVehiculo(vehiculoActual);
            
            //actualización simple: solo el precio y el estado
            System.out.printf("Nuevo Precio por Día [%.2f]: ", vehiculoActual.getPrecioDia());
            String precioStr = sc.nextLine().trim();
            if (!precioStr.isEmpty()) {
                vehiculoActual.setPrecioDia(Double.parseDouble(precioStr));
            }

            System.out.printf("Nuevo Estado (Disponible, Reservado, Mantenimiento) [%s]: ", vehiculoActual.getEstado());
            String estado = sc.nextLine().trim();
            if (!estado.isEmpty()) vehiculoActual.setEstado(estado);

            controlador.actualizarVehiculo(vehiculoActual);
            
        } catch (Exception e) {
            System.err.println("Error al actualizar el vehículo: " + e.getMessage());
        }
    }
    
    private void eliminarVehiculo() {
        System.out.println("\n*** Eliminar Vehículo ***");
        System.out.print("Ingrese Placa del Vehiculo a eliminar: ");
        String placa = sc.nextLine();

        System.out.print("¿Esta seguro que desea eliminar el vehiculo " + placa + "? (S/N): ");
        String confirmacion = sc.nextLine().trim().toUpperCase();

        if (confirmacion.equals("S")) {
            controlador.eliminarVehiculo(placa);
        } else {
            System.out.println("Eliminación cancelada.");
        }
    }

    // imprime un vehiculo
    private void imprimirVehiculo(Vehiculo v) {
        System.out.println("  Placa: " + v.getPlaca());
        System.out.println("  Marca: " + v.getMarca());
        System.out.println("  Modelo: " + v.getModelo());
        System.out.println("  Año: " + v.getAnio());
        System.out.println("  Precio/Día: " + v.getPrecioDia());
        System.out.println("  Estado: " + v.getEstado());
        System.out.println("  Tipo: " + v.getTipo());
        System.out.println("  Combustible: " + (v.getCombustible() != null ? v.getCombustible() : "N/A"));
        System.out.println("  Autonomía (Km): " + (v.getAutonomiaKm() != null ? v.getAutonomiaKm() : "N/A"));
        System.out.println("  Automática: " + (v.getAutomatica() != null ? (v.getAutomatica() ? "Sí" : "No") : "N/A"));
    }
}