package com.vendor.vendor_payment_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class VendorPaymentSystemApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(VendorPaymentSystemApplication.class, args);
	}
	
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(VendorPaymentSystemApplication.class);
    }

}
