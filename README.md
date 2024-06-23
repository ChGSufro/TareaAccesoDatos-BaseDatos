Script:

CREATE DATABASE CanesFelinos;

-- Tabla para los animales
CREATE TABLE Animal (
    rut_mascota VARCHAR(15) PRIMARY KEY,
    nombre VARCHAR(100),
    edad INTEGER,
    raza VARCHAR(40),
    especie VARCHAR(40),
    sexo VARCHAR(20),
    estado VARCHAR(20)
);

-- Tabla para los adoptantes
CREATE TABLE Adoptante (
    rut_usuario VARCHAR(15) PRIMARY KEY,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    sexo VARCHAR(20),
    direccion VARCHAR(100),
    telefono VARCHAR(20),
    correo VARCHAR(45)
);

-- Tabla para los empleados
CREATE TABLE Empleados (
    rut_empleado VARCHAR(15) PRIMARY KEY,
    nombres VARCHAR(100),
    apellidos VARCHAR(100),
    sexo VARCHAR(20),
    cargo VARCHAR(40),
    direccion VARCHAR(100),
    telefono VARCHAR(20),
    correo VARCHAR(45)
);

-- Tabla para las adopciones con referencias a las otras tablas
CREATE TABLE Adopcion (
    id_adopcion SERIAL PRIMARY KEY,
    rut_mascota VARCHAR(15) REFERENCES Animal(rut_mascota),
    rut_usuario VARCHAR(15) REFERENCES Adoptante(rut_usuario),
    rut_empleado VARCHAR(15) REFERENCES Empleados(rut_empleado),
    fecha_adopcion DATE,
    hora_adopcion TIME,
    estado_adopcion VARCHAR(20)
);

INSERT INTO Animal (rut_mascota, nombre, edad, raza, especie, sexo, estado)
VALUES
    ('12345', 'Firulais', 3, 'Labrador', 'Perro', 'Macho', 'Sano'),
    ('67890', 'Mittens', 2, 'Siamés', 'Gato', 'Hembra', 'Sano'),
    ('54321', 'Rex', 4, 'Pastor Alemán', 'Perro', 'Macho', 'Sano'),
    ('98765', 'Whiskers', 1, 'Persa', 'Gato', 'Macho', 'Sano'),
    ('13579', 'Luna', 5, 'Golden Retriever', 'Perro', 'Hembra', 'Sano');
INSERT INTO Empleados (rut_empleado, nombres, apellidos, sexo, cargo, direccion, telefono, correo)
VALUES
    ('11111', 'Christian', NULL, 'Masculino', 'Administrador', 'Calle Principal 123', '987654321', 'c.gajardo08@ufromail.cl'),
    ('22222', 'Alex', NULL, 'Masculino', 'Administrador', 'Avenida Central 456', '923456789', 'a.saez09@ufromail.cl');
