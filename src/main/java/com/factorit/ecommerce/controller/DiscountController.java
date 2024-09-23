package com.factorit.ecommerce.controller;

import com.factorit.ecommerce.model.Discount;
import com.factorit.ecommerce.model.ProductRequest;
import com.factorit.ecommerce.service.DiscountService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class DiscountController {

    private final DiscountService discountService;

    // Get all discounts
    @GetMapping("/discount/all")
    public ResponseEntity<List<Discount>> getAllDiscounts() {
        try {
            return ResponseEntity.ok(discountService.getAllDiscounts());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList()); // Returns an empty list in case of error
        }
    }

    // Obtener descuento mas adecuado para el usuario
    @PostMapping("/discount/get/best")
    public ResponseEntity<?> getBestDiscount(@RequestBody List<ProductRequest> products) {
        // Get username from token
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        String role = authentication.getAuthorities().stream().findFirst().get().getAuthority();

        // Borramos "ROLE_" del principio del rol
        role = role.substring(5);
        try {
            return ResponseEntity.ok(discountService.getBestDiscount(username, role, products));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error getting best discount: " + e.getMessage());
        }
    }

    // Create a discount
    @PostMapping("/admin/discount/add")
    public ResponseEntity<?> addDiscount(@RequestBody Discount discount) {
        try {
            return ResponseEntity.ok(discountService.addDiscount(discount));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error adding discount: " + e.getMessage());
        }
    }
}
