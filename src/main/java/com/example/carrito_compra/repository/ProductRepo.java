package com.example.carrito_compra.repository;

import com.example.carrito_compra.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepo extends JpaRepository<Product, Long> {
    // Metodo para buscar productos
    List<Product> findByNameContainingIgnoreCase(String name);

    // Metodo para filtrar productos por rango de precios
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
}

