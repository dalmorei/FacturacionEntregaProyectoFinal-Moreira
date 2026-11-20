package com.example.demo.service;

import com.example.demo.dto.ComprobanteResponse;
import com.example.demo.dto.CrearComprobanteRequest;
import com.example.demo.model.Cliente;
import com.example.demo.model.DetalleFactura;
import com.example.demo.model.Factura;
import com.example.demo.model.Producto;
import com.example.demo.repository.ClienteRepository;
import com.example.demo.repository.FacturaRepository;
import com.example.demo.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/** Business operations for invoices, including stock and historic-price handling. */
@Service
public class FacturaService {
    private final FacturaRepository facturaRepository;
    private final ClienteRepository clienteRepository;
    private final ProductoRepository productoRepository;
    private final FechaComprobanteService fechaComprobanteService;

    public FacturaService(
            FacturaRepository facturaRepository,
            ClienteRepository clienteRepository,
            ProductoRepository productoRepository,
            FechaComprobanteService fechaComprobanteService) {
        this.facturaRepository = facturaRepository;
        this.clienteRepository = clienteRepository;
        this.productoRepository = productoRepository;
        this.fechaComprobanteService = fechaComprobanteService;
    }

    @Transactional(readOnly = true)
    public List<ComprobanteResponse> listarComprobantes() {
        return facturaRepository.findAll().stream()
                .map(factura -> crearRespuesta(factura, calcularCantidadProductos(factura)))
                .toList();
    }

    @Transactional(readOnly = true)
    public ComprobanteResponse buscarComprobante(Long id) {
        Factura factura = buscar(id);
        return crearRespuesta(factura, calcularCantidadProductos(factura));
    }

    public Factura buscar(Long id) {
        return facturaRepository.findById(id)
                .orElseThrow(() -> new ValidacionNegocioException("Comprobante no encontrado: " + id));
    }

    /**
     * Validates the full order before changing inventory. Pessimistic product locks keep
     * concurrent sales from consuming the same units and the transaction rolls back all
     * inventory changes if persisting the invoice fails.
     */
    @Transactional
    public ComprobanteResponse crear(CrearComprobanteRequest request) {
        if (request == null || request.getCliente() == null || request.getCliente().getClienteId() == null) {
            throw new ValidacionNegocioException("Debe indicar un cliente válido");
        }
        if (request.getLineas() == null || request.getLineas().isEmpty()) {
            throw new ValidacionNegocioException("El comprobante debe tener al menos una línea");
        }

        Long clienteId = request.getCliente().getClienteId();
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ValidacionNegocioException("No existe el cliente con id " + clienteId));

        Map<Long, Integer> cantidadesPorProducto = new TreeMap<>();
        int cantidadProductos = 0;
        for (CrearComprobanteRequest.LineaRequest linea : request.getLineas()) {
            if (linea == null || linea.getCantidad() == null || linea.getCantidad() <= 0) {
                throw new ValidacionNegocioException("Cada línea debe tener una cantidad mayor que cero");
            }
            if (linea.getProducto() == null || linea.getProducto().getProductoId() == null) {
                throw new ValidacionNegocioException("Cada línea debe indicar un producto válido");
            }

            Long productoId = linea.getProducto().getProductoId();
            try {
                cantidadesPorProducto.merge(productoId, linea.getCantidad(), Math::addExact);
                cantidadProductos = Math.addExact(cantidadProductos, linea.getCantidad());
            } catch (ArithmeticException exception) {
                throw new ValidacionNegocioException("La cantidad total solicitada excede el límite permitido");
            }
        }

        Map<Long, Producto> productos = new TreeMap<>();
        for (Long productoId : cantidadesPorProducto.keySet()) {
            Producto producto = productoRepository.buscarPorIdConBloqueo(productoId)
                    .orElseThrow(() -> new ValidacionNegocioException("No existe el producto con id " + productoId));
            Integer stock = producto.getStock();
            if (stock == null || stock < 0) {
                throw new ValidacionNegocioException("El producto " + productoId + " tiene un stock inválido");
            }
            if (producto.getPrecio() == null || producto.getPrecio().signum() < 0) {
                throw new ValidacionNegocioException("El producto " + productoId + " no tiene un precio válido");
            }
            Integer cantidadSolicitada = cantidadesPorProducto.get(productoId);
            if (cantidadSolicitada > stock) {
                throw new ValidacionNegocioException("Stock insuficiente para el producto " + productoId
                        + ": solicitado " + cantidadSolicitada + ", disponible " + stock);
            }
            productos.put(productoId, producto);
        }

        // All products and quantities are valid; inventory can now be reduced atomically.
        cantidadesPorProducto.forEach((productoId, cantidad) -> {
            Producto producto = productos.get(productoId);
            producto.setStock(producto.getStock() - cantidad);
        });

        Factura factura = new Factura(fechaComprobanteService.obtenerFecha(), cliente);
        for (CrearComprobanteRequest.LineaRequest linea : request.getLineas()) {
            Producto producto = productos.get(linea.getProducto().getProductoId());
            // Each detail permanently stores the price used for this sale.
            factura.agregarDetalle(new DetalleFactura(
                    linea.getCantidad(), producto.getPrecio(), producto));
        }
        factura.recalcularTotal();
        Factura guardada = facturaRepository.save(factura);
        return crearRespuesta(guardada, cantidadProductos);
    }

    private ComprobanteResponse crearRespuesta(Factura factura, int cantidadProductos) {
        List<ComprobanteResponse.LineaResponse> lineas = new ArrayList<>();
        for (DetalleFactura detalle : factura.getDetalles()) {
            Producto producto = detalle.getProducto();
            BigDecimal subtotal = detalle.subtotal();
            lineas.add(new ComprobanteResponse.LineaResponse(
                    producto.getId(), producto.getNombre(), detalle.getCantidad(),
                    detalle.getPrecioUnitario(), subtotal));
        }
        return new ComprobanteResponse(
                factura.getId(), factura.getFecha(), factura.getCliente().getId(),
                List.copyOf(lineas), cantidadProductos, factura.getTotal());
    }

    private int calcularCantidadProductos(Factura factura) {
        int cantidad = 0;
        for (DetalleFactura detalle : factura.getDetalles()) {
            cantidad = Math.addExact(cantidad, detalle.getCantidad());
        }
        return cantidad;
    }

    public Factura guardar(Factura factura) {
        factura.recalcularTotal();
        return facturaRepository.save(factura);
    }

    public Factura actualizar(Long id, Factura datos) {
        Factura actual = buscar(id);
        actual.setFecha(datos.getFecha());
        actual.setCliente(datos.getCliente());
        actual.getDetalles().clear();
        datos.getDetalles().forEach(actual::agregarDetalle);
        return guardar(actual);
    }

    public void eliminar(Long id) { facturaRepository.delete(buscar(id)); }
}
