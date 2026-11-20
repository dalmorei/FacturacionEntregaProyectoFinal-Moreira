package com.example.demo.controller;
import com.example.demo.model.Producto; import com.example.demo.service.ProductoService; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/productos") public class ProductoController {
 private final ProductoService service; public ProductoController(ProductoService service){this.service=service;}
 @GetMapping public List<Producto> listar(){return service.listar();} @GetMapping("/{id}") public Producto buscar(@PathVariable Long id){return service.buscar(id);}
 @PostMapping public ResponseEntity<Producto> guardar(@RequestBody Producto v){return ResponseEntity.ok(service.guardar(v));} @PutMapping("/{id}") public Producto actualizar(@PathVariable Long id,@RequestBody Producto v){return service.actualizar(id,v);} @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id){service.eliminar(id);return ResponseEntity.noContent().build();}
}
