CREATE DATABASE RentaFacilDB;
USE RentaFacilDB;

CREATE TABLE Usuario (
    idUsuario INT PRIMARY KEY AUTO_INCREMENT,
    cedula VARCHAR(20) UNIQUE NOT NULL, 
    nombre VARCHAR(50) NOT NULL,
    apellido VARCHAR(50),
    correo VARCHAR(50) UNIQUE NOT NULL,
    contrasena VARCHAR(255) NOT NULL,
    direccion VARCHAR(100),
    rol ENUM('Administrador', 'Cliente', 'Empleado') NOT NULL 
);




CREATE TABLE Cliente (
    idUsuario INT PRIMARY KEY,
    licenciaConduccion VARCHAR(30) UNIQUE, 
    telefono VARCHAR(15) NOT NULL,
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);


CREATE TABLE Empleado (
    idUsuario INT PRIMARY KEY,
    cargo VARCHAR(30) NOT NULL,
    FOREIGN KEY (idUsuario) REFERENCES Usuario(idUsuario)
);



CREATE TABLE Vehiculo (
    placa VARCHAR(10) primary KEY NOT NULL,
    marca VARCHAR(50) NOT NULL,
    modelo VARCHAR(50) NOT NULL,
    anio INT NOT NULL,
    precioDia DECIMAL(10,2) NOT NULL,
    estado ENUM('Disponible','Reservado','Mantenimiento') NOT NULL DEFAULT 'Disponible',
    tipo ENUM('Carro', 'Bicicleta', 'Monopatin electrico', 'Otros') NOT NULL, 
    combustible VARCHAR(20)  DEFAULT NULL,
    autonomiaKm INT DEFAULT NULL,
    automatica BOOLEAN DEFAULT NULL,
    imagen VARCHAR(255) DEFAULT NULL
    
);

CREATE TABLE Reserva (
    idReserva INT PRIMARY KEY AUTO_INCREMENT,
    idCliente INT NOT NULL,
    placa VARCHAR(10)  NOT NULL,
    fechaInicio datetime NOT NULL,
    fecha_entrega DATE NOT NULL,
    metodoPago ENUM('Tarjeta','Efectivo','Transferencia') NOT NULL,

    costoTotal DECIMAL(10,2) NOT NULL, 
    estado ENUM('Activa','Cancelada','Finalizada') NOT NULL DEFAULT 'Activa',
    FOREIGN KEY (idCliente) REFERENCES Cliente(idUsuario),
    FOREIGN KEY (placa) REFERENCES Vehiculo(placa)
);


CREATE TABLE Reporte (
    idReporte INT PRIMARY KEY AUTO_INCREMENT,
    idEmpleado INT NOT NULL,
    tipo ENUM('Ingresos','Reservas','Disponibilidad', 'Clientes') NOT NULL, 
    fechaGeneracion DATE NOT NULL,
    FOREIGN KEY (idEmpleado) REFERENCES Empleado(idUsuario)
);