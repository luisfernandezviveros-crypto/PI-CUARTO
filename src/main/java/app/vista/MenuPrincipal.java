//package app.vista;
//import java.util.InputMismatchException;
//import java.util.Scanner;
//public class MenuPrincipal {
//    //Esta classe llama a todas las vistas 
//    
//    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int opcion = 0 ;
//        
//        //inicializar todas las vistas 
//        VistaCliente clienteVista = new VistaCliente();
//        VistaEmpleado empleadoVista = new VistaEmpleado();
//        VistaReporte reporteVista = new VistaReporte();
//        VistaReserva reservaVista = new VistaReserva();
//        VistaVehiculo vehiculoVita = new VistaVehiculo();
//        
//        do{
//            mostrarMenu();
//            
//            try {
//                opcion = sc.nextInt();
//                
//                switch (opcion) {
//                    case 1:
//                        clienteVista.menuCliente();
//                        break;
//                    case 2:
//                        vehiculoVita.menuVehiculo();
//                        break;
//                    case 3:
//                        empleadoVista.menuEmpleado();
//                        break;
//                    case 4:
//                        reservaVista.menuReserva();
//                        break;
//                    case 5:
//                        reporteVista.menuReporte();
//                        break;
//                    case 6:
//                        System.out.println("Saliendo del sistema....");
//                        break;
//                    default:
//                        System.out.println("Opcion invalida. Intente de nuevo");
//                }
//            } catch (InputMismatchException e) {
//                System.out.println("Entrada invalida. Ingresa un numero.");
//            } catch(Exception e){
//                System.out.println("Error del sistema: " + e.getMessage());
//            }
//        }while(opcion !=6);
//        
//        sc.close();
//    }
//
//    private static void mostrarMenu() {
//        System.out.println("\n--- Menu Principal RentaFacil ---");
//        System.out.println("");
//        System.out.println("1. Gestion de Clientes");
//        System.out.println("2. Gestion de Vehiculos");
//        System.out.println("3. Gestion de Empleados");
//        System.out.println("4. Gestion de Reservas");
//        System.out.println("5. Gestion de Reportes");
//        System.out.println("6. Salir");
//        System.out.print("Seleccione una opcion: ");
//        
//    }
//    
//}
