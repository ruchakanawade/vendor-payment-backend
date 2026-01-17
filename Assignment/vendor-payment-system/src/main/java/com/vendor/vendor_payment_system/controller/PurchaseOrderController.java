package com.vendor.vendor_payment_system.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.vendor.vendor_payment_system.dto.CreatePurchaseOrderRequest;
import com.vendor.vendor_payment_system.dto.PurchaseOrderResponse;
import com.vendor.vendor_payment_system.dto.UpdatePurchaseOrderStatusRequest;
import com.vendor.vendor_payment_system.service.PurchaseOrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/purchase-orders")
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    public PurchaseOrderController(PurchaseOrderService purchaseOrderService) {
        this.purchaseOrderService = purchaseOrderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PurchaseOrderResponse create(@Valid @RequestBody CreatePurchaseOrderRequest request) {
        return purchaseOrderService.createPurchaseOrder(request);
    }

    @GetMapping
    public List<PurchaseOrderResponse> list() {
        return purchaseOrderService.listPurchaseOrders();
    }

    @GetMapping("/{id}")
    public PurchaseOrderResponse getById(@PathVariable Long id) {
        return purchaseOrderService.getPurchaseOrder(id);
    }
    
    @PatchMapping("/{id}/status")
    public PurchaseOrderResponse updateStatus(
            @PathVariable Long id,
            @RequestBody UpdatePurchaseOrderStatusRequest request) {

        return purchaseOrderService.updateStatus(id, request.getStatus());
    }	
}
