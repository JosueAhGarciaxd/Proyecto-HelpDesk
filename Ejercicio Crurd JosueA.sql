--drop table tickets;

CREATE TABLE usuarios (
    usuario_id NUMBER PRIMARY KEY,
    nombre VARCHAR2(50) NOT NULL,
    email VARCHAR2(50) NOT NULL UNIQUE,
    contrasena VARCHAR2(100) NOT NULL
);

CREATE TABLE tickets (
    ticket_number NUMBER PRIMARY KEY,
    titulo VARCHAR2(100) NOT NULL,
    descripcion VARCHAR2(500) NOT NULL,
    usuario_id NUMBER NOT NULL,
    fecha_creacion DATE NOT NULL,
    estado VARCHAR2(20) NOT NULL CHECK (estado IN ('Activo', 'Finalizado')),
    fecha_finalizacion DATE,
    FOREIGN KEY (usuario_id) REFERENCES usuarios(usuario_id)
);

