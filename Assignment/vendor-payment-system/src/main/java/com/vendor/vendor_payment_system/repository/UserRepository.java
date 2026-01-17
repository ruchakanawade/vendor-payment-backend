package com.vendor.vendor_payment_system.repository;

import com.vendor.vendor_payment_system.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsernameAndActiveTrue(String username);
}
