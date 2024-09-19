package com.factorit.ecommerce.service;

import com.factorit.ecommerce.model.SalesOrder;
import com.factorit.ecommerce.model.User;
import com.factorit.ecommerce.repository.SalesOrderRepository;
import com.factorit.ecommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.MissingResourceException;

@Service
@AllArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final UserRepository userRepository;

    // Implementar métodos para CRUD

    // Obtener todas las órdenes de venta
    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    // Agregar una orden de venta
    public SalesOrder addSalesOrder(SalesOrder salesOrder) {
        User user = userRepository.findById(salesOrder.getUser().getId()).orElseThrow(() -> new MissingResourceException("User not found", "User", salesOrder.getUser().getId().toString()));
        salesOrder.setUser(user);
        return salesOrderRepository.save(salesOrder);
    }

    // Eliminar una orden de venta
    public void deleteSalesOrder(Long salesOrderId) {
        salesOrderRepository.deleteById(salesOrderId);
    }
}
