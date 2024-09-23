package com.factorit.ecommerce.service;

import com.factorit.ecommerce.model.Discount;
import com.factorit.ecommerce.model.DiscountedPrice;
import com.factorit.ecommerce.model.ProductRequest;
import com.factorit.ecommerce.repository.DiscountRepository;
import com.factorit.ecommerce.service.DiscountService;
import com.factorit.ecommerce.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DiscountServiceTest {

    @Mock
    private DiscountRepository discountRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private DiscountService discountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Tests go here
    @Test
    void testGetAllDiscounts() {
        // Mock data
        Discount discount1 = new Discount();
        Discount discount2 = new Discount();

        when(discountRepository.findAll()).thenReturn(Arrays.asList(discount1, discount2));

        // Call the service
        List<Discount> discounts = discountService.getAllDiscounts();

        // Assert
        assertNotNull(discounts);
        assertEquals(2, discounts.size());
        verify(discountRepository).findAll();
    }

    @Test
    void testAddDiscount() {
        // Mock data
        Discount discount = new Discount();
        discount.setDiscountAmount(10.0);

        when(discountRepository.save(any(Discount.class))).thenReturn(discount);

        // Call the service
        Object savedDiscount = discountService.addDiscount(discount);

        // Assert
        assertNotNull(savedDiscount);
        assertEquals(10.0, ((Discount) savedDiscount).getDiscountAmount());
        verify(discountRepository).save(discount);
    }

    @Test
    void testGetBestDiscount() {
        // Mock data
        ProductRequest product1 = new ProductRequest();
        product1.setPrice("100.0");
        product1.setQuantity(2);
        product1.setDate(LocalDate.now());

        ProductRequest product2 = new ProductRequest();
        product2.setPrice("50.0");
        product2.setQuantity(1);
        product2.setDate(LocalDate.now());

        List<ProductRequest> products = Arrays.asList(product1, product2);

        Discount discount1 = new Discount();
        discount1.setDiscountPercentage(10);
        discount1.setRoleForDiscount(null);
        discount1.setProductCountMax(0);
        discount1.setProductCountMin(0);
        discount1.setProductCountMin(2);

        Discount discount2 = new Discount();
        discount2.setFreeCheapestProductCount(1);
        discount2.setRoleForDiscount(null);
        discount2.setProductCountMax(0);
        discount2.setProductCountMin(0);
        discount2.setProductCountMin(2);

        when(discountRepository.findAll()).thenReturn(Arrays.asList(discount1, discount2));

        // Call the service
        DiscountedPrice bestDiscount = (DiscountedPrice) discountService.getBestDiscount("testUser", "USER", products);

        // Assert
        assertNotNull(bestDiscount);
        assertEquals(discount2, bestDiscount.getDiscount()); // Aseguramos que el descuento con productos gratis se aplique
        verify(discountRepository).findAll();
    }

    @Test
    void testGetBestDiscountWithFreeProducts() {
        // Mock data
        ProductRequest product1 = new ProductRequest();
        product1.setPrice("200.0");
        product1.setQuantity(1);
        product1.setDate(LocalDate.now());

        ProductRequest product2 = new ProductRequest();
        product2.setPrice("100.0");
        product2.setQuantity(1);
        product2.setDate(LocalDate.now());

        List<ProductRequest> products = Arrays.asList(product1, product2);

        Discount discount = new Discount();
        discount.setFreeCheapestProductCount(1); // Producto más barato gratis
        discount.setDiscountPercentage(0);
        discount.setProductCountMax(0);
        discount.setProductCountMin(0);
        discount.setRoleForDiscount(null);

        when(discountRepository.findAll()).thenReturn(Collections.singletonList(discount));

        // Call the service
        DiscountedPrice bestDiscount = (DiscountedPrice) discountService.getBestDiscount("testUser", "USER", products);

        // Assert
        assertNotNull(bestDiscount);
        assertEquals(200.0, bestDiscount.getTotal()); // Solo pagas por el producto más caro
        verify(discountRepository).findAll();
    }

    @Test
    void testGetBestDiscountWithValidDates() {
        // Mock data
        ProductRequest product = new ProductRequest();
        product.setPrice("100.0");
        product.setQuantity(1);
        product.setDate(LocalDate.now());

        Discount discount = new Discount();
        discount.setDiscountPercentage(20);
        discount.setProductCountMax(0);
        discount.setProductCountMin(0);
        discount.setValidDates(Collections.singletonList(LocalDate.now()));

        when(discountRepository.findAll()).thenReturn(Collections.singletonList(discount));

        // Call the service
        DiscountedPrice bestDiscount = (DiscountedPrice) discountService.getBestDiscount("testUser", "USER", Collections.singletonList(product));

        // Assert
        assertNotNull(bestDiscount);
        assertEquals(80.0, bestDiscount.getTotal()); // Se aplicó el descuento del 20%
        verify(discountRepository).findAll();
    }


}
