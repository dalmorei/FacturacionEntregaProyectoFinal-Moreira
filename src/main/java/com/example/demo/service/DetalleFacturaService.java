package com.example.demo.service;
import com.example.demo.model.DetalleFactura;
import com.example.demo.repository.DetalleFacturaRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class DetalleFacturaService {
 private final DetalleFacturaRepository repository;
 public DetalleFacturaService(DetalleFacturaRepository repository){this.repository=repository;}
 public List<DetalleFactura> listar(){return repository.findAll();}
 public DetalleFactura buscar(Long id){return repository.findById(id).orElseThrow(()->new RuntimeException("Detalle no encontrado: "+id));}
 public DetalleFactura guardar(DetalleFactura detalle){return repository.save(detalle);}
 public DetalleFactura actualizar(Long id, DetalleFactura datos){DetalleFactura actual=buscar(id); actual.setCantidad(datos.getCantidad()); actual.setPrecioUnitario(datos.getPrecioUnitario()); actual.setProducto(datos.getProducto()); return repository.save(actual);}
 public void eliminar(Long id){repository.delete(buscar(id));}
}
