package com.factorit.ecommerce.service;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.factorit.ecommerce.repository.UserRepository;
import java.time.LocalDate;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    // Consultar lista de usuarios
    public List<com.factorit.ecommerce.model.User> getAllUsers() {
        return userRepository.findAll();
    }

    // Consultar lista de usuarios VIP
    public List<com.factorit.ecommerce.model.User> getVipUsers() {
        return userRepository.findByRole(com.factorit.ecommerce.model.Role.VIP);
    }

    // Consultar lista de usuarios que pasaron a ser VIP en un determinado mes y año
    public List<com.factorit.ecommerce.model.User> getVipUsersByMonth(int year, int month) {
        // Construir el rango de fechas para el mes y año dados
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        // Filtrar los usuarios entre las fechas
        return userRepository.findByDateWhenBecameVIPBetween(startOfMonth, endOfMonth);
    }

    // Consultar lista de usuarios que dejaron de ser VIP en un determinado mes y año
    public List<com.factorit.ecommerce.model.User> getLostVipUsersByMonth(int year, int month) {
        // Construir el rango de fechas para el mes y año dados
        LocalDate startOfMonth = LocalDate.of(year, month, 1);
        LocalDate endOfMonth = startOfMonth.withDayOfMonth(startOfMonth.lengthOfMonth());

        // Filtrar los usuarios entre las fechas
        return userRepository.findByDateWhenLostVIPBetween(startOfMonth, endOfMonth);
    }

    // Este método es usado por JWTController para validar el token
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<com.factorit.ecommerce.model.User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            var userObj = user.get();
            return User.builder()
                    .username(userObj.getUsername())
                    .password(userObj.getPassword())
                    .roles(userObj.getRole().toString())
                    .build();
        }else{
            throw new UsernameNotFoundException(username);
        }
    }

}
