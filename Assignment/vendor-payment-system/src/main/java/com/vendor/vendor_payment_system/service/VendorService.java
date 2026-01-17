package com.vendor.vendor_payment_system.service;

import com.vendor.vendor_payment_system.dto.CreateVendorRequest;
import com.vendor.vendor_payment_system.dto.VendorResponse;
import com.vendor.vendor_payment_system.entity.VendorEntity;
import com.vendor.vendor_payment_system.entity.VendorStatus;
import com.vendor.vendor_payment_system.repository.VendorRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VendorService {

    private final VendorRepository vendorRepository;

    public VendorService(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Transactional
    public VendorResponse createVendor(CreateVendorRequest request) {

        vendorRepository.findByName(request.getName())
                .ifPresent(v -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Vendor name already exists"
                    );
                });

        vendorRepository.findByEmail(request.getEmail())
                .ifPresent(v -> {
                    throw new ResponseStatusException(
                            HttpStatus.CONFLICT,
                            "Vendor email already exists"
                    );
                });

        VendorEntity vendor = new VendorEntity();
        vendor.setName(request.getName());
        vendor.setContactPerson(request.getContactPerson());
        vendor.setEmail(request.getEmail());
        vendor.setPhone(request.getPhone());
        vendor.setPaymentTermsDays(request.getPaymentTermsDays());
        vendor.setStatus(VendorStatus.ACTIVE);

        VendorEntity saved = vendorRepository.save(vendor);

        return new VendorResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail(),
                saved.getStatus()
        );
    }

    @Transactional(readOnly = true)
    public List<VendorResponse> listVendors() {
        return vendorRepository.findAll()
                .stream()
                .map(v -> new VendorResponse(
                        v.getId(),
                        v.getName(),
                        v.getEmail(),
                        v.getStatus()
                ))
                .toList();
    }
}
