package com.example.carrito_compra.controller;

import com.example.carrito_compra.model.Product;
import com.example.carrito_compra.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    // Mostrar todos los productos
    @GetMapping
    public String getAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "productos";
    }

    // Mostrar el formulario para agregar o editar un producto
    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "formulario-producto";
    }

    // Manejar la adición o edición de un producto
    @PostMapping("/save")
    public String saveProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/products";
    }

    // Mostrar el formulario para editar un producto
    @GetMapping("/edit/{id}")
    public String showEditProductForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id);
        model.addAttribute("product", product);
        return "formulario-producto";
    }

    // Metodo para eliminar un producto por ID
    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, Model model) {
        try {
            productService.deleteProduct(id);
        } catch (Exception e) {
            // Capturar la excepción completa y mostrarla en los logs para diagnóstico
            e.printStackTrace();

            // Mostrar el mensaje de error detallado en la página
            model.addAttribute("errorMessage", "Error al eliminar el producto: " + e.getMessage());
            return "error";
        }
        return "redirect:/products";
    }

    // Buscar productos por nombre
    @GetMapping("/search")
    public String searchProducts(@RequestParam("query") String query, Model model) {
        List<Product> products = productService.searchProductsByName(query);
        model.addAttribute("products", products);
        return "productos";
    }

    @GetMapping("/filter")
    public String filterProductsByPrice(@RequestParam("minPrice") double minPrice, @RequestParam("maxPrice") double maxPrice, Model model) {
        List<Product> filteredProducts = productService.filterByPriceRange(minPrice, maxPrice);
        model.addAttribute("products", filteredProducts);
        return "productos";
    }

    // Metodo para exportar los productos como un archivo SQL
    @GetMapping("/export")
    public ResponseEntity<Resource> exportProductsToSQL() throws IOException {
        List<Product> products = productService.getAllProducts();
        StringBuilder sqlBuilder = new StringBuilder("-- Exportación de la tabla Product\n\n");

        for (Product product : products) {
            sqlBuilder.append(String.format(
                    "INSERT INTO Product (id, name, price, quantity) VALUES (%d, '%s', %.2f, %d);\n",
                    product.getId(), product.getName(), product.getPrice(), product.getQuantity()
            ));
        }

        // Crear el archivo temporal
        Path path = Files.createTempFile("productos_export", ".sql");
        Files.write(path, sqlBuilder.toString().getBytes());

        // Devolver el archivo como recurso para descarga
        Resource resource = new FileSystemResource(path.toFile());
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=productos_export.sql")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}
