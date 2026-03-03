CREATE TABLE usuarios (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    rol_id INTEGER NOT NULL,
    almacen_id INTEGER, -- Almacén por defecto (nullable)
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
	);
	
CREATE TABLE almacenes (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    ubicacion VARCHAR(200),
    responsable VARCHAR(100),
    es_central BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE categorias (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    descripcion TEXT,
    activo BOOLEAN DEFAULT TRUE
);
CREATE TABLE productos (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(50) UNIQUE NOT NULL,
    nombre VARCHAR(200) NOT NULL,
    categoria_id INTEGER REFERENCES categorias(id),
    descripcion TEXT,
    unidad_medida VARCHAR(20),
    precio_costo DECIMAL(10,2) NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL,
    stock_minimo INTEGER DEFAULT 5,
    imagen VARCHAR(255),
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE stock_almacen (
    id SERIAL PRIMARY KEY,
    producto_id INTEGER REFERENCES productos(id),
    almacen_id INTEGER REFERENCES almacenes(id),
    cantidad INTEGER DEFAULT 0,
    fecha_actualizacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(producto_id, almacen_id) -- Un producto solo una vez por almacén
);

CREATE TABLE proveedores (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    empresa VARCHAR(100),
    telefono VARCHAR(20),
    direccion TEXT,
    email VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE entradas_producto (
    id SERIAL PRIMARY KEY,
    numero_entrada VARCHAR(50) UNIQUE NOT NULL,
    fecha_entrada TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    proveedor_id INTEGER REFERENCES proveedores(id),
    numero_factura VARCHAR(50),
    almacen_id INTEGER REFERENCES almacenes(id),
    responsable_id INTEGER REFERENCES usuarios(id),
    observaciones TEXT
);
CREATE TABLE detalle_entrada (
    id SERIAL PRIMARY KEY,
    entrada_id INTEGER REFERENCES entradas_producto(id) ON DELETE CASCADE,
    producto_id INTEGER REFERENCES productos(id),
    cantidad INTEGER NOT NULL,
    precio_compra DECIMAL(10,2) NOT NULL,
    lote VARCHAR(50),
    subtotal DECIMAL(10,2) GENERATED ALWAYS AS (cantidad * precio_compra) STORED
);

CREATE TABLE turnos (
    id SERIAL PRIMARY KEY,
    usuario_id INTEGER REFERENCES usuarios(id),
    almacen_id INTEGER REFERENCES almacenes(id),
    fecha_inicio TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_fin TIMESTAMP,
    efectivo_inicial DECIMAL(10,2) DEFAULT 0,
    efectivo_final DECIMAL(10,2),
    ventas_efectivo DECIMAL(10,2) DEFAULT 0,
    ventas_transferencia DECIMAL(10,2) DEFAULT 0,
    total_ventas DECIMAL(10,2) DEFAULT 0,
    estado VARCHAR(20) DEFAULT 'ACTIVO' -- ACTIVO, CERRADO
);

CREATE TABLE clientes (
    id SERIAL PRIMARY KEY,
    codigo VARCHAR(20) UNIQUE NOT NULL,
    nombre VARCHAR(100) NOT NULL,
    documento VARCHAR(30) UNIQUE,
    telefono VARCHAR(20),
    direccion TEXT,
    email VARCHAR(100),
    activo BOOLEAN DEFAULT TRUE
);

CREATE TABLE ventas (
    id SERIAL PRIMARY KEY,
    numero_venta VARCHAR(50) UNIQUE NOT NULL,
    fecha_venta TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    vendedor_id INTEGER REFERENCES usuarios(id),
    almacen_id INTEGER REFERENCES almacenes(id),
    cliente_id INTEGER REFERENCES clientes(id),
    subtotal DECIMAL(10,2) NOT NULL,
    descuento DECIMAL(10,2) DEFAULT 0,
    total DECIMAL(10,2) NOT NULL,
    efectivo DECIMAL(10,2) DEFAULT 0,
    transferencia DECIMAL(10,2) DEFAULT 0,
    estado VARCHAR(20) DEFAULT 'COMPLETADA', -- COMPLETADA, ANULADA
    turno_id INTEGER REFERENCES turnos(id),
    observaciones TEXT
);

CREATE TABLE detalle_venta (
    id SERIAL PRIMARY KEY,
    venta_id INTEGER REFERENCES ventas(id) ON DELETE CASCADE,
    producto_id INTEGER REFERENCES productos(id),
    cantidad INTEGER NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    descuento_item DECIMAL(10,2) DEFAULT 0,
    subtotal DECIMAL(10,2) GENERATED ALWAYS AS ((cantidad * precio_unitario) - descuento_item) STORED
);

CREATE TABLE transferencias (
    id SERIAL PRIMARY KEY,
    numero_transferencia VARCHAR(50) UNIQUE NOT NULL,
    almacen_origen_id INTEGER REFERENCES almacenes(id),
    almacen_destino_id INTEGER REFERENCES almacenes(id),
    fecha_solicitud TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_aprobacion TIMESTAMP,
    solicitante_id INTEGER REFERENCES usuarios(id),
    aprobador_id INTEGER REFERENCES usuarios(id),
    estado VARCHAR(20) DEFAULT 'PENDIENTE', -- PENDIENTE, APROBADA, RECHAZADA, COMPLETADA
    observaciones TEXT
);
CREATE TABLE detalle_transferencia (
    id SERIAL PRIMARY KEY,
    transferencia_id INTEGER REFERENCES transferencias(id) ON DELETE CASCADE,
    producto_id INTEGER REFERENCES productos(id),
    cantidad_solicitada INTEGER NOT NULL,
    cantidad_aprobada INTEGER DEFAULT 0,
    precio_unitario DECIMAL(10,2)
);