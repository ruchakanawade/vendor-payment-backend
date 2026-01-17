package com.vendor.vendor_payment_system.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class CreatePurchaseOrderRequest {

    @NotNull
    private Long vendorId;

    @NotNull
    private LocalDate poDate;

    @Valid
    @Size(min = 1)
    private List<CreatePurchaseOrderItemRequest> items;

	public Long getVendorId() {
		return vendorId;
	}

	public void setVendorId(Long vendorId) {
		this.vendorId = vendorId;
	}

	public LocalDate getPoDate() {
		return poDate;
	}

	public void setPoDate(LocalDate poDate) {
		this.poDate = poDate;
	}

	public List<CreatePurchaseOrderItemRequest> getItems() {
		return items;
	}

	public void setItems(List<CreatePurchaseOrderItemRequest> items) {
		this.items = items;
	}
}
