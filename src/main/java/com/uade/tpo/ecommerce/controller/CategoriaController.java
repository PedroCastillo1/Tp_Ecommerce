package com.uade.tpo.ecommerce.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/categoria")
public class CategoriaController {


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
    
    


    
}
