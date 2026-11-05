package co.edu.ufps.pedidos;

// Lo que Pedidos necesita saber de un producto. No es la entidad de Catálogo.
public record ProductoDTO(Long id, String nombre, Double precio, Integer stock) {
}
