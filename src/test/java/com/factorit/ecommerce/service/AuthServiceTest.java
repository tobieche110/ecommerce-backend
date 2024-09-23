package com.factorit.ecommerce.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.factorit.ecommerce.model.Role;
import com.factorit.ecommerce.model.User;
import com.factorit.ecommerce.repository.SalesOrderRepository;
import com.factorit.ecommerce.repository.UserRepository;
import com.factorit.ecommerce.security.JwtTokenUtil;
import com.factorit.ecommerce.service.AuthService;
import com.factorit.ecommerce.service.UserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private UserStatusService userStatusService;

    @Mock
    private SalesOrderRepository salesOrderRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para el registro de usuario
    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");

        User savedUser = authService.registerUser(user);

        assertNotNull(savedUser);
        assertEquals("testUser", savedUser.getUsername());
        assertEquals(Role.USER, savedUser.getRole());
        assertEquals("encodedPassword", savedUser.getPassword());

        verify(userRepository).findByUsername("testUser");
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testRegisterUser_ThrowsExceptionWhenUserExists() {
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(new User()));

        User user = new User();
        user.setUsername("existingUser");

        assertThrows(IllegalArgumentException.class, () -> authService.registerUser(user));

        verify(userRepository).findByUsername("existingUser");
        verify(userRepository, never()).save(any(User.class));
    }

    // Test para el inicio de sesión
    @Test
    void testLogin_Success() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("password");

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(jwtTokenUtil.generateToken("testUser")).thenReturn("token123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("testUser", "password"));

        String token = authService.login(user);

        assertNotNull(token);
        assertEquals("token123", token);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(userRepository).findByUsername("testUser");
        verify(jwtTokenUtil).generateToken("testUser");
    }

    @Test
    void testLogin_ThrowsExceptionWhenAuthenticationFails() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("wrongPassword");

        doThrow(new BadCredentialsException("Credenciales incorrectas"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        assertThrows(IllegalArgumentException.class, () -> authService.login(user));

        verify(jwtTokenUtil, never()).generateToken(anyString());
    }
}
