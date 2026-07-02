package com.uade.tpo.ecommerce.config;

// ## INICIALIZADOR DE DATOS — Seed de la Base de Datos
// ##
// ## Se ejecuta automáticamente cuando arranca la aplicación Spring Boot.
// ## Implementa CommandLineRunner → Spring llama a run() justo después de arrancar.
// ##
// ## PROPÓSITO: Poblar la BD con datos de ejemplo si está vacía,
// ## para que la tienda tenga productos desde el primer arranque.
// ##
// ## CONDICIÓN: Solo carga datos si hay menos de 10 productos (productosRepository.count() < 10)
// ## Esto evita duplicar datos si el servidor se reinicia.
// ##
// ## CARGA: 8 categorías + ~25 productos con imágenes reales de Unsplash.

import com.uade.tpo.ecommerce.model.Category;
import com.uade.tpo.ecommerce.model.Product;
import com.uade.tpo.ecommerce.repository.ICategoriaRepository;
import com.uade.tpo.ecommerce.repository.IProductosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ICategoriaRepository categoriaRepository;

    @Autowired
    private IProductosRepository productosRepository;

    // ## Punto de entrada: Spring lo llama automáticamente al arrancar la app
    @Override
    public void run(String... args) {
        // ## Solo carga datos si la base está vacía (evita duplicados en reinicios)
        if (productosRepository.count() >= 10) return;

        System.out.println(">>> Cargando datos iniciales...");

        // ## Crear las 8 categorías base del catálogo
        Category electronica = save("Electrónica");
        Category ropa        = save("Ropa");
        Category calzado     = save("Calzado");
        Category hogar       = save("Hogar");
        Category deportes    = save("Deportes");
        Category libros      = save("Libros");
        Category juguetes    = save("Juguetes");
        Category belleza     = save("Belleza");

        // ## Electrónica
        prod("Smartphone Galaxy S24",
             "Último modelo con IA integrada, cámara de 50MP y pantalla AMOLED 120Hz.",
             899.99, 25,
             "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=500&q=80",
             Set.of(electronica));

        prod("Laptop UltraBook Pro 15",
             "Intel Core i7 de 13° gen, 16GB RAM, SSD 512GB, pantalla 4K táctil.",
             1299.99, 10,
             "https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=500&q=80",
             Set.of(electronica));

        prod("Auriculares Sony WH-1000XM5",
             "Cancelación de ruido activa líder en su clase, 30 horas de batería, carga rápida.",
             349.99, 40,
             "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=500&q=80",
             Set.of(electronica));

        prod("Smart TV 55\" 4K OLED",
             "Panel OLED, HDR10+, Google TV integrado, Dolby Atmos, 4 puertos HDMI 2.1.",
             1099.99, 8,
             "https://images.unsplash.com/photo-1593359677879-a4bb92f4834c?w=500&q=80",
             Set.of(electronica));

        prod("Tablet iPad Air 11\"",
             "Chip M2, 256GB de almacenamiento, pantalla Liquid Retina, compatible con Apple Pencil.",
             749.99, 15,
             "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=500&q=80",
             Set.of(electronica));

        prod("Cámara Sony A7 IV",
             "Full frame 33MP, video 4K 120fps, estabilización de 5 ejes, doble ranura SD.",
             2499.99, 5,
             "https://images.unsplash.com/photo-1516035069371-29a1b244cc32?w=500&q=80",
             Set.of(electronica));

        // ## Ropa
        prod("Remera Premium Algodón",
             "100% algodón peinado, corte regular fit, disponible en 8 colores, lavado a 30°.",
             29.99, 120,
             "https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?w=500&q=80",
             Set.of(ropa));

        prod("Campera Impermeable Mountain",
             "Shell 3 capas, costuras selladas, capucha ajustable desmontable, bolsillos con cierre.",
             189.99, 30,
             "https://images.unsplash.com/photo-1551028719-00167b16eac5?w=500&q=80",
             Set.of(ropa));

        prod("Jeans Slim Fit Premium",
             "Denim elastizado 2%, corte slim, lavado stone, 5 bolsillos, tiro medio.",
             79.99, 60,
             "https://images.unsplash.com/photo-1542272604-787c3835535d?w=500&q=80",
             Set.of(ropa));

        // ## Calzado
        prod("Nike Air Max 270",
             "Unidad Air más grande en el talón, suela de goma resistente, diseño retro futurista.",
             149.99, 45,
             "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=500&q=80",
             Set.of(calzado));

        prod("Botas de cuero artesanales",
             "Cuero vacuno curtido al vegetal, suela de cuero cosida, forro interior de lana.",
             219.99, 20,
             "https://images.unsplash.com/photo-1608256246200-53e635b5b65f?w=500&q=80",
             Set.of(calzado));

        prod("Ojotas de playa ergonómicas",
             "Material EVA ultraliviano, plantilla ergonómica con arco de soporte, antideslizante.",
             24.99, 80,
             "https://images.unsplash.com/photo-1603487742131-4160ec999306?w=500&q=80",
             Set.of(calzado));

        // ## Hogar
        prod("Sofá 3 cuerpos aterciopelado",
             "Tapizado premium en terciopelo, estructura de madera de pino macizo, patas metálicas.",
             599.99, 6,
             "https://images.unsplash.com/photo-1555041469-a586c61ea9bc?w=500&q=80",
             Set.of(hogar));

        prod("Cafetera Nespresso Vertuo",
             "19 bar de presión, depósito extraíble de 1L, calienta en 25 segundos, cápsula centrifuga.",
             129.99, 35,
             "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=500&q=80",
             Set.of(hogar));

        prod("Set de sábanas 400 hilos",
             "100% algodón egipcio, incluye sábana plana, ajustable y 2 fundas de almohada.",
             89.99, 50,
             "https://images.unsplash.com/photo-1631049307264-da0ec9d70304?w=500&q=80",
             Set.of(hogar));

        // ## Deportes
        prod("Bicicleta MTB 29\" Shimano",
             "21 velocidades Shimano, frenos hidráulicos, cuadro aluminio doble suspensión.",
             549.99, 12,
             "https://images.unsplash.com/photo-1485965120184-e220f721d03e?w=500&q=80",
             Set.of(deportes));

        prod("Mat de yoga antideslizante 6mm",
             "Material TPE ecológico, 6mm de grosor, superficie antideslizante doble cara, incluye correa.",
             39.99, 75,
             "https://images.unsplash.com/photo-1506629082955-511b1aa562c8?w=500&q=80",
             Set.of(deportes));

        prod("Pelota de fútbol adidas Pro",
             "FIFA Quality Pro certificada, cuero sintético termoligado, talla 5, cámara de butilo.",
             59.99, 55,
             "https://images.unsplash.com/photo-1575361204480-aadea25e6e68?w=500&q=80",
             Set.of(deportes));

        // ## Libros
        prod("El Poder del Ahora",
             "Eckhart Tolle. Una guía para la iluminación espiritual. Más de 3 millones de copias vendidas.",
             18.99, 90,
             "https://images.unsplash.com/photo-1481627834876-b7833e8f5570?w=500&q=80",
             Set.of(libros));

        prod("Sapiens: De animales a dioses",
             "Yuval Noah Harari. Historia breve de la humanidad. Traducción al español de Joandomènec Ros.",
             22.99, 70,
             "https://images.unsplash.com/photo-1544947950-fa07a98d237f?w=500&q=80",
             Set.of(libros));

        prod("El inversor inteligente",
             "Benjamin Graham. La biblia del value investing. Prólogo de Warren Buffett.",
             27.99, 45,
             "https://images.unsplash.com/photo-1592496431122-2349e0fbc666?w=500&q=80",
             Set.of(libros));

        // ## Juguetes
        prod("LEGO Technic Ferrari 488 GTE",
             "1677 piezas, motor V8 funcional, suspensión delantera y trasera, escala 1:8.",
             199.99, 18,
             "https://images.unsplash.com/photo-1558060370-d644479cb6f7?w=500&q=80",
             Set.of(juguetes));

        prod("Control PS5 DualSense",
             "Gatillos adaptativos, retroalimentación háptica avanzada, micrófono incorporado.",
             89.99, 28,
             "https://images.unsplash.com/photo-1493711662062-fa541adb3fc8?w=500&q=80",
             Set.of(juguetes));

        // ## Belleza
        prod("Perfume Chanel Bleu 100ml EDP",
             "Fragancia amaderada aromática, notas de cedro, sándalo y ámbar. Larga duración 8+ horas.",
             159.99, 22,
             "https://images.unsplash.com/photo-1541643600914-78b084683702?w=500&q=80",
             Set.of(belleza));

        prod("Paleta de sombras 48 colores",
             "48 tonos mate, shimmer y glitter, altamente pigmentados, larga duración todo el día.",
             49.99, 40,
             "https://images.unsplash.com/photo-1596462502278-27bfdc403348?w=500&q=80",
             Set.of(belleza));

        prod("Set skincare hidratación profunda",
             "Sérum vitamina C, crema hidratante y contorno de ojos. Para todo tipo de piel.",
             79.99, 35,
             "https://images.unsplash.com/photo-1556228720-195a672e8a03?w=500&q=80",
             Set.of(belleza));

        System.out.println(">>> ¡Datos iniciales cargados correctamente! " + productosRepository.count() + " productos.");
    }

    // ## Helper: crea y guarda una categoría nueva en la DB
    private Category save(String name) {
        Category c = new Category();
        c.setName(name);
        return categoriaRepository.save(c);
    }

    // ## Helper: crea y guarda un producto nuevo con sus categorías
    private void prod(String name, String desc, double price, int stock, String imageUrl, Set<Category> cats) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(BigDecimal.valueOf(price));
        p.setStock(stock);
        p.setImageUrl(imageUrl);
        p.setCategories(cats);
        productosRepository.save(p);
    }
}
