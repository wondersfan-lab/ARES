DROP SCHEMA IF EXISTS ares CASCADE;
CREATE SCHEMA ares;



CREATE TABLE ares.usuario (
    id_usuario  SERIAL       PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    telefono    VARCHAR(20),
    email       VARCHAR(100) UNIQUE,
    usuario     VARCHAR(50)  NOT NULL UNIQUE,          -- nombre de login
    contrasena  VARCHAR(255) NOT NULL,                 -- académico: texto plano; en producción iría con hash
    rol         VARCHAR(20)  NOT NULL
                CHECK (rol IN ('ADMINISTRADOR', 'OPERADOR')),
    activo      BOOLEAN      NOT NULL DEFAULT TRUE,    -- se desactiva en vez de borrar (conserva el historial)
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);
 
-- ---------------------------------------------------------------------
-- 2. TRANSPORTADORA: los operadores (Mercado Libre, Envía, Coordinadora...)
-- ---------------------------------------------------------------------
CREATE TABLE ares.transportadora (
    id_transportadora  SERIAL       PRIMARY KEY,
    nombre             VARCHAR(100) NOT NULL UNIQUE,
    nit                VARCHAR(30)  UNIQUE,
    telefono_contacto  VARCHAR(20),
    direccion          VARCHAR(150),
    tipo_servicio      VARCHAR(20)  NOT NULL DEFAULT 'AMBOS'
                       CHECK (tipo_servicio IN ('ENVIO', 'RECEPCION', 'AMBOS')),
    activo             BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at         TIMESTAMPTZ  NOT NULL DEFAULT now()
);
 
-- ---------------------------------------------------------------------
-- 3. TARIFA: precio de cada transportadora por rango de peso (kg)
--    Ejemplo: de 0 a 5 kg cuesta 12.000; de 5 a 10 kg cuesta 18.000
-- ---------------------------------------------------------------------
CREATE TABLE ares.tarifa (
    id_tarifa          SERIAL        PRIMARY KEY,
    id_transportadora  INT           NOT NULL
                       REFERENCES ares.transportadora(id_transportadora) ON DELETE CASCADE,
    peso_min           NUMERIC(8,2)  NOT NULL CHECK (peso_min >= 0),
    peso_max           NUMERIC(8,2)  NOT NULL,
    valor              NUMERIC(10,2) NOT NULL CHECK (valor >= 0),
    created_at         TIMESTAMPTZ   NOT NULL DEFAULT now(),
    CONSTRAINT ck_tarifa_rango CHECK (peso_max > peso_min)
);
 
-- ---------------------------------------------------------------------
-- 4. OPERACION: cada envío o recepción que se registra en el negocio
-- ---------------------------------------------------------------------
CREATE TABLE ares.operacion (
    id_operacion       SERIAL       PRIMARY KEY,
    id_transportadora  INT          NOT NULL REFERENCES ares.transportadora(id_transportadora),
    id_usuario         INT          NOT NULL REFERENCES ares.usuario(id_usuario),
    tipo               VARCHAR(20)  NOT NULL
                       CHECK (tipo IN ('ENVIO', 'RECEPCION')),
    guia               VARCHAR(50)  NOT NULL,
    -- Fecha y hora en horario de Colombia (ver decisión 2)
    fecha              DATE         NOT NULL DEFAULT (timezone('America/Bogota', now()))::date,
    hora               TIME(0)      NOT NULL DEFAULT (timezone('America/Bogota', now()))::time(0),
    estado             VARCHAR(20)  NOT NULL DEFAULT 'REGISTRADO'
                       CHECK (estado IN ('REGISTRADO', 'ENTREGADO', 'CANCELADO')),
    remitente          VARCHAR(100) NOT NULL,
    destinatario       VARCHAR(100) NOT NULL,
    -- peso y costo solo aplican a envíos; en recepciones quedan en NULL
    peso               NUMERIC(8,2) CHECK (peso > 0),
    costo              NUMERIC(10,2) CHECK (costo >= 0),
    observaciones      TEXT,
    created_at         TIMESTAMPTZ  NOT NULL DEFAULT now(),
    -- La misma guía no se repite para el mismo operador y tipo de operación
    CONSTRAINT uq_operacion_guia UNIQUE (id_transportadora, guia, tipo)
);
 
-- ---------------------------------------------------------------------
-- 5. PAQUETE: contenido de cada operación (una operación puede tener varios)
-- ---------------------------------------------------------------------
CREATE TABLE ares.paquete (
    id_paquete       SERIAL       PRIMARY KEY,
    id_operacion     INT          NOT NULL
                     REFERENCES ares.operacion(id_operacion) ON DELETE CASCADE,
    nombre_producto  VARCHAR(100) NOT NULL,
    descripcion      TEXT,
    cantidad         INT          NOT NULL DEFAULT 1 CHECK (cantidad > 0),
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now()
);
 
-- ---------------------------------------------------------------------
-- ÍNDICES: aceleran las búsquedas más frecuentes
-- ---------------------------------------------------------------------
CREATE INDEX idx_operacion_fecha          ON ares.operacion (fecha);            -- consulta por día / Excel diario
CREATE INDEX idx_operacion_guia           ON ares.operacion (guia);             -- buscar por número de guía
CREATE INDEX idx_operacion_transportadora ON ares.operacion (id_transportadora);
CREATE INDEX idx_operacion_usuario        ON ares.operacion (id_usuario);
CREATE INDEX idx_tarifa_transportadora    ON ares.tarifa (id_transportadora);
CREATE INDEX idx_paquete_operacion        ON ares.paquete (id_operacion);
 
-- =====================================================================
-- ARES - seed.sql: datos iniciales para poder probar el sistema
-- Se ejecuta después de schema.sql. Es seguro repetirlo (ON CONFLICT).
-- =====================================================================
 
-- Usuario administrador inicial (cambiar la contraseña antes de la entrega final)
INSERT INTO ares.usuario (nombre, usuario, contrasena, rol)
VALUES ('Administrador Inicial', 'admin', 'admin123', 'ADMINISTRADOR')
ON CONFLICT (usuario) DO NOTHING;
 
-- Operadores con los que trabaja el negocio
INSERT INTO ares.transportadora (nombre, tipo_servicio) VALUES
    ('Mercado Libre', 'RECEPCION'),
    ('Envía',         'ENVIO'),
    ('Coordinadora',  'ENVIO')
ON CONFLICT (nombre) DO NOTHING;