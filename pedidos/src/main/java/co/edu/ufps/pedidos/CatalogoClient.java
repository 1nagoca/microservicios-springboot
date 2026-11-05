package co.edu.ufps.pedidos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class CatalogoClient {

    private final RestClient restClient;

    public CatalogoClient(@Value("${catalogo.url}") String catalogoUrl) {
        this.restClient = RestClient.create(catalogoUrl);
    }

    // Llamada síncrona: GET http://localhost:8081/productos/{id}
    public ProductoDTO obtenerProducto(Long id) {
        return restClient.get()
                .uri("/productos/{id}", id)
                .retrieve()
                .body(ProductoDTO.class);
    }
}
