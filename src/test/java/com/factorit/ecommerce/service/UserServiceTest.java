package com.factorit.ecommerce.service;

import com.factorit.ecommerce.model.Role;
import com.factorit.ecommerce.model.User;
import com.factorit.ecommerce.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para getAllUsers()
    @Test
    void testGetAllUsers() {
        // Mock data
        User user1 = new User();
        User user2 = new User();
        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Call service
        List<User> users = userService.getAllUsers();

        // Assert
        assertNotNull(users);
        assertEquals(2, users.size());
        verify(userRepository).findAll();
    }

    // Test para getVipUsers()
    @Test
    void testGetVipUsers() {
        // Mock data
        User vipUser1 = new User();
        User vipUser2 = new User();
        when(userRepository.findByRole(Role.VIP)).thenReturn(Arrays.asList(vipUser1, vipUser2));

        // Call service
        List<User> vipUsers = userService.getVipUsers();

        // Assert
        assertNotNull(vipUsers);
        assertEquals(2, vipUsers.size());
        verify(userRepository).findByRole(Role.VIP);
    }

    // Test para getVipUsersByMonth()
    @Test
    void testGetVipUsersByMonth() {
        // Mock data
        int year = 2024;
        int month = 9;
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        User vipUser1 = new User();
        User vipUser2 = new User();
        when(userRepository.findByDateWhenBecameVIPBetween(startOfMonth, endOfMonth)).thenReturn(Arrays.asList(vipUser1, vipUser2));

        // Call service
        List<User> vipUsersByMonth = userService.getVipUsersByMonth(year, month);

        // Assert
        assertNotNull(vipUsersByMonth);
        assertEquals(2, vipUsersByMonth.size());
        verify(userRepository).findByDateWhenBecameVIPBetween(startOfMonth, endOfMonth);
    }

    // Test para getLostVipUsersByMonth()
    @Test
    void testGetLostVipUsersByMonth() {
        // Mock data
        int year = 2024;
        int month = 9;
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        User lostVipUser1 = new User();
        User lostVipUser2 = new User();
        when(userRepository.findByDateWhenLostVIPBetween(startOfMonth, endOfMonth)).thenReturn(Arrays.asList(lostVipUser1, lostVipUser2));

        // Call service
        List<User> lostVipUsersByMonth = userService.getLostVipUsersByMonth(year, month);

        // Assert
        assertNotNull(lostVipUsersByMonth);
        assertEquals(2, lostVipUsersByMonth.size());
        verify(userRepository).findByDateWhenLostVIPBetween(startOfMonth, endOfMonth);
    }

    // Test para loadUserByUsername() cuando el usuario es encontrado
    @Test
    void testLoadUserByUsername_UserFound() {
        // Mock data
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setPassword("password");
        user.setRole(Role.USER);

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Call service
        UserDetails userDetails = userService.loadUserByUsername(username);

        // Assert
        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password", userDetails.getPassword());
        verify(userRepository).findByUsername(username);
    }

    // Test para loadUserByUsername() cuando el usuario no es encontrado
    @Test
    void testLoadUserByUsername_UserNotFound() {
        // Mock data
        String username = "nonexistentuser";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Call service and expect exception
        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () ->
                userService.loadUserByUsername(username));

        // Assert
        assertEquals(username, exception.getMessage());
        verify(userRepository).findByUsername(username);
    }
}
