CREATE SCHEMA IF NOT EXISTS ares;

-- 1. Tabla: USUARIO
CREATE TABLE IF NOT EXISTS ares.usuario (
    id_usuario SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    usuario VARCHAR(50) NOT NULL UNIQUE,
    contrasena VARCHAR(255) NOT NULL,
    rol VARCHAR(20) NOT NULL CHECK (rol IN ('Administrador', 'Operador')),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 2. Tabla: TRANSPORTADORA
CREATE TABLE IF NOT EXISTS ares.transportadora (
    id_transportadora SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    telefono_contacto VARCHAR(20),
    direccion VARCHAR(150),
    tipo_servicio VARCHAR(50),
    nit VARCHAR(30) UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 3. Tabla: TARIFA
CREATE TABLE IF NOT EXISTS ares.tarifa (
    id_tarifa SERIAL PRIMARY KEY,
    id_transportadora INT NOT NULL REFERENCES ares.transportadora(id_transportadora) ON DELETE CASCADE,
    peso_min NUMERIC(8,2) NOT NULL CHECK (peso_min >= 0),
    peso_max NUMERIC(8,2) NOT NULL CHECK (peso_max > peso_min),
    valor NUMERIC(10,2) NOT NULL CHECK (valor >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 4. Tabla: ENVIO
CREATE TABLE IF NOT EXISTS ares.envio (
    id_envio SERIAL PRIMARY KEY,
    id_transportadora INT NOT NULL REFERENCES ares.transportadora(id_transportadora),
    id_usuario INT NOT NULL REFERENCES ares.usuario(id_usuario),
    guia VARCHAR(50) NOT NULL UNIQUE,
    fecha VARCHAR(30) NOT NULL,
    estado VARCHAR(30) NOT NULL DEFAULT 'En Espera',
    remitente VARCHAR(100) NOT NULL,
    destinatario VARCHAR(100) NOT NULL,
    pais_origen VARCHAR(50) NOT NULL,
    pais_destino VARCHAR(50) NOT NULL,
    peso NUMERIC(8,2) NOT NULL CHECK (peso > 0),
    costo NUMERIC(10,2) NOT NULL CHECK (costo >= 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 5. Tabla: PAQUETE
CREATE TABLE IF NOT EXISTS ares.paquete (
    id_paquete SERIAL PRIMARY KEY,
    id_envio INT NOT NULL REFERENCES ares.envio(id_envio) ON DELETE CASCADE,
    nombre_producto VARCHAR(100) NOT NULL,
    descripcion TEXT,
    cantidad INT NOT NULL CHECK (cantidad > 0),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 6. Tabla: RECEPCION
CREATE TABLE IF NOT EXISTS ares.recepcion (
    id_recepcion SERIAL PRIMARY KEY,
    id_transportadora INT NOT NULL REFERENCES ares.transportadora(id_transportadora),
    id_usuario INT NOT NULL REFERENCES ares.usuario(id_usuario),
    guia VARCHAR(50) NOT NULL UNIQUE,
    fecha_recepcion VARCHAR(30) NOT NULL,
    fecha_entrega VARCHAR(30),
    estado VARCHAR(30) NOT NULL DEFAULT 'Recibido',
    destinatario VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Inserción inicial de prueba
INSERT INTO ares.usuario (nombre, usuario, contrasena, rol)
VALUES ('Administrador Inicial', 'admin', 'admin123', 'Administrador')
ON CONFLICT (usuario) DO NOTHING;