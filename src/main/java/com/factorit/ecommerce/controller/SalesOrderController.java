package com.factorit.ecommerce.controller;

import com.factorit.ecommerce.model.SalesOrder;
import com.factorit.ecommerce.service.SalesOrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class SalesOrderController {

    private final SalesOrderService salesOrderService;

    // Implementar métodos para CRUD
    // Obtener todas las órdenes de venta
    @GetMapping("admin/sales-order/all")
    public ResponseEntity<List<SalesOrder>> getAllSalesOrders() {
        try {
            return ResponseEntity.ok(salesOrderService.getAllSalesOrders());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList()); // Devuelve una lista vacía en caso de error
        }
    }

    // Agregar una orden de venta
    @PostMapping("/sales-order/add")
    public ResponseEntity<SalesOrder> addSalesOrder(@RequestBody SalesOrder salesOrder) {
        try {
            System.out.println(salesOrder);
            return ResponseEntity.ok(salesOrderService.addSalesOrder(salesOrder));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    // Eliminar una orden de venta
    @DeleteMapping("/admin/sales-order/delete/{salesOrderId}")
    public ResponseEntity<String> deleteSalesOrder(@PathVariable Long salesOrderId) {
        try {
            salesOrderService.deleteSalesOrder(salesOrderId);
            return ResponseEntity.ok("Sales order deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error deleting sales order: " + e.getMessage());
        }
    }
}
