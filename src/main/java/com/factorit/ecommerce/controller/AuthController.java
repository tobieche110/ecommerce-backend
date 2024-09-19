package com.factorit.ecommerce.controller;

import com.factorit.ecommerce.model.Role;
import com.factorit.ecommerce.model.User;
import com.factorit.ecommerce.repository.UserRepository;
import com.factorit.ecommerce.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    // Inyectamos los beans necesarios
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    // Este método recibe un objeto User con los datos de registro y retorna el usuario guardado en la base de datos
    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user) {
        try {
            // Verificamos si el usuario ya existe en la base de datos
            if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT).build();  // Retorna HTTP 409 en caso de conflicto
            }

            user.setRole(Role.USER);  // Asignamos el rol USER al usuario. Por seguridad, el rol ADMIN debe asignarse manualmente desde la base de datos
            user.setTotalSpentLastMonth(0.0);  // Inicializamos el total gastado en 0
            user.setDateWhenBecameVIP(null);  // Inicializamos la fecha en la que se convirtió en VIP en null
            user.setDateWhenLostVIP(null);  // Inicializamos la fecha en la que dejó de ser VIP en null
            user.setLastPurchase(null);  // Inicializamos la última compra en null
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println(user);
            User savedUser = userRepository.save(user);
            return ResponseEntity.ok(savedUser);  // Retorna HTTP 200 con el usuario en el cuerpo
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();  // Retorna HTTP 500 en caso de error
        }
    }

    // Este método recibe un objeto User con los datos de login y retorna un token JWT
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody User loginRequest) {
        try {
            // Autenticamos al usuario con el nombre de usuario y contraseña
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );
            String token = jwtTokenUtil.generateToken(loginRequest.getUsername());
            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.ok(response); // Retorna HTTP 200 con el token en el cuerpo
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();  // Retorna HTTP 401 en caso de error de autenticación
        }
    }
}
