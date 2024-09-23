package com.factorit.ecommerce.service;

import com.factorit.ecommerce.model.Role;
import com.factorit.ecommerce.model.User;
import com.factorit.ecommerce.repository.SalesOrderRepository;
import com.factorit.ecommerce.repository.UserRepository;
import com.factorit.ecommerce.security.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final UserStatusService userStatusService;
    private final SalesOrderRepository salesOrderRepository;

    // Método para registrar un usuario
    public User registerUser(User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }

        user.setRole(Role.USER);
        user.setTotalSpentLastMonth(0.0);
        user.setDateWhenBecameVIP(null);
        user.setDateWhenLostVIP(null);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // Método para iniciar sesión
    public String login(User loginRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(), loginRequest.getPassword()
                    )
            );

            // Verificamos si el usuario debe dejar de ser VIP
            Optional<User> user = userRepository.findByUsername(loginRequest.getUsername());
            if (user.isPresent()) {
                User userObj = user.get();
                if (userObj.getRole() == Role.VIP) {
                    double totalSpent = userStatusService.calculateTotalSpentLastMonth(userObj, salesOrderRepository.findByUser(userObj), LocalDate.now());
                    userStatusService.calculateVipStatus(userObj, totalSpent, LocalDate.now());
                    userRepository.save(userObj);
                }
            }

            return jwtTokenUtil.generateToken(loginRequest.getUsername());
        } catch (AuthenticationException e) {
            throw new IllegalArgumentException("Las credenciales no coinciden o no existen");
        }
    }

}
