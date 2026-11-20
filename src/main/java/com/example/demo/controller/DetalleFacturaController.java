package com.example.demo.controller;
import com.example.demo.model.DetalleFactura; import com.example.demo.service.DetalleFacturaService; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/detalles") public class DetalleFacturaController {
 private final DetalleFacturaService service; public DetalleFacturaController(DetalleFacturaService service){this.service=service;}
 @GetMapping public List<DetalleFactura> listar(){return service.listar();} @GetMapping("/{id}") public DetalleFactura buscar(@PathVariable Long id){return service.buscar(id);}
 @PostMapping public ResponseEntity<DetalleFactura> guardar(@RequestBody DetalleFactura v){return ResponseEntity.ok(service.guardar(v));} @PutMapping("/{id}") public DetalleFactura actualizar(@PathVariable Long id,@RequestBody DetalleFactura v){return service.actualizar(id,v);} @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id){service.eliminar(id);return ResponseEntity.noContent().build();}
}
