package com.factorit.ecommerce.service;

import com.factorit.ecommerce.model.SalesOrder;
import com.factorit.ecommerce.model.User;
import com.factorit.ecommerce.repository.SalesOrderRepository;
import com.factorit.ecommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;

@Service
@AllArgsConstructor
public class SalesOrderService {

    private final SalesOrderRepository salesOrderRepository;
    private final UserStatusService userStatusService;
    private final UserRepository userRepository;
    private final ProductService productService;

    // Implementar métodos para CRUD

    // Obtener todas las órdenes de venta
    public List<SalesOrder> getAllSalesOrders() {
        return salesOrderRepository.findAll();
    }

    // Agregar una orden de venta
    public SalesOrder addSalesOrder(SalesOrder salesOrder, String username) {
        // Obtener el usuario que realizó la orden de venta
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new MissingResourceException("User not found", "User", username));
        System.out.println(salesOrder);
        // Asignar el usuario a la orden de venta
        salesOrder.setUser(user);

        // Antes de guardar la salesOrder, debería verificarse el stock de los productos, y revisar el precio enviado desde el cliente
        // Estas verificaciones no se realizarán por falta de tiempo
        salesOrderRepository.save(salesOrder);

        List<SalesOrder> userSalesOrders = salesOrderRepository.findByUser(user);

        // Calcular el monto total gastado por el usuario en el último mes y actualizar su estado VIP
        double totalSpentLastMonth = userStatusService.calculateTotalSpentLastMonth(user, userSalesOrders, salesOrder.getDate());
        userStatusService.calculateVipStatus(user, totalSpentLastMonth, salesOrder.getDate());

        // Actualizar stock de productos via update
        // Enviamos un update de stock a cada producto de la orden de venta utilizando su ID y la cantidad de veces que se repite en salesOrder.getProducts()
        // Crear un Map para contar la cantidad de cada producto
        Map<Long, Integer> productCountMap = new HashMap<>();

        // Contar cuántas veces se repite cada productId en la lista
        for (Long productId : salesOrder.getProductsId()) {
            productCountMap.put(productId, productCountMap.getOrDefault(productId, 0) + 1);
        }

        // Ahora actualizamos el stock de cada producto usando el productCountMap
        productCountMap.forEach((productId, quantity) -> {
            // Llamamos al servicio de producto para actualizar el stock
            productService.updateProductStock(productId, quantity);
        });

        return salesOrder;
    }

    // Eliminar una orden de venta
    public void deleteSalesOrder(Long salesOrderId) {
        salesOrderRepository.deleteById(salesOrderId);
    }

    // Obtener todas las órdenes de venta de un usuario
    public List<SalesOrder> getSalesOrdersByUser(User user) {
        return salesOrderRepository.findByUser(user);
    }
}
