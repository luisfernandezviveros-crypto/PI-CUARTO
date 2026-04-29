package app.modelo;

public class Cliente extends Usuario{
    // atributos 
    private String licenciaConduccion;
    private String telefono;
    
    // construtores
    public Cliente(){}

    public Cliente(String licenciaConduccion, String telefono, int idUsuario, String cedula, String nombre, String apellido, String correo, String contrasena, String direccion, String rol) {
        super(idUsuario, cedula, nombre, apellido, correo, contrasena, direccion, rol);
        this.licenciaConduccion = licenciaConduccion;
        this.telefono = telefono;
    }
    
    //setters y getters

    public String getLicenciaConduccion() {
        return licenciaConduccion;
    }

    public void setLicenciaConduccion(String licenciaConduccion) {
        this.licenciaConduccion = licenciaConduccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    
}
