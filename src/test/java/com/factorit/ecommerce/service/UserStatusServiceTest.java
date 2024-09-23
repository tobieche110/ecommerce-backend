package com.factorit.ecommerce.service;

import com.factorit.ecommerce.model.Role;
import com.factorit.ecommerce.model.SalesOrder;
import com.factorit.ecommerce.model.User;
import com.factorit.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserStatusServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserStatusService userStatusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para calculateTotalSpentLastMonth()
    @Test
    void testCalculateTotalSpentLastMonth() {
        // Mock data
        User user = new User();
        LocalDate today = LocalDate.now();

        SalesOrder order1 = new SalesOrder();
        order1.setDate(today.minusDays(5));
        order1.setTotalPrice(5000);

        SalesOrder order2 = new SalesOrder();
        order2.setDate(today.minusDays(15));
        order2.setTotalPrice(3000);

        SalesOrder order3 = new SalesOrder();
        order3.setDate(today.minusDays(40)); // Fuera del rango del último mes
        order3.setTotalPrice(4000);

        List<SalesOrder> salesOrders = Arrays.asList(order1, order2, order3);

        // Call service
        double totalSpent = userStatusService.calculateTotalSpentLastMonth(user, salesOrders, today);

        // Assert
        assertEquals(8000, totalSpent);
        assertEquals(8000, user.getTotalSpentLastMonth());
        verify(userRepository).save(user);
    }

    // Test para calculateVipStatus() cuando el usuario pasa a ser VIP
    @Test
    void testCalculateVipStatus_UserBecomesVIP() {
        // Mock data
        User user = new User();
        user.setRole(Role.USER); // El usuario no es VIP inicialmente
        double totalSpentLastMonth = 15000; // Monto que excede los 10000
        LocalDate today = LocalDate.now();

        // Call service
        userStatusService.calculateVipStatus(user, totalSpentLastMonth, today);

        // Assert
        assertEquals(Role.VIP, user.getRole());
        assertEquals(today, user.getDateWhenBecameVIP());
        verify(userRepository).save(user);
    }

    // Test para calculateVipStatus() cuando el usuario deja de ser VIP
    @Test
    void testCalculateVipStatus_UserLosesVIPStatus() {
        // Mock data
        User user = new User();
        user.setRole(Role.VIP); // El usuario es VIP inicialmente
        double totalSpentLastMonth = 5000; // Monto por debajo de los 10000
        LocalDate today = LocalDate.now();

        // Call service
        userStatusService.calculateVipStatus(user, totalSpentLastMonth, today);

        // Assert
        assertEquals(Role.USER, user.getRole());
        assertEquals(today, user.getDateWhenLostVIP());
        verify(userRepository).save(user);
    }

    // Test para calculateVipStatus() cuando el usuario es ADMIN y no debe cambiar de estado
    @Test
    void testCalculateVipStatus_AdminDoesNotChangeStatus() {
        // Mock data
        User user = new User();
        user.setRole(Role.ADMIN); // El usuario es ADMIN
        double totalSpentLastMonth = 20000; // Monto que excede los 10000
        LocalDate today = LocalDate.now();

        // Call service
        userStatusService.calculateVipStatus(user, totalSpentLastMonth, today);

        // Assert
        assertEquals(Role.ADMIN, user.getRole()); // El rol no debe cambiar
        assertNull(user.getDateWhenBecameVIP()); // No debe haber cambio en VIP
        assertNull(user.getDateWhenLostVIP()); // No debe haber cambio en VIP
        verify(userRepository, never()).save(user); // No debe guardarse el usuario
    }
}
