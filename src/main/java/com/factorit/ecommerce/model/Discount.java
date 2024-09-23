package com.factorit.ecommerce.model;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "discount", schema = "public", catalog = "ecommerce")
@Data
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name; // Nombre del descuento

    private Integer productCountMin; // Cantidad mínima de productos para aplicar el descuento. 0 si no hay cantidad mínima.

    private Integer productCountMax; // Cantidad máxima de productos para aplicar el descuento. 0 si no hay cantidad máxima.

    @Enumerated(EnumType.STRING)
    private Role roleForDiscount; // Rol que debe tener el bonificado. Null si no hay restricción de rol.

    private double discountPercentage; // Porcentaje de descuento
    private double discountAmount; // Monto fijo de descuento

    private int freeCheapestProductCount; // Cantidad de productos mas baratos bonificados

    @ElementCollection
    @CollectionTable(name = "discount_dates", schema = "public", catalog = "ecommerce", joinColumns = @JoinColumn(name = "discount_id"))
    private List<LocalDate> validDates; // Fechas en las que aplica el descuento. Null si es valido siempre.
    // Lo ideal seria tener un rango de fechas, pero por simplicidad se usará una lista de fechas.
}
