package com.vendor.vendor_payment_system.dto;

import com.vendor.vendor_payment_system.entity.VendorStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VendorResponse {

    private Long id;
    private String name;
    private String email;
    private VendorStatus status;
    
    public VendorResponse(Long id, String name, String email, VendorStatus status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.status = status;
    }
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public VendorStatus getStatus() {
		return status;
	}
	public void setStatus(VendorStatus status) {
		this.status = status;
	}
}
