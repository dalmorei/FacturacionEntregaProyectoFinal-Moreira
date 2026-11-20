package com.example.demo.service;
import com.example.demo.model.Factura;
import com.example.demo.repository.FacturaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class FacturaService {
 private final FacturaRepository repository;
 public FacturaService(FacturaRepository repository){this.repository=repository;}
 public List<Factura> listar(){return repository.findAll();}
 public Factura buscar(Long id){return repository.findById(id).orElseThrow(()->new RuntimeException("Factura no encontrada: "+id));}
 public Factura guardar(Factura factura){factura.recalcularTotal(); return repository.save(factura);}
 public Factura actualizar(Long id, Factura datos){Factura actual=buscar(id); actual.setFecha(datos.getFecha()); actual.setCliente(datos.getCliente()); actual.getDetalles().clear(); datos.getDetalles().forEach(actual::agregarDetalle); return guardar(actual);}
 public void eliminar(Long id){repository.delete(buscar(id));}
}
