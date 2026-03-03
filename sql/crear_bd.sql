

-- =============================================
-- SCRIPT COMPLETO PARA CREAR LA BASE DE DATOS
-- =============================================

-- 1. Crear la base de datos
CREATE DATABASE sistema_inventarios
    WITH 
    OWNER = postgres
    ENCODING = 'UTF8'
    LC_COLLATE = 'es_ES.UTF-8'
    LC_CTYPE = 'es_ES.UTF-8'
    CONNECTION LIMIT = -1;

-- 2. Conectarse a la base de datos
\c sistema_inventarios;

-- 3. Crear todas las tablas (copiar y pegar todas las CREATE TABLE de arriba)

-- 4. Insertar datos iniciales (opcional)
INSERT INTO almacenes (codigo, nombre, ubicacion, responsable, es_central) VALUES
('ALM-001', 'Almacén Central', 'Sede Principal', 'Administrador', TRUE),
('ALM-002', 'Almacén Norte', 'Zona Norte', 'Pendiente', FALSE),
('ALM-003', 'Almacén Sur', 'Zona Sur', 'Pendiente', FALSE);

INSERT INTO categorias (codigo, nombre, descripcion) VALUES
('CAT-001', 'Electrónica', 'Productos electrónicos y gadgets'),
('CAT-002', 'Alimentos', 'Productos alimenticios y bebidas'),
('CAT-003', 'Ropa y Accesorios', 'Prendas de vestir'),
('CAT-004', 'Hogar', 'Artículos para el hogar');

INSERT INTO productos (codigo, nombre, categoria_id, unidad_medida, precio_costo, precio_venta, stock_minimo) VALUES
('PROD-001', 'Laptop HP', 1, 'Unidad', 450.00, 650.00, 5),
('PROD-002', 'Mouse Inalámbrico', 1, 'Unidad', 8.50, 15.99, 10),
('PROD-003', 'Arroz 5kg', 2, 'Kg', 2.80, 4.50, 20),
('PROD-004', 'Camiseta Algodón', 3, 'Unidad', 5.00, 12.50, 15);

INSERT INTO clientes (codigo, nombre, documento, telefono) VALUES
('CLI-001', 'Consumidor Final', '000-0000000-0', 'N/A'),
('CLI-002', 'Juan Pérez', '001-1234567-8', '809-555-1111'),
('CLI-003', 'María García', '002-2345678-9', '809-555-2222');

INSERT INTO usuarios (nombre, username, password, rol_id, almacen_id) VALUES
('Administrador', 'admin', 'admin123', 1, 1),
('Carlos Vendedor', 'carlos', 'clave123', 2, 2),
('Ana Contadora', 'ana', 'contador456', 3, 1);

-- 5. Crear índices para mejorar rendimiento
CREATE INDEX idx_productos_categoria ON productos(categoria_id);
CREATE INDEX idx_stock_producto ON stock_almacen(producto_id);
CREATE INDEX idx_stock_almacen ON stock_almacen(almacen_id);
CREATE INDEX idx_ventas_fecha ON ventas(fecha_venta);
CREATE INDEX idx_ventas_vendedor ON ventas(vendedor_id);
CREATE INDEX idx_turnos_usuario ON turnos(usuario_id);
CREATE INDEX idx_turnos_estado ON turnos(estado);