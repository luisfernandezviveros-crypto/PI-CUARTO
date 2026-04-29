/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package app.modelo;


public class Empleado extends Usuario{
    // atributos 
    private String cargo;
    
    // construtores 
    public Empleado(){}
    
    public Empleado(int idUsuario, String cedula, String nombre, String apellido, String cargo) {
    super(idUsuario, cedula, nombre, apellido, null, null, null, null); // campos opcionales del padre
    this.cargo = cargo;
}


    public Empleado(String cargo, int idUsuario, String cedula, String nombre, String apellido, String correo, String contrasena, String direccion, String rol) {
        super(idUsuario, cedula, nombre, apellido, correo, contrasena, direccion, rol);
        this.cargo = cargo;
    }

    // getters y setters
    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    
    
}