DROP SCHEMA IF EXISTS ares CASCADE;
CREATE SCHEMA IF NOT EXISTS ares;
CREATE TABLE ares.usuario (
    id_usuario  SERIAL       PRIMARY KEY,
    nombre      VARCHAR(100) NOT NULL,
    telefono    VARCHAR(20),
    email       VARCHAR(100) UNIQUE,
    usuario     VARCHAR(50)  NOT NULL UNIQUE,
    contrasena  VARCHAR(255) NOT NULL,
    rol         VARCHAR(20)  NOT NULL
                CHECK (rol IN ('ADMINISTRADOR', 'OPERADOR')),
    activo      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT now()
);
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
CREATE TABLE ares.operacion (
    id_operacion       SERIAL       PRIMARY KEY,
    id_transportadora  INT          NOT NULL REFERENCES ares.transportadora(id_transportadora),
    id_usuario         INT          NOT NULL REFERENCES ares.usuario(id_usuario),
    tipo               VARCHAR(20)  NOT NULL
                       CHECK (tipo IN ('ENVIO', 'RECEPCION')),
    guia               VARCHAR(50)  NOT NULL,
    fecha              DATE         NOT NULL DEFAULT (timezone('America/Bogota', now()))::date,
    hora               TIME(0)      NOT NULL DEFAULT (timezone('America/Bogota', now()))::time(0),
    estado             VARCHAR(20)  NOT NULL DEFAULT 'REGISTRADO'
                       CHECK (estado IN ('REGISTRADO', 'ENTREGADO', 'CANCELADO')),
    remitente          VARCHAR(100) NOT NULL,
    destinatario       VARCHAR(100) NOT NULL,
    peso               NUMERIC(8,2) CHECK (peso > 0),
    costo              NUMERIC(10,2) CHECK (costo >= 0),
    observaciones      TEXT,
    created_at         TIMESTAMPTZ  NOT NULL DEFAULT now(),
    CONSTRAINT uq_operacion_guia UNIQUE (id_transportadora, guia, tipo)
);
CREATE TABLE ares.paquete (
    id_paquete       SERIAL       PRIMARY KEY,
    id_operacion     INT          NOT NULL
                     REFERENCES ares.operacion(id_operacion) ON DELETE CASCADE,
    nombre_producto  VARCHAR(100) NOT NULL,
    descripcion      TEXT,
    cantidad         INT          NOT NULL DEFAULT 1 CHECK (cantidad > 0),
    created_at       TIMESTAMPTZ  NOT NULL DEFAULT now()
);
CREATE INDEX idx_operacion_fecha          ON ares.operacion (fecha);
CREATE INDEX idx_operacion_guia           ON ares.operacion (guia);
CREATE INDEX idx_operacion_transportadora ON ares.operacion (id_transportadora);
CREATE INDEX idx_operacion_usuario        ON ares.operacion (id_usuario);
CREATE INDEX idx_tarifa_transportadora    ON ares.tarifa (id_transportadora);
CREATE INDEX idx_paquete_operacion        ON ares.paquete (id_operacion);
INSERT INTO ares.usuario (nombre, usuario, contrasena, rol)
VALUES ('Administrador Inicial', 'admin', 'admin123', 'ADMINISTRADOR')
ON CONFLICT (usuario) DO NOTHING;
INSERT INTO ares.transportadora (nombre, tipo_servicio) VALUES
    ('Mercado Libre', 'RECEPCION'),
    ('Envía',         'ENVIO'),
    ('Coordinadora',  'ENVIO')
ON CONFLICT (nombre) DO NOTHING;
DELETE FROM ares.operacion WHERE guia LIKE 'DEMO-%';
INSERT INTO ares.usuario (nombre, telefono, email, usuario, contrasena, rol)
VALUES ('Operador Uno', '3001234567', 'operador1@ares.com', 'operador1', 'operador123', 'OPERADOR')
ON CONFLICT (usuario) DO NOTHING;
INSERT INTO ares.tarifa (id_transportadora, peso_min, peso_max, valor)
SELECT t.id_transportadora, v.peso_min, v.peso_max, v.valor
FROM ares.transportadora t
CROSS JOIN (VALUES (0, 1, 8000), (1, 5, 12000), (5, 10, 18000), (10, 20, 26000))
           AS v(peso_min, peso_max, valor)
