package com.example.carrito_compra.service;

import com.example.carrito_compra.model.Product;
import com.example.carrito_compra.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class DatabaseExportService {

    @Autowired
    private ProductRepo productRepo;

    // Exportar la base de datos de productos como un script SQL
    public byte[] exportDatabaseToSql() {
        List<Product> products = productRepo.findAll(); // Obtiene todos los productos de la base de datos
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(outputStream);

        // Generar comandos SQL para insertar los productos en la base de datos
        for (Product product : products) {
            String sql = String.format(
                    "INSERT INTO product (id, name, price, quantity) VALUES (%d, '%s', %.2f, %d);",
                    product.getId(), product.getName(), product.getPrice(), product.getQuantity()
            );
            writer.println(sql);
        }

        writer.flush();
        try {
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return outputStream.toByteArray();
    }
}
