package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import java.math.BigDecimal;

@Entity
@Table(name = "productos")
public class Producto {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String nombre;
    @Column(nullable = false)
    private BigDecimal precio;
    @Column(nullable = false)
    private Integer stock = 0;

    public Producto() {

    }

    public Producto(String nombre, BigDecimal precio) {
        this.nombre = nombre;
        this.precio = precio;
    }

    public Producto(String nombre, BigDecimal precio, Integer stock) {
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
    }

    public void setId(Long id){
        this.id = id;
    }

    public Long getId() { return id; }

    public String getNombre(){
        return nombre;
    }

    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public BigDecimal getPrecio(){
        return precio;
    }

    public void setPrecio(BigDecimal precio){
        this.precio = precio;
    }

    public Integer getStock() { return stock; }

    public void setStock(Integer stock) { this.stock = stock; }


    @Override
    public String toString(){
        return "producto("+
                "id: "+id+
                "nombre: "+nombre+
                "precio: "+precio+
                ", stock: "+stock+
                ")";
    }
}



