package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "detalles_factura")
public class DetalleFactura {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false) private Integer cantidad;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal precioUnitario;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "factura_id", nullable = false)
    @JsonBackReference("factura-detalles") private Factura factura;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "producto_id", nullable = false) private Producto producto;
    public DetalleFactura() { }
    public DetalleFactura(Integer cantidad, BigDecimal precioUnitario, Producto producto) { this.cantidad=cantidad; this.precioUnitario=precioUnitario; this.producto=producto; }
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public Integer getCantidad(){return cantidad;} public void setCantidad(Integer cantidad){this.cantidad=cantidad;}
    public BigDecimal getPrecioUnitario(){return precioUnitario;} public void setPrecioUnitario(BigDecimal precioUnitario){this.precioUnitario=precioUnitario;}
    public Factura getFactura(){return factura;} public void setFactura(Factura factura){this.factura=factura;}
    public Producto getProducto(){return producto;} public void setProducto(Producto producto){this.producto=producto;}
    public BigDecimal subtotal(){return precioUnitario.multiply(BigDecimal.valueOf(cantidad));}
}
