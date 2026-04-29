package app.vista;

import app.controlador.ClienteControlador;
import app.modelo.Cliente;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class VistaCliente {

    // La vista solo conoce al controlador.
    private final ClienteControlador controlador;
    private final Scanner sc;

    public VistaCliente() {
        this.controlador = new ClienteControlador();
        this.sc = new Scanner(System.in);
    }

    // --- Menú de Clientes ---
//    public void menuCliente() {
//        int opcion;
//        do {
//            System.out.println("\n*** Gestion de Clientes ***");
//            System.out.println("1. Registrar Nuevo Cliente");
//            System.out.println("2. Ver Cliente por ID");
//            System.out.println("3. Listar Todos los Clientes");
//            System.out.println("4. Actualizar Cliente");
//            System.out.println("5. Eliminar Cliente");
//            System.out.println("0. Volver al Menu Principal");
//            System.out.print("Seleccione una opcion: ");
//            
//            try {
//                opcion = sc.nextInt();
//                switch (opcion) {
//                    case 1: crearCliente(); break;
//                    case 2: verClientePorId(); break;
//                    case 3: listarClientes(); break;
//                    case 4: actualizarCliente(); break;
//                    case 5: eliminarCliente(); break;
//                    case 0: System.out.println("Volviendo..."); break;
//                    default: System.out.println("Opción no válida.");
//                }
//            } catch (InputMismatchException e) {
//                System.err.println("Entrada inválida. Por favor, ingrese un número.");
//                sc.nextLine(); 
//                opcion = -1;
//            }
//        } while (opcion != 0);
//    }

    //metodos para el crud
    
    private void crearCliente() {
        System.out.println("\n--- Registro de Nuevo Cliente ---");
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
            
            System.out.print("Licencia de Conduccion: ");
            String licenciaConduccion = sc.nextLine();
            System.out.print("Telefono: ");
            String telefono = sc.nextLine();

            Cliente nuevoCliente = new Cliente(licenciaConduccion, telefono, idUsuario, cedula, nombre, apellido, correo, contrasena, direccion, correo);
            
            // Llama al controlador para ejecutar la lógica de negocio y la transacción
            controlador.registrarCliente(nuevoCliente); 

        } catch (InputMismatchException e) {
            System.err.println("Error de formato de entrada asegúrese de ingresar números donde corresponde.");
            sc.nextLine(); 
        } catch (Exception e) {
            System.err.println("Error inesperado al registrar: " + e.getMessage());
        }
    }

    private void verClientePorId() {
        System.out.println("\n*** Ver Cliente por ID ***");
        try {
            System.out.print("Ingrese ID del Cliente a buscar: ");
            int idUsuario = sc.nextInt();
            sc.nextLine();
            
            Cliente cliente = controlador.obtenerClientePorId(idUsuario);
            
            if (cliente != null) {
                System.out.println("\nCliente Encontrado:");
                imprimirCliente(cliente);
            } else {
                System.out.println("Cliente con ID " + idUsuario + " no encontrado.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Entrada inválida. Ingrese un número para el ID.");
            sc.nextLine();
        }
    }

//    private void listarClientes() {
//        System.out.println("\n--- Listado de Clientes ---");
//       // List<Cliente> clientes = controlador.listarClientes();
//        
//        if (clientes.isEmpty()) {
//            System.out.println("No hay clientes registrados.");
//            return;
//        }
//        
//        System.out.printf("| %-4s | %-10s | %-20s | %-15s | %-12s |\n", "ID", "Cédula", "Nombre Completo", "Licencia", "Teléfono");
//        System.out.println("");
//        for (Cliente c : clientes) {
//            System.out.printf("| %-4d | %-10s | %-20s | %-15s | %-12s |\n", 
//                c.getIdUsuario(), 
//                c.getCedula(), 
//                c.getNombre() + " " + c.getApellido(),
//                c.getLicenciaConduccion(),
//                c.getTelefono());
//        }
//    }
    
    // Aquí irían los métodos actualizarCliente() y eliminarCliente() que usan lógica similar
    private void actualizarCliente() {
        System.out.println("\n*** Actualizar Cliente ***");
        try {
            System.out.print("Ingrese ID del Cliente a actualizar: ");
            int idUsuario = sc.nextInt();
            sc.nextLine();

            Cliente clienteActual = controlador.obtenerClientePorId(idUsuario);

            //validacion si no existe el id
            if (clienteActual == null) {
                System.out.println("Cliente con ID " + idUsuario + " no encontrado.");
                return;
            }

            System.out.println("\nDatos actuales del cliente:");
            imprimirCliente(clienteActual);

            // Se solicitan los nuevos datos. Solo se actualiza si la entrada no está vacía.
            System.out.printf("Cédula actual [%s]: ", clienteActual.getCedula());
            String cedula = sc.nextLine().trim();
            if (!cedula.isEmpty()) clienteActual.setCedula(cedula);

            System.out.printf("Nombre actual [%s]: ", clienteActual.getNombre());
            String nombre = sc.nextLine().trim();
            if (!nombre.isEmpty()) clienteActual.setNombre(nombre);
            
            //continuar pidiendo y actualizando los demas campos de manera similar al DAO original)
            
            System.out.printf("Teléfono actual [%s]: ", clienteActual.getTelefono());
            String telefono = sc.nextLine().trim();
            if (!telefono.isEmpty()) clienteActual.setTelefono(telefono);

            // Llama al controlador para ejecutar la actualización
            controlador.actualizarCliente(clienteActual);
            
        } catch (InputMismatchException e) {
            System.err.println("Entrada inválida. Ingrese un número para el ID.");
            sc.nextLine();
        }
    }
    //eleiminar clientes 
    private void eliminarCliente() {
        System.out.println("\n*** Eliminar Cliente ***");
        try {
            System.out.print("Ingrese ID del Cliente a eliminar: ");
            int idUsuario = sc.nextInt();
            sc.nextLine();

            System.out.print("️¿Esta seguro que desea eliminar al cliente " + idUsuario + "? (S/N): ");
            String confirmacion = sc.nextLine().trim().toUpperCase();

            if (confirmacion.equals("S")) {
                controlador.eliminarCliente(idUsuario);
            } else {
                System.out.println("Eliminación cancelada.");
            }
        } catch (InputMismatchException e) {
            System.err.println("Entrada inválida. Ingrese un número para el ID.");
            sc.nextLine();
        }
    }
    
    // mostrar clientes
    private void imprimirCliente(Cliente c) {
        System.out.println("  ID: " + c.getIdUsuario());
        System.out.println("  Cédula: " + c.getCedula());
        System.out.println("  Nombre: " + c.getNombre() + " " + c.getApellido());
        System.out.println("  Correo: " + c.getCorreo());
        System.out.println("  Dirección: " + c.getDireccion());
        System.out.println("  Licencia: " + c.getLicenciaConduccion());
        System.out.println("  Teléfono: " + c.getTelefono());
    }
}
