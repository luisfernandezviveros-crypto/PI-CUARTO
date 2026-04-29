package app.vista;

import app.controlador.EmpleadoControlador;
import app.modelo.Empleado;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class VistaEmpleado {

    private final EmpleadoControlador controlador;
    private final Scanner sc;

    public VistaEmpleado() {
        this.controlador = new EmpleadoControlador();
        this.sc = new Scanner(System.in);
    }

    public void menuEmpleado() {
        int opcion;
        do {
            System.out.println("\n*** Gestion de Empleados ***");
            System.out.println("1. Registrar Nuevo Empleado");
            System.out.println("2. Ver Empleado por ID");
            System.out.println("3. Listar Todos los Empleados");
            System.out.println("4. Actualizar Empleado");
            System.out.println("5. Eliminar Empleado");
            System.out.println("0. Volver al MenuPrincipal");
            System.out.print("Seleccione una opcion: ");
            
            try {
                opcion = sc.nextInt();
                sc.nextLine(); 

                switch (opcion) {
                    case 1: crearEmpleado(); break;
                    case 2: verEmpleadoPorId(); break;
                    case 3: listarEmpleados(); break;
                    case 4: actualizarEmpleado(); break;
                    case 5: eliminarEmpleado(); break;
                    case 0: System.out.println("Volviendo..."); break;
                    default: System.out.println("Opción no valida.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Entrada invalida. Por favor, ingrese un numero.");
                sc.nextLine(); 
                opcion = -1;
            }
        } while (opcion != 0);
    }
    
    private void crearEmpleado() {
        System.out.println("\n*** Registro de Nuevo Empleado ***");
        try {
            System.out.print("Ingrese ID de Usuario (debe ser un entero): ");
            int idUsuario = sc.nextInt();
            sc.nextLine(); 
            
            System.out.print("Cedula: ");
            String cedula = sc.nextLine();
            System.out.print("Nombre: ");
            String nombre = sc.nextLine();
            System.out.print("Apellido: ");
            String apellido = sc.nextLine();
            System.out.print("Correo: ");
            String correo = sc.nextLine();
            System.out.print("Contraseña: ");
            String contrasena = sc.nextLine();
            System.out.print("Direccion: ");
            String direccion = sc.nextLine();
            
            System.out.print("Cargo del Empleado: ");
            String cargo = sc.nextLine();

            // El rol siempre sera "Empleado"
            Empleado nuevoEmpleado = new Empleado(cargo, idUsuario, cedula, nombre, apellido, correo, contrasena, direccion, cargo);
            
            controlador.registrarEmpleado(nuevoEmpleado); 

        } catch (InputMismatchException e) {
            System.err.println("Error de formato de entrada. Asegurese de ingresar numeros donde corresponde.");
            sc.nextLine(); 
        }
    }
    
    private void verEmpleadoPorId() {
        // Lógica similar a ClienteView
        System.out.println("\n*** Ver Empleado por ID ***");
        try {
            System.out.print("Ingrese ID del Empleado a buscar: ");
            int idUsuario = sc.nextInt();
            sc.nextLine();
            
            Empleado empleado = controlador.obtenerEmpleadoPorId(idUsuario);
            
            if (empleado != null) {
                System.out.println("\n Empleado Encontrado:");
                imprimirEmpleado(empleado);
            } else {
                System.out.println("Empleado con ID " + idUsuario + " no encontrado.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Entrada invalida. Ingrese un numero para el ID.");
            sc.nextLine();
        }
    }
    
    private void listarEmpleados() {
        System.out.println("\n*** Listado de Empleados ***");
        List<Empleado> empleados = controlador.listarEmpleados();
        
        if (empleados.isEmpty()) {
            System.out.println("No hay empleados registrados.");
            return;
        }
        
        System.out.printf("| %-4s | %-10s | %-20s | %-15s |\n", "ID", "Cedula", "Nombre Completo", "Cargo");
        System.out.println();
        for (Empleado e : empleados) {
            System.out.printf("| %-4d | %-10s | %-20s | %-15s |\n", 
                e.getIdUsuario(), 
                e.getCedula(), 
                e.getNombre() + " " + e.getApellido(),
                e.getCargo());
        }
    }

    private void actualizarEmpleado() {
        System.out.println("\n*** Actualizar Empleado ***");
        try {
            System.out.print("Ingrese ID del Empleado a actualizar: ");
            int idUsuario = sc.nextInt();
            sc.nextLine();

            Empleado empleadoActual = controlador.obtenerEmpleadoPorId(idUsuario);

            if (empleadoActual == null) {
                System.out.println("Empleado con ID " + idUsuario + " no encontrado.");
                return;
            }

            System.out.println("\nDatos actuales del empleado:");
            imprimirEmpleado(empleadoActual);
            
            // Pedir y actualizar campos de Usuario (cedula, nombre, etc.)
            System.out.printf("Nuevo Cargo [%s]: ", empleadoActual.getCargo());
            String cargo = sc.nextLine().trim();
            if (!cargo.isEmpty()) empleadoActual.setCargo(cargo);

            controlador.actualizarEmpleado(empleadoActual);
            
        } catch (InputMismatchException e) {
            System.err.println("Entrada invalida. Ingrese un numero para el ID.");
            sc.nextLine();
        }
    }
    
    private void eliminarEmpleado() {
        System.out.println("\n*** Eliminar Empleado ***");
        try {
            System.out.print("Ingrese ID del Empleado a eliminar: ");
            int idUsuario = sc.nextInt();
            sc.nextLine();

            System.out.print("¿Esta seguro que desea eliminar al empleado " + idUsuario + "? (S/N): ");
            String confirmacion = sc.nextLine().trim().toUpperCase();

            if (confirmacion.equals("S")) {
                controlador.eliminarEmpleado(idUsuario);
            } else {
                System.out.println("Eliminacinn cancelada.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Entrada inválida. Ingrese un número para el ID.");
            sc.nextLine();
        }
    }
    
    private void imprimirEmpleado(Empleado e) {
        System.out.println("  ID: " + e.getIdUsuario());
        System.out.println("  Cédula: " + e.getCedula());
        System.out.println("  Nombre: " + e.getNombre() + " " + e.getApellido());
        System.out.println("  Correo: " + e.getCorreo());
        System.out.println("  Dirección: " + e.getDireccion());
        System.out.println("  Cargo: " + e.getCargo());
    }
}