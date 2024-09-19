package com.factorit.ecommerce.controller;

import com.factorit.ecommerce.model.User;
import com.factorit.ecommerce.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    // Consultar lista de usuarios
    @GetMapping("/admin/user/all")
    public ResponseEntity<List<User>> getAllUsers() {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList()); // Devuelve una lista vacía en caso de error
        }
    }

    // Consultar lista de usuarios VIP
    @GetMapping("/admin/user/all/vip")
    public ResponseEntity<List<User>> getVipUsers() {
        try {
            return ResponseEntity.ok(userService.getVipUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList()); // Devuelve una lista vacía en caso de error
        }
    }

    // Consultar lista de usuarios que pasaron a ser VIP en un determinado mes y año
    @GetMapping("/admin/user/vip/got/{year}/{month}")
    public ResponseEntity<List<User>> getVipUsersByMonth(@PathVariable int year, @PathVariable int month) {
        try {
            return ResponseEntity.ok(userService.getVipUsersByMonth(year, month));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList()); // Devuelve una lista vacía en caso de error
        }
    }

    // Consultar lista de usuarios que dejaron de ser VIP en un determinado mes y año
    @GetMapping("/admin/user/vip/lost/{year}/{month}")
    public ResponseEntity<List<User>> getLostVipUsersByMonth(@PathVariable int year, @PathVariable int month) {
        try {
            return ResponseEntity.ok(userService.getLostVipUsersByMonth(year, month));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.emptyList()); // Devuelve una lista vacía en caso de error
        }
    }
}
