package com.factorit.ecommerce.repository;

import com.factorit.ecommerce.model.Role;
import com.factorit.ecommerce.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    List<User> findByRole(Role role);

    List<User> findByDateWhenBecameVIPBetween(LocalDate startOfMonth, LocalDate endOfMonth);

    List<User> findByDateWhenLostVIPBetween(LocalDate startOfMonth, LocalDate endOfMonth);
}
