package co.edu.ufps.pedidos;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoRepository repositorio;
    private final CatalogoClient catalogo;

    public PedidoController(PedidoRepository repositorio, CatalogoClient catalogo) {
        this.repositorio = repositorio;
        this.catalogo = catalogo;
    }

    @GetMapping
    public List<Pedido> listar() {
        return repositorio.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Pedido crear(@Valid @RequestBody NuevoPedido solicitud) {
        ProductoDTO producto;
        try {
            producto = catalogo.obtenerProducto(solicitud.productoId());
        } catch (HttpClientErrorException.NotFound e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El producto " + solicitud.productoId() + " no existe");
        } catch (ResourceAccessException e) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE,
                    "El servicio de catalogo no responde");
        }

        if (producto.stock() < solicitud.cantidad()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Stock insuficiente: quedan " + producto.stock());
        }

        Pedido pedido = new Pedido();
        pedido.setProductoId(producto.id());
        pedido.setNombreProducto(producto.nombre());
        pedido.setCantidad(solicitud.cantidad());
        pedido.setTotal(producto.precio() * solicitud.cantidad());
        pedido.setEstado("CREADO");
        return repositorio.save(pedido);
    }
}
