package com.vendor.vendor_payment_system.service;

import com.vendor.vendor_payment_system.dto.CreatePaymentRequest;
import com.vendor.vendor_payment_system.dto.PaymentResponse;
import com.vendor.vendor_payment_system.entity.PaymentEntity;
import com.vendor.vendor_payment_system.entity.PurchaseOrderEntity;
import com.vendor.vendor_payment_system.entity.PurchaseOrderStatus;
import com.vendor.vendor_payment_system.repository.PaymentRepository;
import com.vendor.vendor_payment_system.repository.PurchaseOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PaymentService {

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    private final PaymentRepository paymentRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    public PaymentService(
            PaymentRepository paymentRepository,
            PurchaseOrderRepository purchaseOrderRepository) {

        this.paymentRepository = paymentRepository;
        this.purchaseOrderRepository = purchaseOrderRepository;
    }

    @Transactional
    public PaymentResponse createPayment(CreatePaymentRequest request) {

        PurchaseOrderEntity po = purchaseOrderRepository.findById(request.getPurchaseOrderId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase order not found"));

        BigDecimal totalPaidSoFar =
                paymentRepository.sumPaymentsForPurchaseOrder(po.getId());

        BigDecimal outstanding =
                po.getTotalAmount().subtract(totalPaidSoFar);

        if (request.getAmountPaid().compareTo(outstanding) > 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Payment amount exceeds outstanding balance"
            );
        }

        PaymentEntity payment = new PaymentEntity();
        payment.setPurchaseOrder(po);
        payment.setPaymentDate(request.getPaymentDate());
        payment.setAmountPaid(request.getAmountPaid());
        payment.setPaymentMethod(request.getPaymentMethod());
        payment.setNotes(request.getNotes());

        paymentRepository.save(payment);

        String reference = generatePaymentReference(payment.getId(), LocalDate.now());
        payment.setPaymentReference(reference);
        paymentRepository.save(payment);

        BigDecimal updatedTotalPaid =
                totalPaidSoFar.add(request.getAmountPaid());

        updatePurchaseOrderStatus(po, updatedTotalPaid);

        return new PaymentResponse(
                payment.getId(),
                payment.getPaymentReference(),
                po.getId(),
                payment.getPaymentDate(),
                payment.getAmountPaid(),
                payment.getPaymentMethod()
        );
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> listPayments() {

        return paymentRepository.findAll()
                .stream()
                .map(p -> new PaymentResponse(
                        p.getId(),
                        p.getPaymentReference(),
                        p.getPurchaseOrder().getId(),
                        p.getPaymentDate(),
                        p.getAmountPaid(),
                        p.getPaymentMethod()
                ))
                .toList();
    }

    private void updatePurchaseOrderStatus(
            PurchaseOrderEntity po,
            BigDecimal totalPaid) {

        if (totalPaid.compareTo(po.getTotalAmount()) == 0) {
            po.setStatus(PurchaseOrderStatus.FULLY_PAID);
        } else if (totalPaid.compareTo(BigDecimal.ZERO) > 0) {
            po.setStatus(PurchaseOrderStatus.PARTIALLY_PAID);
        }
        // Draft / Approved transitions handled separately
    }

    private String generatePaymentReference(Long id, LocalDate date) {
        String datePart = date.format(DATE_FORMAT);
        String seqPart = String.format("%03d", id % 1000);
        return "PAY-" + datePart + "-" + seqPart;
    }
}
