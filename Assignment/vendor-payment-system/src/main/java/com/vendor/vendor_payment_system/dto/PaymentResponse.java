package com.vendor.vendor_payment_system.dto;

import com.vendor.vendor_payment_system.entity.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentResponse {

    private final Long id;
    private final String paymentReference;
    private final Long purchaseOrderId;
    private final LocalDate paymentDate;
    private final BigDecimal amountPaid;
    private final PaymentMethod paymentMethod;

    public PaymentResponse(
            Long id,
            String paymentReference,
            Long purchaseOrderId,
            LocalDate paymentDate,
            BigDecimal amountPaid,
            PaymentMethod paymentMethod) {

        this.id = id;
        this.paymentReference = paymentReference;
        this.purchaseOrderId = purchaseOrderId;
        this.paymentDate = paymentDate;
        this.amountPaid = amountPaid;
        this.paymentMethod = paymentMethod;
    }

    public Long getId() {
        return id;
    }

    public String getPaymentReference() {
        return paymentReference;
    }

    public Long getPurchaseOrderId() {
        return purchaseOrderId;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }
}
