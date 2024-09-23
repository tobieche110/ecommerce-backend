package com.factorit.ecommerce.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "sales_orders", schema = "public", catalog = "ecommerce")
@Data
public class SalesOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ElementCollection()
    @CollectionTable(name = "sales_order_products", schema = "public", catalog = "ecommerce", joinColumns = @JoinColumn(name = "sales_order_id"))
    private List<Long> productsId;

    private double totalPrice;

    private LocalDate date;
}
