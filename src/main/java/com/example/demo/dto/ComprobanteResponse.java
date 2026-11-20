package com.example.demo.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/** Immutable result with sale totals and the prices captured at sale time. */
public record ComprobanteResponse(
        Long id,
        LocalDate fecha,
        Long clienteId,
        List<LineaResponse> lineas,
        Integer cantidadProductos,
        BigDecimal total) {

    public record LineaResponse(
            Long productoId,
            String productoNombre,
            Integer cantidad,
            BigDecimal precioUnitario,
            BigDecimal subtotal) { }
}
