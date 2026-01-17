package com.vendor.vendor_payment_system.repository;

import com.vendor.vendor_payment_system.entity.PaymentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface PaymentRepository extends JpaRepository<PaymentEntity, Long> {

    List<PaymentEntity> findByPurchaseOrderIdAndDeletedAtIsNull(Long purchaseOrderId);

    @Query("""
        SELECT COALESCE(SUM(p.amountPaid), 0)
        FROM PaymentEntity p
        WHERE p.purchaseOrder.id = :purchaseOrderId
          AND p.deletedAt IS NULL
    """)
    BigDecimal sumPaymentsForPurchaseOrder(Long purchaseOrderId);
}
