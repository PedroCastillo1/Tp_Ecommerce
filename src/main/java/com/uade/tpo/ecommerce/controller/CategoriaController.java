package com.uade.tpo.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/categoria")
public class CategoriaController {
    @Autowired
    private com.uade.tpo.ecommerce.service.ICategoryService categoryService;


    @GetMapping("/lentes")
    public String lentes(){
        return "aca hay lentes";
    }

    @GetMapping("/bolsos")
    public String bolsos() {
        return new String();
    }

    @GetMapping("/autos")
    public String autos() {
        return new String();
    }
    
    @GetMapping("/celulares")
    public String celulares() {
        return new String();
    }
    


    
}
