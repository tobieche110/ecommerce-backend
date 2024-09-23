package com.factorit.ecommerce.service;

import com.factorit.ecommerce.model.SalesOrder;
import com.factorit.ecommerce.model.User;
import com.factorit.ecommerce.repository.SalesOrderRepository;
import com.factorit.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalesOrderServiceTest {

    @Mock
    private SalesOrderRepository salesOrderRepository;

    @Mock
    private UserStatusService userStatusService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductService productService;

    @InjectMocks
    private SalesOrderService salesOrderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para getAllSalesOrders()
    @Test
    void testGetAllSalesOrders() {
        // Mock data
        SalesOrder salesOrder1 = new SalesOrder();
        SalesOrder salesOrder2 = new SalesOrder();
        when(salesOrderRepository.findAll()).thenReturn(Arrays.asList(salesOrder1, salesOrder2));

        // Call service
        List<SalesOrder> salesOrders = salesOrderService.getAllSalesOrders();

        // Assert
        assertNotNull(salesOrders);
        assertEquals(2, salesOrders.size());
        verify(salesOrderRepository).findAll();
    }

    // Test para addSalesOrder()
    @Test
    void testAddSalesOrder() {
        // Mock data
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setProductsId(Arrays.asList(1L, 2L));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(salesOrderRepository.save(salesOrder)).thenReturn(salesOrder);

        // Call service
        SalesOrder addedSalesOrder = salesOrderService.addSalesOrder(salesOrder, username);

        // Assert
        assertNotNull(addedSalesOrder);
        assertEquals(user, addedSalesOrder.getUser());
        verify(salesOrderRepository).save(salesOrder);
        verify(productService, times(2)).updateProductStock(anyLong(), anyInt());
    }

    // Test para addSalesOrder() cuando el usuario no es encontrado
    @Test
    void testAddSalesOrder_UserNotFound() {
        // Mock data
        String username = "nonexistentuser";
        SalesOrder salesOrder = new SalesOrder();

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Call service and expect exception
        MissingResourceException exception = assertThrows(MissingResourceException.class, () ->
                salesOrderService.addSalesOrder(salesOrder, username));

        // Assert
        assertEquals("User not found", exception.getMessage());
    }

    // Test para deleteSalesOrder()
    @Test
    void testDeleteSalesOrder() {
        // Mock data
        Long salesOrderId = 1L;

        // Call service
        salesOrderService.deleteSalesOrder(salesOrderId);

        // Verify repository method called
        verify(salesOrderRepository).deleteById(salesOrderId);
    }

    // Test para getSalesOrdersByUser()
    @Test
    void testGetSalesOrdersByUser() {
        // Mock data
        User user = new User();
        SalesOrder salesOrder1 = new SalesOrder();
        SalesOrder salesOrder2 = new SalesOrder();
        when(salesOrderRepository.findByUser(user)).thenReturn(Arrays.asList(salesOrder1, salesOrder2));

        // Call service
        List<SalesOrder> salesOrders = salesOrderService.getSalesOrdersByUser(user);

        // Assert
        assertNotNull(salesOrders);
        assertEquals(2, salesOrders.size());
        verify(salesOrderRepository).findByUser(user);
    }

    // Test para verificar la actualización del stock de productos en addSalesOrder()
    @Test
    void testAddSalesOrder_StockUpdate() {
        // Mock data
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setProductsId(Arrays.asList(1L, 2L, 1L)); // 2 productos con el mismo ID

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(salesOrderRepository.save(salesOrder)).thenReturn(salesOrder);

        // Call service
        salesOrderService.addSalesOrder(salesOrder, username);

        // Verify that updateProductStock is called the correct number of times
        verify(productService).updateProductStock(1L, 2);  // Producto 1 aparece 2 veces
        verify(productService).updateProductStock(2L, 1);  // Producto 2 aparece 1 vez
    }

    // Test para verificar que calculateVipStatus sea llamado en addSalesOrder()
    @Test
    void testAddSalesOrder_VipStatusUpdate() {
        // Mock data
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setProductsId(Arrays.asList(1L, 2L));

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));
        when(salesOrderRepository.save(salesOrder)).thenReturn(salesOrder);
        when(salesOrderRepository.findByUser(user)).thenReturn(Arrays.asList(salesOrder));

        // Call service
        salesOrderService.addSalesOrder(salesOrder, username);

        // Verify that calculateVipStatus is called
        verify(userStatusService).calculateVipStatus(any(User.class), anyDouble(), any());
    }
}
