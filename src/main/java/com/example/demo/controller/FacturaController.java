package com.example.demo.controller;
import com.example.demo.model.Factura; import com.example.demo.service.FacturaService; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/facturas") public class FacturaController {
 private final FacturaService service; public FacturaController(FacturaService service){this.service=service;}
 @GetMapping public List<Factura> listar(){return service.listar();} @GetMapping("/{id}") public Factura buscar(@PathVariable Long id){return service.buscar(id);}
 @PostMapping public ResponseEntity<Factura> guardar(@RequestBody Factura v){return ResponseEntity.ok(service.guardar(v));} @PutMapping("/{id}") public Factura actualizar(@PathVariable Long id,@RequestBody Factura v){return service.actualizar(id,v);} @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id){service.eliminar(id);return ResponseEntity.noContent().build();}
}
