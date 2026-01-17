package com.vendor.vendor_payment_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateVendorRequest {

    @NotBlank
    private String name;

    private String contactPerson;

    @Email
    @NotBlank
    private String email;

    private String phone;

    @NotNull
    private Integer paymentTermsDays;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getPaymentTermsDays() {
		return paymentTermsDays;
	}

	public void setPaymentTermsDays(Integer paymentTermsDays) {
		this.paymentTermsDays = paymentTermsDays;
	}
}
