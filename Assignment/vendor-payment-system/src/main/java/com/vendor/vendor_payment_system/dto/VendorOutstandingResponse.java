package com.vendor.vendor_payment_system.dto;

import java.math.BigDecimal;

public class VendorOutstandingResponse {

    private final Long vendorId;
    private final String vendorName;
    private final BigDecimal outstandingAmount;

    public VendorOutstandingResponse(
            Long vendorId,
            String vendorName,
            BigDecimal outstandingAmount) {

        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.outstandingAmount = outstandingAmount;
    }

    public Long getVendorId() {
        return vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public BigDecimal getOutstandingAmount() {
        return outstandingAmount;
    }
}
