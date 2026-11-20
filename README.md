# FacturacionEntregaProyectoFinal-Moreira

API REST para administrar clientes, productos, comprobantes y sus líneas. Está implementada con Java 21, Spring Boot, Spring Data JPA y H2.

## Ejecutar

Desde la raíz del repositorio:

```bash
./mvnw clean package
java -jar target/FacturacionEntregaProyectoFinal-Moreira.jar
```

En Windows se puede usar `mvnw.cmd clean package`. La API inicia en `http://localhost:8080`. La consola H2 está en `/h2-console` (JDBC URL `jdbc:h2:mem:facturacion`, usuario `sa`, contraseña vacía). El script `src/main/resources/schema.sql` crea las tablas y `src/main/resources/data.sql` agrega datos de ejemplo.

## Crear comprobante

Se puede usar `POST /api/comprobantes` o `POST /api/facturas`, con `Content-Type: application/json`:

```json
{
  "cliente": { "clienteid": 1 },
  "lineas": [
    { "cantidad": 1, "producto": { "productoid": 1 } }
  ]
}
```

La respuesta `201 Created` contiene la fecha, el total, las unidades vendidas y el precio unitario guardado en cada línea. La fecha se consulta a `http://worldclockapi.com/api/json/utc/now`; ante una falla se usa `java.util.Date`. El servicio valida cliente, productos, cantidades y stock, y descuenta las unidades en la misma transacción. Cada detalle conserva el precio de venta, de modo que modificar después el precio del producto no altera comprobantes existentes.

Las validaciones devuelven `400 Bad Request` con `fecha`, `estado`, `error` y `mensaje`. Los datos de prueba iniciales son los clientes 1 y 2, y los productos 1 y 2 (stock 10 y 20).

## Otros endpoints

- Clientes: `/api/clientes`
- Productos: `/api/productos` (incluye `stock`)
- Comprobantes: `/api/comprobantes` o `/api/facturas`
- Detalles: `/api/detalles`

La colección importable de Postman está en `postman/FacturacionEntregaProyectoFinal-Moreira.postman_collection.json`. Incluye una venta válida y solicitudes para cliente inexistente, producto inexistente y stock insuficiente.

## Entrega del proyecto

El nombre del artefacto ejecutable es `FacturacionEntregaProyectoFinal-Moreira.jar`. El remoto configurado actualmente en este checkout es GitHub (`https://github.com/dalmorei/FacturacionEntregaProyectoFinal-Moreira.git`);
