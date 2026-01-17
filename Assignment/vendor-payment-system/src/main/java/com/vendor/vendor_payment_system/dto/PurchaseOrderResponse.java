package com.vendor.vendor_payment_system.dto;

import com.vendor.vendor_payment_system.entity.PurchaseOrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@AllArgsConstructor
public class PurchaseOrderResponse {

    private Long id;
    private String poNumber;
    private Long vendorId;
    private LocalDate poDate;
    private LocalDate dueDate;
    private BigDecimal totalAmount;
    private PurchaseOrderStatus status;
    private List<PurchaseOrderItemResponse> items;
    public PurchaseOrderResponse(
            Long id,
            String poNumber,
            Long vendorId,
            LocalDate poDate,
            LocalDate dueDate,
            BigDecimal totalAmount,
            PurchaseOrderStatus status,
            List<PurchaseOrderItemResponse> items) {

        this.id = id;
        this.poNumber = poNumber;
        this.vendorId = vendorId;
        this.poDate = poDate;
        this.dueDate = dueDate;
        this.totalAmount = totalAmount;
        this.status = status;
        this.items = items;
    }
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPoNumber() {
		return poNumber;
	}
	public void setPoNumber(String poNumber) {
		this.poNumber = poNumber;
	}
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
	public LocalDate getDueDate() {
		return dueDate;
	}
	public void setDueDate(LocalDate dueDate) {
		this.dueDate = dueDate;
	}
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}
	public PurchaseOrderStatus getStatus() {
		return status;
	}
	public void setStatus(PurchaseOrderStatus status) {
		this.status = status;
	}
	public List<PurchaseOrderItemResponse> getItems() {
		return items;
	}
	public void setItems(List<PurchaseOrderItemResponse> items) {
		this.items = items;
	}
}
