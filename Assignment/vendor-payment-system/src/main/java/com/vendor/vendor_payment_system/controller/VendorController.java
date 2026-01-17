package com.vendor.vendor_payment_system.controller;

import com.vendor.vendor_payment_system.dto.CreateVendorRequest;
import com.vendor.vendor_payment_system.dto.VendorResponse;
import com.vendor.vendor_payment_system.service.VendorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/vendors")
public class VendorController {

    private final VendorService vendorService;

    public VendorController(VendorService vendorService) {
        this.vendorService = vendorService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public VendorResponse createVendor(@Valid @RequestBody CreateVendorRequest request) {
        return vendorService.createVendor(request);
    }

    @GetMapping
    public List<VendorResponse> listVendors() {
        return vendorService.listVendors();
    }
}
