package com.example.demo.controller;
import com.example.demo.model.Cliente; import com.example.demo.service.ClienteService; import org.springframework.http.ResponseEntity; import org.springframework.web.bind.annotation.*; import java.util.List;
@RestController @RequestMapping("/api/clientes") public class ClienteController {
 private final ClienteService service; public ClienteController(ClienteService service){this.service=service;}
 @GetMapping public List<Cliente> listar(){return service.listar();} @GetMapping("/{id}") public Cliente buscar(@PathVariable Long id){return service.buscar(id);}
 @PostMapping public ResponseEntity<Cliente> guardar(@RequestBody Cliente v){return ResponseEntity.ok(service.guardar(v));}
 @PutMapping("/{id}") public Cliente actualizar(@PathVariable Long id,@RequestBody Cliente v){return service.actualizar(id,v);} @DeleteMapping("/{id}") public ResponseEntity<Void> eliminar(@PathVariable Long id){service.eliminar(id);return ResponseEntity.noContent().build();}
}