WHERE t.nombre IN ('Envía', 'Coordinadora')
  AND NOT EXISTS (SELECT 1 FROM ares.tarifa x WHERE x.id_transportadora = t.id_transportadora);
INSERT INTO ares.operacion
    (id_transportadora, id_usuario, tipo, guia, fecha, hora, estado, remitente, destinatario, peso, costo, observaciones)
SELECT t.id_transportadora, u.id_usuario, v.tipo, v.guia,
       ((now() AT TIME ZONE 'America/Bogota')::date - v.dias),
       v.hora::time, v.estado, v.remitente, v.destinatario, v.peso, v.costo, v.obs
FROM (VALUES
    ('Envía',          'admin',     'ENVIO',     'DEMO-ENV-001', 0, '08:15', 'REGISTRADO', 'Tienda La 14',   'Carlos Pérez',   0.8,  8000,  NULL),
    ('Coordinadora',   'operador1', 'ENVIO',     'DEMO-ENV-002', 0, '09:40', 'REGISTRADO', 'Ana Gómez',      'Luis Martínez',  3.2,  12000, 'Frágil'),
    ('Envía',          'operador1', 'ENVIO',     'DEMO-ENV-003', 0, '11:05', 'ENTREGADO',  'Boutique Sol',   'María Rojas',    7.0,  18000, NULL),
    ('Mercado Libre',  'admin',     'RECEPCION', 'DEMO-REC-001', 0, '10:20', 'REGISTRADO', 'Mercado Libre', 'Juan Torres',    NULL, NULL,  NULL),
    ('Mercado Libre',  'operador1', 'RECEPCION', 'DEMO-REC-002', 0, '14:30', 'ENTREGADO',  'Mercado Libre', 'Sofía Díaz',     NULL, NULL,  'Entregado en portería'),
    ('Coordinadora',   'admin',     'ENVIO',     'DEMO-ENV-004', 1, '08:50', 'ENTREGADO',  'Papelería Cali', 'Pedro Ruiz',    12.0, 26000, NULL),
    ('Envía',          'operador1', 'ENVIO',     'DEMO-ENV-005', 1, '15:10', 'CANCELADO',  'Carlos Pérez',   'Laura Vega',    4.5,  12000, 'Cliente canceló'),
    ('Mercado Libre',  'admin',     'RECEPCION', 'DEMO-REC-003', 1, '16:45', 'ENTREGADO',  'Mercado Libre', 'Andrés López',   NULL, NULL,  NULL),
    ('Coordinadora',   'operador1', 'ENVIO',     'DEMO-ENV-006', 2, '10:00', 'ENTREGADO',  'Tienda La 14',   'Camila Soto',   0.5,  8000,  NULL),
    ('Mercado Libre',  'admin',     'RECEPCION', 'DEMO-REC-004', 2, '13:25', 'ENTREGADO',  'Mercado Libre', 'Diego Herrera',  NULL, NULL,  NULL)
) AS v(transportadora, usuario, tipo, guia, dias, hora, estado, remitente, destinatario, peso, costo, obs)
JOIN ares.transportadora t ON t.nombre = v.transportadora
JOIN ares.usuario u        ON u.usuario = v.usuario;
INSERT INTO ares.paquete (id_operacion, nombre_producto, descripcion, cantidad)
SELECT o.id_operacion, v.producto, v.descripcion, v.cantidad
FROM (VALUES
    ('DEMO-ENV-001', 'Camiseta deportiva', 'Talla M, color azul', 2),
    ('DEMO-ENV-002', 'Juego de vasos',     'Vidrio, caja de 6',   1),
    ('DEMO-ENV-003', 'Chaqueta',           'Talla L',             1),
    ('DEMO-ENV-003', 'Bufanda',            'Lana',                2),
    ('DEMO-REC-001', 'Audífonos',          'Bluetooth',           1),
    ('DEMO-REC-002', 'Libro',              'Programación en Java',1)
) AS v(guia, producto, descripcion, cantidad)
JOIN ares.operacion o ON o.guia = v.guia;
SELECT fecha, hora, tipo, guia, estado, remitente, destinatario
FROM ares.operacion
ORDER BY fecha DESC, hora DESC;