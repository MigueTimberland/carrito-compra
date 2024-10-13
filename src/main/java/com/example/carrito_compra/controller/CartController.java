package com.example.carrito_compra.controller;

import com.example.carrito_compra.model.Cart;
import com.example.carrito_compra.model.Product;
import com.example.carrito_compra.service.CartService;
import com.example.carrito_compra.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private ProductService productService;

    // Ver productos específicos añadidos al carrito
    @GetMapping("/view/{id}")
    public String viewAddedProduct(@PathVariable Long id, Model model) {
        Long userId = 1L;  // ID del usuario actual
        Cart cart = cartService.getCartByUserId(userId);

        if (cart != null) {
            // Obtener los productos del carrito
            model.addAttribute("cartItems", cartService.getCartItems(cart.getId()));
            model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart.getId()));
        }

        // Mostrar específicamente el producto añadido
        Product product = productService.getProductById(id);
        model.addAttribute("addedProduct", product);  // Producto recién añadido

        return "cart-product-view";
    }

    // Eliminar un producto del carrito
    @GetMapping("/remove/{productId}")
    public String removeProductFromCart(@PathVariable Long productId) {
        Long userId = 1L;  // ID del usuario actual

        // Obtener el carrito del usuario
        Cart cart = cartService.getCartByUserId(userId);

        if (cart != null) {
            // Pasar el ID del carrito y el ID del producto
            cartService.removeProductFromCart(cart.getId(), productId);
        }

        return "redirect:/cart/view";
    }

    // Vaciar el carrito
    @GetMapping("/clear")
    public String clearCart() {
        Long userId = 1L;  // ID del usuario actual
        Cart cart = cartService.getCartByUserId(userId);

        if (cart != null) {
            cartService.clearCart(cart.getId());  // Vaciar el carrito
        }
        return "redirect:/cart/view";  // Redirige al carrito vacío
    }

    @GetMapping("/add/{productId}")
    public String addProductToCart(@PathVariable Long productId) {
        Long userId = 1L;  // ID del usuario actual (simulado)

        // Obtener el carrito del usuario o crear uno nuevo
        Cart cart = cartService.getCartByUserId(userId);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cartService.saveCart(cart);
        }

        // Obtener el producto por su ID
        Product product = productService.getProductById(productId);
        if (product == null) {
            throw new RuntimeException("Producto no encontrado");
        }

        // Añadir el producto al carrito con cantidad 1
        cartService.addProductToCart(cart.getId(), product, 1);

        return "redirect:/cart/view";  // Redirigir al carrito
    }

    // Ver carrito
    @GetMapping("/view")
    public String viewCart(Model model) {
        Long userId = 1L;  // ID del usuario actual
        Cart cart = cartService.getCartByUserId(userId);  // Obtener el carrito del usuario

        if (cart != null) {
            model.addAttribute("cartItems", cartService.getCartItems(cart.getId()));  // Obtener los productos del carrito
            model.addAttribute("totalPrice", cartService.calculateTotalPrice(cart.getId()));  // Calcular total del carrito
        }

        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cartService.saveCart(cart);
        }

        return "cart";
    }
}
