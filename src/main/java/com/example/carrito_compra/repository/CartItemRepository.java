package com.example.carrito_compra.repository;

import com.example.carrito_compra.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    // Obtener todos los productos en un carrito por el ID del carrito
    List<CartItem> findByCartId(Long cartId);

    // Obtener un producto específico en un carrito por el ID del carrito y el ID del producto
    CartItem findByCartIdAndProductId(Long cartId, Long productId);

    // Eliminar todos los CartItems relacionados con un producto específico
    void deleteByProductId(Long productId);
}
