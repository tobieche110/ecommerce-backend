package com.factorit.ecommerce.service;

import com.factorit.ecommerce.model.Discount;
import com.factorit.ecommerce.model.DiscountedPrice;
import com.factorit.ecommerce.model.ProductRequest;
import com.factorit.ecommerce.repository.DiscountRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final ProductService productService;

    // Get all discounts
    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    // Create a discount
    public Object addDiscount(Discount discount) {
        return discountRepository.save(discount);
    }

    // Get the best discount for the user
    public Object getBestDiscount(String username, String role, List<ProductRequest> products) {
        // Calcular total de productos en el carrito
        int totalProducts = products.stream().mapToInt(ProductRequest::getQuantity).sum();

        // Obtener descuentos
        List<Discount> discounts = getAllDiscounts();
        System.out.println("role: " + role);
        // Filtrar descuentos aplicables para el usuario a partir de su rol
        List<Discount> applicableDiscounts = discounts.stream()
                .filter(discount -> discount.getRoleForDiscount() == null ||
                        (role != null &&
                                role.equals(discount.getRoleForDiscount().toString())))
                .collect(Collectors.toList());
        System.out.println("applicableDiscounts: " + applicableDiscounts);

        // Filtrar descuentos por total de productos
        List<Discount> applicableDiscountsByProductCount = applicableDiscounts.stream()
                .filter(discount -> (discount.getProductCountMin() == 0 ||
                        discount.getProductCountMin() <= totalProducts) &&
                        (discount.getProductCountMax() == 0 ||
                                discount.getProductCountMax() >= totalProducts))
                .collect(Collectors.toList());

        // Calcular precios con descuentos
        List<DiscountedPrice> totalPricesWithDiscounts = applicableDiscountsByProductCount.stream()
                .map(discount -> {
                    double total = calculateTotalPriceWithoutDiscount(products);

                    // Aplicar productos gratis
                    if (discount.getFreeCheapestProductCount() > 0) {
                        // Obtener los productos más baratos
                        List<ProductRequest> cheapestProducts = products.stream()
                                // Ordenar por precio de menor a mayor
                                .sorted(Comparator.comparingDouble(product -> Double.parseDouble(product.getPrice())))
                                // Tomar los productos más baratos hasta el límite de productos gratis getFreeCheapestProductCount()
                                .limit(discount.getFreeCheapestProductCount())
                                .collect(Collectors.toList());
                        // Calcular el total de los productos más baratos
                        double cheapestProductsTotal = cheapestProducts.stream()
                                // Calcular el precio total de cada producto
                                .mapToDouble(product -> Double.parseDouble(product.getPrice()))
                                .sum();
                        total -= cheapestProductsTotal; // Resta los productos más baratos
                    }

                    // Restar descuento porcentual
                    if (discount.getDiscountPercentage() > 0) {
                        total -= total * (discount.getDiscountPercentage() / 100);
                    }

                    // Restar monto de descuento
                    if (discount.getDiscountAmount() > 0) {
                        total -= discount.getDiscountAmount();
                    }

                    // Devolvemos el precio total con descuento y el objeto descuento aplicado
                    return new DiscountedPrice(discount, total);
                })
                .collect(Collectors.toList());

        // Filtrar descuentos válidos por fecha
        LocalDate today = products.get(0).getDate();
        List<DiscountedPrice> validDateDiscounts = totalPricesWithDiscounts.stream()
                .filter(dp -> dp.getDiscount().getValidDates() == null || dp.getDiscount().getValidDates().isEmpty() ||
                        dp.getDiscount().getValidDates().contains(today))
                .collect(Collectors.toList());

        // Encontrar el máximo descuento
        DiscountedPrice maxDiscount = validDateDiscounts.stream()
                .min(Comparator.comparingDouble(DiscountedPrice::getTotal))
                .orElse(null);

        return maxDiscount;
    }

    // Método para calcular el precio total sin descuentos
    private double calculateTotalPriceWithoutDiscount(List<ProductRequest> products) {
        return products.stream()
                .mapToDouble(product -> Double.parseDouble(product.getPrice()) * product.getQuantity())
                .sum();
    }

}
