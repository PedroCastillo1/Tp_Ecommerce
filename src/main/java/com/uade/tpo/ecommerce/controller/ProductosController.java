package com.uade.tpo.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/productos")
public class ProductosController {

    @GetMapping
    public String getAllProductos(){
        return "Hola Bro, soy el endpoint de productos";
    }

    @GetMapping("/id")
    public String getProductoById(@RequestParam String id){
        return "Hola Bro, soy el producto id: " + id;
    }

}
