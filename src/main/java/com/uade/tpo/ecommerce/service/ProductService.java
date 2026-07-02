package com.uade.tpo.ecommerce.service;

// ## SERVICIO — Productos
// ##
// ## Lógica de negocio para el CRUD de productos del catálogo.
// ## Es llamado por ProductController.
// ##
// ## Patrón DTO (Data Transfer Object):
// ##   - Entrada: ProductRequest  → lo que el cliente manda (sin id, con categoryIds)
// ##   - Salida:  ProductResponse → lo que devolvemos (con id y categorías como objetos)
// ##   - Entidad: Product         → lo que guardamos en la DB (nunca se expone directo)
// ##
// ## Esto desacopla la API de la estructura interna de la base de datos:
// ##   si cambia el modelo, la API puede mantenerse estable.

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uade.tpo.ecommerce.dto.CategoryResponse;
import com.uade.tpo.ecommerce.dto.ProductRequest;
import com.uade.tpo.ecommerce.dto.ProductResponse;
import com.uade.tpo.ecommerce.exception.CategoriaNotFoundException;
import com.uade.tpo.ecommerce.exception.ProductoNotFoundException;
import com.uade.tpo.ecommerce.model.Category;
import com.uade.tpo.ecommerce.model.Product;
import com.uade.tpo.ecommerce.repository.ICategoriaRepository;
import com.uade.tpo.ecommerce.repository.IProductosRepository;

@Service
public class ProductService implements IProductService {

    @Autowired
    private IProductosRepository productRepository;

    @Autowired
    private ICategoriaRepository categoriaRepository;

    // ## Convierte una entidad Product a un DTO ProductResponse para enviar al cliente
    // ## Mapea cada Category a CategoryResponse (solo id y name, sin los productos)
    private ProductResponse toResponse(Product product) {
        Set<CategoryResponse> categorias = product.getCategories().stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName()))
                .collect(Collectors.toSet());
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getImageUrl(),
                categorias);
    }

    // ## Convierte un Set de IDs de categorías a entidades Category reales de la DB
    // ## Lanza CategoriaNotFoundException si algún ID no existe
    private Set<Category> resolveCategories(Set<Long> categoryIds) {
        if (categoryIds == null || categoryIds.isEmpty()) {
            return new HashSet<>();
        }
        Set<Category> categories = new HashSet<>();
        for (Long catId : categoryIds) {
            Category cat = categoriaRepository.findById(catId)
                    .orElseThrow(() -> new CategoriaNotFoundException(catId));
            categories.add(cat);
        }
        return categories;
    }

    // ## Lista todos los productos en el orden en que están en la DB
    @Override
    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ## Lista todos los productos ordenados alfabéticamente por nombre (A-Z)
    // ## Se usa cuando el frontend envía ?ordered=true
    @Override
    public List<ProductResponse> getAllProductsOrdered() {
        return productRepository.findAll().stream()
                .sorted((p1, p2) -> p1.getName().compareToIgnoreCase(p2.getName()))
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    // ## Crea un producto nuevo:
    // ##   1. Mapea el DTO a entidad Product
    // ##   2. Resuelve los IDs de categorías a entidades Category
    // ##   3. Guarda en DB y devuelve el DTO de respuesta
    @Override
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setImageUrl(request.getImageUrl());
        product.setCategories(resolveCategories(request.getCategoryIds()));
        return toResponse(productRepository.save(product));
    }

    // ## Actualiza todos los campos de un producto existente (reemplazo completo)
    // ## Lanza ProductoNotFoundException si el ID no existe
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        existing.setPrice(request.getPrice());
        existing.setStock(request.getStock());
        existing.setImageUrl(request.getImageUrl());
        existing.setCategories(resolveCategories(request.getCategoryIds()));
        return toResponse(productRepository.save(existing));
    }

    // ## Elimina un producto por ID
    // ## Verifica existencia antes de eliminar para dar un 404 descriptivo
    @Override
    public void deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ProductoNotFoundException(id);
        }
        productRepository.deleteById(id);
    }

    // ## Busca un producto por ID y lo convierte a DTO
    // ## Lanza ProductoNotFoundException si no existe (manejado por GlobalExceptionHandler → 404)
    @Override
    public ProductResponse findProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductoNotFoundException(id));
        return toResponse(product);
    }

    // ## Verifica si existe un producto con el ID dado (true/false)
    @Override
    public boolean existsById(Long id) {
        return productRepository.existsById(id);
    }
}
