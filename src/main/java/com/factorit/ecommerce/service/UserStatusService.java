package com.factorit.ecommerce.service;

import com.factorit.ecommerce.model.SalesOrder;
import com.factorit.ecommerce.model.User;
import com.factorit.ecommerce.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class UserStatusService {

    private final UserRepository userRepository;

    // Calcula el monto total gastado por el usuario en el último mes y lo actualiza en la base de datos
    public double calculateTotalSpentLastMonth(User user, List<SalesOrder> salesOrders, LocalDate today) {
        double totalSpentLastMonth = 0;
        System.out.println("Today: " + today);
        for (SalesOrder salesOrder : salesOrders) {
            if (salesOrder.getDate().isAfter(today.minusMonths(1))) {
                totalSpentLastMonth += salesOrder.getTotalPrice();
            }
        }
        System.out.println("Total spent last month: " + totalSpentLastMonth);
        user.setTotalSpentLastMonth(totalSpentLastMonth);
        userRepository.save(user);
        System.out.println("User updated: " + user);
        return totalSpentLastMonth;
    }

    // Calcula si el usuario debe pasar a ser VIP o dejar de serlo
    // Según el monto total gastado en el último mes
    // Si el usuario ya es VIP, se verifica si debe dejar de serlo
    // Luego actualizamos el usuario en la base de datos
    public void calculateVipStatus(User user, double totalSpentLastMonth, LocalDate today) {
        // Si el monto total gastado en el último mes supera los 10000 y el usuario no es admin, se convierte en VIP
        if (totalSpentLastMonth > 10000 && user.getRole() != com.factorit.ecommerce.model.Role.ADMIN) {
            user.setRole(com.factorit.ecommerce.model.Role.VIP);
            user.setDateWhenBecameVIP(today);
            userRepository.save(user);
        } else {
            if (user.getRole() == com.factorit.ecommerce.model.Role.VIP) {
                user.setRole(com.factorit.ecommerce.model.Role.USER);
                user.setDateWhenLostVIP(today);
                userRepository.save(user);
            }
        }

    }
}
