package com.example.demo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

/** Request body accepted by POST /api/facturas. */
public class CrearComprobanteRequest {
    @NotNull(message = "Debe indicar el cliente")
    @Valid
    private ClienteRequest cliente;

    @NotEmpty(message = "El comprobante debe tener al menos una línea")
    @Valid
    private List<@NotNull(message = "La línea no puede ser nula") LineaRequest> lineas;

    public ClienteRequest getCliente() { return cliente; }
    public void setCliente(ClienteRequest cliente) { this.cliente = cliente; }
    public List<LineaRequest> getLineas() { return lineas; }
    public void setLineas(List<LineaRequest> lineas) { this.lineas = lineas; }

    public static class ClienteRequest {
        @NotNull(message = "Debe indicar cliente.clienteid")
        @Positive(message = "cliente.clienteid debe ser mayor que cero")
        @JsonProperty("clienteid")
        private Long clienteId;

        public Long getClienteId() { return clienteId; }
        public void setClienteId(Long clienteId) { this.clienteId = clienteId; }
    }

    public static class LineaRequest {
        @NotNull(message = "Debe indicar la cantidad de la línea")
        @Positive(message = "La cantidad debe ser mayor que cero")
        private Integer cantidad;

        @NotNull(message = "Debe indicar el producto de la línea")
        @Valid
        private ProductoRequest producto;

        public Integer getCantidad() { return cantidad; }
        public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
        public ProductoRequest getProducto() { return producto; }
        public void setProducto(ProductoRequest producto) { this.producto = producto; }
    }

    public static class ProductoRequest {
        @NotNull(message = "Debe indicar linea.producto.productoid")
        @Positive(message = "producto.productoid debe ser mayor que cero")
        @JsonProperty("productoid")
        private Long productoId;

        public Long getProductoId() { return productoId; }
        public void setProductoId(Long productoId) { this.productoId = productoId; }
    }
}
