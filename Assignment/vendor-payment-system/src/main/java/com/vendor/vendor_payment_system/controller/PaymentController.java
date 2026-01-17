package com.vendor.vendor_payment_system.controller;

import com.vendor.vendor_payment_system.dto.CreatePaymentRequest;
import com.vendor.vendor_payment_system.dto.PaymentResponse;
import com.vendor.vendor_payment_system.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse create(@Valid @RequestBody CreatePaymentRequest request) {
        return paymentService.createPayment(request);
    }

    @GetMapping
    public List<PaymentResponse> list() {
        return paymentService.listPayments();
    }
}
