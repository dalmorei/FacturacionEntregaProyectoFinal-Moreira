package com.example.demo.service;
import com.example.demo.model.Cliente;
import com.example.demo.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import java.util.List;
@Service public class ClienteService {
 private final ClienteRepository repository;
 public ClienteService(ClienteRepository repository){this.repository=repository;}
 public List<Cliente> listar(){return repository.findAll();}
 public Cliente buscar(Long id){return repository.findById(id).orElseThrow(()->new RuntimeException("Cliente no encontrado: "+id));}
 public Cliente guardar(Cliente cliente){return repository.save(cliente);}
 public Cliente actualizar(Long id, Cliente datos){Cliente actual=buscar(id); actual.setNombre(datos.getNombre()); actual.setEmail(datos.getEmail()); return repository.save(actual);}
 public void eliminar(Long id){repository.delete(buscar(id));}
}
