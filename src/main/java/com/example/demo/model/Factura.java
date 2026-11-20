package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "facturas")
public class Factura {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate fecha;
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal total = BigDecimal.ZERO;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cliente_id", nullable = false)
    @JsonBackReference("cliente-facturas")
    private Cliente cliente;
    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("factura-detalles")
    private List<DetalleFactura> detalles = new ArrayList<>();

    public Factura() { }
    public Factura(LocalDate fecha, Cliente cliente) { this.fecha = fecha; this.cliente = cliente; }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
    public BigDecimal getTotal() { return total; }
    public Cliente getCliente() { return cliente; }
    public void setCliente(Cliente cliente) { this.cliente = cliente; }
    public List<DetalleFactura> getDetalles() { return detalles; }
    public void agregarDetalle(DetalleFactura detalle) { detalles.add(detalle); detalle.setFactura(this); recalcularTotal(); }
    public void quitarDetalle(DetalleFactura detalle) { detalles.remove(detalle); detalle.setFactura(null); recalcularTotal(); }
    public void recalcularTotal() { total = detalles.stream().map(DetalleFactura::subtotal).reduce(BigDecimal.ZERO, BigDecimal::add); }
}
