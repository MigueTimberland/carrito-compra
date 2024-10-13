package com.example.carrito_compra.controller;

import com.example.carrito_compra.service.DatabaseExportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DatabaseExportService databaseExportService;

    // Endpoint para descargar la base de datos en formato SQL
    @GetMapping("/export")
    public ResponseEntity<ByteArrayResource> exportDatabase() {
        byte[] data = databaseExportService.exportDatabaseToSql();
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=productos.sql")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .contentLength(data.length)
                .body(resource);
    }
}
