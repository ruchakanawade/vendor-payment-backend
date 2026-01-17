package com.vendor.vendor_payment_system.dto;

import com.vendor.vendor_payment_system.entity.PurchaseOrderStatus;
import jakarta.validation.constraints.NotNull;

public class UpdatePurchaseOrderStatusRequest {

    @NotNull
    private PurchaseOrderStatus status;

    public PurchaseOrderStatus getStatus() {
        return status;
    }

    public void setStatus(PurchaseOrderStatus status) {
        this.status = status;
    }
}
