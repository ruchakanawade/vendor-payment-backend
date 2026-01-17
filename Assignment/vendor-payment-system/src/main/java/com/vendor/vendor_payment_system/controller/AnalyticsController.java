package com.vendor.vendor_payment_system.controller;

import com.vendor.vendor_payment_system.dto.VendorOutstandingResponse;
import com.vendor.vendor_payment_system.service.AnalyticsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    public AnalyticsController(AnalyticsService analyticsService) {
        this.analyticsService = analyticsService;
    }

    @GetMapping("/vendor-outstanding")
    public List<VendorOutstandingResponse> vendorOutstanding() {
        return analyticsService.getVendorOutstanding();
    }
}
