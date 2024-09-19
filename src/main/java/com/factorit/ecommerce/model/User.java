package com.factorit.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user",  schema = "public", catalog = "ecommerce")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "total_spent_last_month")
    private double totalSpentLastMonth;

    @Column(name = "last_purchase")
    private LocalDate lastPurchase;

    @Column(name = "date_when_became_vip")
    private LocalDate dateWhenBecameVIP;

    @Column(name = "date_when_lost_vip")
    private LocalDate dateWhenLostVIP;

    // Estos métodos surgen por implementar UserDetails

    // Método para obtener los roles del usuario
    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        System.out.println("Role: " + role);
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    // Los demás métodos no los usaremos, por lo que los dejamos como true

    @JsonIgnore
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @JsonIgnore
    @Override
    public boolean isEnabled() {
        return true;
    }
}