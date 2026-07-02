package com.uade.tpo.ecommerce;

// ## PUNTO DE ENTRADA — Aplicación Spring Boot
// ##
// ## Este es el archivo principal que arranca toda la aplicación.
// ## @SpringBootApplication combina tres anotaciones:
// ##   @Configuration     → este archivo puede definir Beans adicionales
// ##   @EnableAutoConfiguration → Spring configura automáticamente componentes
// ##                              (DataSource, JPA, Security, Web MVC, etc.)
// ##   @ComponentScan     → Spring escanea todas las clases del paquete
// ##                        "com.uade.tpo.ecommerce" y sus subpaquetes
// ##                        buscando @Component, @Service, @Repository, @Controller
// ##
// ## Al ejecutar: SpringApplication.run() inicia el servidor embebido Tomcat
// ## en el puerto 8080 (configurable en application.properties)

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TpEcommerceApplication {

    // ## main(): punto de entrada de la JVM — arranca todo Spring Boot
    public static void main(String[] args) {
        SpringApplication.run(TpEcommerceApplication.class, args);
    }
}
