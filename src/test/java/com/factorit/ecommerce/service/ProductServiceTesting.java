package com.factorit.ecommerce.service;

import com.factorit.ecommerce.model.Product;
import com.factorit.ecommerce.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para getAllProducts()
    @Test
    void testGetAllProducts() {
        // Mock data
        Product product1 = new Product();
        Product product2 = new Product();
        when(productRepository.findAll()).thenReturn(Arrays.asList(product1, product2));

        // Call service
        List<Product> products = productService.getAllProducts();

        // Assert
        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository).findAll();
    }

    // Test para addProduct()
    @Test
    void testAddProduct() {
        // Mock data
        Product product = new Product();
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Call service
        Product savedProduct = productService.addProduct(product);

        // Assert
        assertNotNull(savedProduct);
        verify(productRepository).save(product);
    }

    // Test para updateProduct()
    @Test
    void testUpdateProduct() {
        // Mock data
        Product product = new Product();
        product.setId(1L);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        // Call service
        Product updatedProduct = productService.updateProduct(product);

        // Assert
        assertNotNull(updatedProduct);
        assertEquals(1L, updatedProduct.getId());
        verify(productRepository).save(product);
    }

    // Test para deleteProduct()
    @Test
    void testDeleteProduct() {
        // Mock data
        Long productId = 1L;

        // Call service
        productService.deleteProduct(productId);

        // Assert and verify
        verify(productRepository).deleteById(productId);
    }

    // Test para getProductsByIds()
    @Test
    void testGetProductsByIds() {
        // Mock data
        Product product1 = new Product();
        product1.setId(1L);
        Product product2 = new Product();
        product2.setId(2L);

        List<Long> productIds = Arrays.asList(1L, 2L);
        when(productRepository.findByIdIn(productIds)).thenReturn(Arrays.asList(product1, product2));

        // Call service
        List<Product> products = productService.getProductsByIds(productIds);

        // Assert
        assertNotNull(products);
        assertEquals(2, products.size());
        verify(productRepository).findByIdIn(productIds);
    }

    // Test para updateProductStock()
    @Test
    void testUpdateProductStock() {
        // Mock data
        Product product = new Product();
        product.setId(1L);
        product.setStock(10);

        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        // Call service
        productService.updateProductStock(1L, 5);

        // Assert
        assertEquals(5, product.getStock());  // El stock debería haber disminuido
        verify(productRepository).save(product);
    }

    // Test para updateProductStock() cuando el producto no existe
    @Test
    void testUpdateProductStock_ProductNotFound() {
        // Mock behavior: no se encuentra el producto
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        // Call service and expect exception
        MissingResourceException exception = assertThrows(MissingResourceException.class, () ->
                productService.updateProductStock(1L, 5));

        // Assert
        assertEquals("Product not found", exception.getMessage());
    }
}
