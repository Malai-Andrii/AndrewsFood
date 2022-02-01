package com.site.andrewsfood.Dao;

import com.site.andrewsfood.Model.domain.CustomUserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomUserDetailsRepo extends JpaRepository<CustomUserDetails, Long> {
    CustomUserDetails findByActivationCode(String code);
    CustomUserDetails findByEmail(String email);
}
