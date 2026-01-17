package com.vendor.vendor_payment_system.repository;

import com.vendor.vendor_payment_system.entity.VendorEntity;
import com.vendor.vendor_payment_system.entity.VendorStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VendorRepository extends JpaRepository<VendorEntity, Long> {

    Optional<VendorEntity> findByName(String name);

    Optional<VendorEntity> findByEmail(String email);

    List<VendorEntity> findByStatus(VendorStatus status);
}
