package com.factorit.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "product", schema = "public", catalog = "ecommerce")
@Data
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private double price;
    private Integer stock;

    // Para imágenes, se implementará después

}
