# FacturacionSegundaEntregaMoreira

API REST de facturación implementada con Spring Boot, Spring Data JPA y H2. El JAR ejecutable se genera como `FacturacionSegundaEntregaMoreira.jar`.

## Ejecución

Requiere Java 21 y Maven 3.9+.

```bash
mvn clean package
java -jar target/FacturacionSegundaEntregaMoreira.jar
```

La aplicación queda disponible en `http://localhost:8080`. La consola H2 está en `/h2-console`, con URL `jdbc:h2:mem:facturacion`, usuario `sa` y contraseña vacía.

## Capas y endpoints

Cada entidad posee `Controller`, `Service` y `Repository`: `Cliente` (`/api/clientes`), `Producto` (`/api/productos`), `Factura` (`/api/facturas`) y `DetalleFactura` (`/api/detalles`).

El agregado `Cliente -> Factura -> DetalleFactura` usa `CascadeType.ALL` y `orphanRemoval=true`; las altas, modificaciones y bajas de las colecciones se propagan a sus dependientes. El total de cada factura se recalcula desde sus detalles.

## Inicialización

`src/main/resources/schema.sql` crea las tablas y sus claves foráneas; `src/main/resources/data.sql` carga clientes y productos de ejemplo al iniciar. Para usar una base externa, cambie las propiedades `spring.datasource.*` y adapte los scripts SQL al motor elegido.
