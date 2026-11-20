package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clientes")
public class Cliente {

    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    private String nombre;
    private String email;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference("cliente-facturas")
    private List<Factura> facturas = new ArrayList<>();

    public Cliente() {

    }

    public Cliente(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
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

    public String getEmail(){
        return email;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public List<Factura> getFacturas() { return facturas; }
    public void agregarFactura(Factura factura) { facturas.add(factura); factura.setCliente(this); }
    public void quitarFactura(Factura factura) { facturas.remove(factura); factura.setCliente(null); }


    @Override
    public String toString(){
        return "cliente("+
                "id: "+id+
                "nombre: "+nombre+
                "email: "+email+
                ")";
    }
}



