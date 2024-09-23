package com.factorit.ecommerce.service;

import com.factorit.ecommerce.model.Product;
import com.factorit.ecommerce.repository.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.MissingResourceException;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    // Devuelve todos los productos
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    // Agrega el producto a la base de datos y lo devuelve
    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    // Actualiza el producto en la base de datos y lo devuelve
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    // Elimina el producto de la base de datos
    public void deleteProduct(Long productId) {
        productRepository.deleteById(productId);
    }

    public List<Product> getProductsByIds(List<Long> productIds) {
        return productRepository.findByIdIn(productIds);
    }

    // Actualiza el stock de un producto en la base de datos
    public void updateProductStock(Long productId, Integer quantitySold) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new MissingResourceException("Product not found", "Product", productId.toString()));

        // Reducir el stock en función de la cantidad vendida
        product.setStock(product.getStock() - quantitySold);

        // Guardar los cambios en el producto
        productRepository.save(product);
    }
}
