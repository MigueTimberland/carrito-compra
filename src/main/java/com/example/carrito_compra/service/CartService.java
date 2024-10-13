package com.example.carrito_compra.service;

import com.example.carrito_compra.model.Cart;
import com.example.carrito_compra.model.CartItem;
import com.example.carrito_compra.model.Product;
import com.example.carrito_compra.repository.CartRepository;
import com.example.carrito_compra.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    // Obtener el carrito por el ID del usuario
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }

    // Añadir un producto al carrito
    public void addProductToCart(Long cartId, Product product, int quantity) {
        Cart cart = cartRepository.findById(cartId).orElseThrow(() -> new RuntimeException("Carrito no encontrado"));

        // Verificar si el producto ya existe en el carrito
        CartItem existingCartItem = cartItemRepository.findByCartIdAndProductId(cartId, product.getId());
        if (existingCartItem != null) {
            // Si el producto ya está en el carrito, incrementa la cantidad
            existingCartItem.setQuantity(existingCartItem.getQuantity() + quantity);
            cartItemRepository.save(existingCartItem);
        } else {
            // Si no está en el carrito, añadirlo como un nuevo ítem
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
    }

    // Obtener todos los productos en un carrito
    public List<CartItem> getCartItems(Long cartId) {
        return cartItemRepository.findByCartId(cartId);
    }

    // Calcular el total de precios en el carrito
    public double calculateTotalPrice(Long cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        return cartItems.stream().mapToDouble(item -> item.getProduct().getPrice() * item.getQuantity()).sum();
    }

    // Vaciar el carrito
    public void clearCart(Long cartId) {
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);
        cartItemRepository.deleteAll(cartItems);
    }

    // Metodo para eliminar un producto específico del carrito usando el ID del carrito y el ID del producto
    public void removeProductFromCart(Long cartId, Long productId) {
        // Buscar el producto en el carrito
        CartItem cartItem = cartItemRepository.findByCartIdAndProductId(cartId, productId);

        // Eliminar si existe
        if (cartItem != null) {
            cartItemRepository.delete(cartItem);  // Eliminar el producto del carrito
        }
    }

    // Guardar el carrito en la base de datos
    public void saveCart(Cart cart) {
        cartRepository.save(cart);
    }
}
