package com.example.carrito_compra.repository;

import com.example.carrito_compra.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    // Metodo para encontrar el carrito por el ID de usuario
    Cart findByUserId(Long userId);

}
