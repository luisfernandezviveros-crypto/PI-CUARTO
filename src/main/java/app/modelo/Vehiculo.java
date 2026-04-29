package app.modelo;




public class Vehiculo {
    // atributos 
    private String placa;
    private String marca;
    private String modelo;
    private int anio;
    private double precioDia;
    private String estado; // Corresponde a ENUM('Disponible','Reservado','Mantenimiento')
    private String tipo; // Corresponde a ENUM('Carro', 'Bicicleta', 'Otro')
    private String combustible; // NULL si es Bicicleta
    private Integer autonomiaKm; // Usamos Integer (objeto) para permitir el valor NULL
    private Boolean automatica;
    private String imagen;


    
    // construtores
    public Vehiculo(){}
    public Vehiculo(String text, String text1, String text2, double parseDouble, String text3, String text4, String rutaImagen){}

    public Vehiculo(String placa, String marca, String modelo, int anio, double precioDia, String estado, String tipo, String combustible, Integer autonomiaKm, Boolean automatica, String imagen) {
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.precioDia = precioDia;
        this.estado = estado;
        this.tipo = tipo;
        this.combustible = combustible;
        this.autonomiaKm = autonomiaKm;
        this.automatica = automatica;
        this.imagen = imagen;
    }
    
    // setters y guetters
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public double getPrecioDia() {
        return precioDia;
    }

    public void setPrecioDia(double precioDia) {
        this.precioDia = precioDia;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }


    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getCombustible() {
        return combustible;
    }

    public void setCombustible(String combustible) {
        this.combustible = combustible;
    }

    public Integer getAutonomiaKm() {
        return autonomiaKm;
    }

    public void setAutonomiaKm(Integer autonomiaKm) {
        this.autonomiaKm = autonomiaKm;
    }

    public Boolean getAutomatica() {
        return automatica;
    }

    public void setAutomatica(Boolean automatica) {
        this.automatica = automatica;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
    
  
    

  
   
    
    
}