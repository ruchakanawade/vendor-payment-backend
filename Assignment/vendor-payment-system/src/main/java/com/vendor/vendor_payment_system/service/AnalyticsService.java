package com.vendor.vendor_payment_system.service;

import com.vendor.vendor_payment_system.dto.VendorOutstandingResponse;
import com.vendor.vendor_payment_system.repository.PurchaseOrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class AnalyticsService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    public AnalyticsService(PurchaseOrderRepository purchaseOrderRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Transactional(readOnly = true)
    public List<VendorOutstandingResponse> getVendorOutstanding() {

        List<Object[]> rows = purchaseOrderRepository.calculateVendorOutstanding();

        return rows.stream()
                .map(row -> new VendorOutstandingResponse(
                        ((Number) row[0]).longValue(),
                        (String) row[1],
                        (BigDecimal) row[2]
                ))
                .toList();
    }
}
