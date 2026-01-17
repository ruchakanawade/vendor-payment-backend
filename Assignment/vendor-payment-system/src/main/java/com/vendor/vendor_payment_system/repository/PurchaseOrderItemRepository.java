package com.vendor.vendor_payment_system.repository;

import com.vendor.vendor_payment_system.entity.PurchaseOrderItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseOrderItemRepository extends JpaRepository<PurchaseOrderItemEntity, Long> {

    List<PurchaseOrderItemEntity> findByPurchaseOrderId(Long purchaseOrderId);
}
