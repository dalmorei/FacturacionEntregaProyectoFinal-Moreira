package com.example.demo.controller;
import com.example.demo.dto.ComprobanteResponse;
import com.example.demo.dto.CrearComprobanteRequest;
import com.example.demo.model.Factura;
import com.example.demo.service.FacturaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/api/facturas", "/api/comprobantes"})
public class FacturaController {
    private final FacturaService service;

    public FacturaController(FacturaService service) { this.service = service; }

    @GetMapping
    public List<ComprobanteResponse> listar() { return service.listarComprobantes(); }

    @GetMapping("/{id}")
    public ComprobanteResponse buscar(@PathVariable Long id) { return service.buscarComprobante(id); }

    @PostMapping
    public ResponseEntity<ComprobanteResponse> crear(@Valid @RequestBody CrearComprobanteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(request));
    }

    @PutMapping("/{id}")
    public Factura actualizar(@PathVariable Long id, @RequestBody Factura factura) {
        return service.actualizar(id, factura);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
