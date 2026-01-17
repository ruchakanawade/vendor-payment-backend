package com.vendor.vendor_payment_system.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.vendor.vendor_payment_system.entity.PurchaseOrderEntity;
import com.vendor.vendor_payment_system.entity.PurchaseOrderStatus;

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrderEntity, Long> {

    Optional<PurchaseOrderEntity> findByPoNumber(String poNumber);

    List<PurchaseOrderEntity> findByVendorId(Long vendorId);

    List<PurchaseOrderEntity> findByStatus(PurchaseOrderStatus status);
    
    @Query("""
    	    SELECT 
    	        v.id,
    	        v.name,
    	        COALESCE(SUM(po.totalAmount), 0) 
    	        - COALESCE(SUM(p.amountPaid), 0)
    	    FROM PurchaseOrderEntity po
    	    JOIN po.vendor v
    	    LEFT JOIN PaymentEntity p
    	        ON p.purchaseOrder.id = po.id
    	        AND p.deletedAt IS NULL
    	    WHERE po.deletedAt IS NULL
    	    GROUP BY v.id, v.name
    	""")
    List<Object[]> calculateVendorOutstanding();
}
