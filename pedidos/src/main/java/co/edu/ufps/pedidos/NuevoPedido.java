package co.edu.ufps.pedidos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

// Lo que el cliente envía para crear un pedido
public record NuevoPedido(@NotNull Long productoId, @NotNull @Positive Integer cantidad) {
}
