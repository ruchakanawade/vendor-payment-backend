package com.vendor.vendor_payment_system.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.vendor.vendor_payment_system.dto.CreatePurchaseOrderRequest;
import com.vendor.vendor_payment_system.dto.PurchaseOrderItemResponse;
import com.vendor.vendor_payment_system.dto.PurchaseOrderResponse;
import com.vendor.vendor_payment_system.entity.PurchaseOrderEntity;
import com.vendor.vendor_payment_system.entity.PurchaseOrderItemEntity;
import com.vendor.vendor_payment_system.entity.PurchaseOrderStatus;
import com.vendor.vendor_payment_system.entity.VendorEntity;
import com.vendor.vendor_payment_system.entity.VendorStatus;
import com.vendor.vendor_payment_system.repository.PurchaseOrderItemRepository;
import com.vendor.vendor_payment_system.repository.PurchaseOrderRepository;
import com.vendor.vendor_payment_system.repository.VendorRepository;

@Service
public class PurchaseOrderService {

    private static final DateTimeFormatter PO_DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd");

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository itemRepository;
    private final VendorRepository vendorRepository;

    public PurchaseOrderService(
            PurchaseOrderRepository purchaseOrderRepository,
            PurchaseOrderItemRepository itemRepository,
            VendorRepository vendorRepository) {

        this.purchaseOrderRepository = purchaseOrderRepository;
        this.itemRepository = itemRepository;
        this.vendorRepository = vendorRepository;
    }

    @Transactional
    public PurchaseOrderResponse createPurchaseOrder(CreatePurchaseOrderRequest request) {

        VendorEntity vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Vendor not found"));

        if (vendor.getStatus() != VendorStatus.ACTIVE) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot create purchase order for inactive vendor"
            );
        }

        BigDecimal totalAmount = request.getItems()
                .stream()
                .map(i -> i.getUnitPrice()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        LocalDate dueDate = request.getPoDate()
                .plusDays(vendor.getPaymentTermsDays());

        PurchaseOrderEntity po = new PurchaseOrderEntity();
        po.setVendor(vendor);
        po.setPoDate(request.getPoDate());
        po.setDueDate(dueDate);
        po.setTotalAmount(totalAmount);
        po.setStatus(PurchaseOrderStatus.DRAFT);

        purchaseOrderRepository.save(po);

        String poNumber = generatePoNumber(po.getId(), request.getPoDate());
        po.setPoNumber(poNumber);

        purchaseOrderRepository.save(po);

        List<PurchaseOrderItemEntity> items = request.getItems()
                .stream()
                .map(req -> {
                    PurchaseOrderItemEntity item = new PurchaseOrderItemEntity();
                    item.setPurchaseOrder(po);
                    item.setDescription(req.getDescription());
                    item.setQuantity(req.getQuantity());
                    item.setUnitPrice(req.getUnitPrice());
                    return item;
                })
                .toList();

        itemRepository.saveAll(items);

        return mapToResponse(po, items);
    }

    @Transactional(readOnly = true)
    public List<PurchaseOrderResponse> listPurchaseOrders() {

        return purchaseOrderRepository.findAll()
                .stream()
                .map(po -> {
                    List<PurchaseOrderItemEntity> items =
                            itemRepository.findByPurchaseOrderId(po.getId());
                    return mapToResponse(po, items);
                })
                .toList();
    }

    @Transactional(readOnly = true)
    public PurchaseOrderResponse getPurchaseOrder(Long id) {

        PurchaseOrderEntity po = purchaseOrderRepository.findById(id)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase order not found"));

        List<PurchaseOrderItemEntity> items =
                itemRepository.findByPurchaseOrderId(po.getId());

        return mapToResponse(po, items);
    }

    private PurchaseOrderResponse mapToResponse(
            PurchaseOrderEntity po,
            List<PurchaseOrderItemEntity> items) {

        List<PurchaseOrderItemResponse> itemResponses = items.stream()
                .map(i -> new PurchaseOrderItemResponse(
                        i.getId(),
                        i.getDescription(),
                        i.getQuantity(),
                        i.getUnitPrice(),
                        i.getLineTotal()
                ))
                .toList();

        return new PurchaseOrderResponse(
                po.getId(),
                po.getPoNumber(),
                po.getVendor().getId(),
                po.getPoDate(),
                po.getDueDate(),
                po.getTotalAmount(),
                po.getStatus(),
                itemResponses
        );
    }

    private String generatePoNumber(Long id, LocalDate poDate) {
        String datePart = poDate.format(PO_DATE_FORMAT);
        String seqPart = String.format("%03d", id % 1000);
        return "PO-" + datePart + "-" + seqPart;
    }
    
    @Transactional
    public PurchaseOrderResponse updateStatus(Long poId, PurchaseOrderStatus newStatus) {

        PurchaseOrderEntity po = purchaseOrderRepository.findById(poId)
                .orElseThrow(() ->
                        new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase order not found"));

        PurchaseOrderStatus current = po.getStatus();

        validateStatusTransition(current, newStatus);

        po.setStatus(newStatus);

        return mapToResponse(
                po,
                itemRepository.findByPurchaseOrderId(po.getId())
        );
    }

    private void validateStatusTransition(
            PurchaseOrderStatus current,
            PurchaseOrderStatus target) {

        if (current == PurchaseOrderStatus.FULLY_PAID) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Fully paid purchase order cannot be modified"
            );
        }

        if (current == target) {
            return;
        }

        switch (current) {
            case DRAFT -> {
                if (target != PurchaseOrderStatus.APPROVED) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Draft can only transition to Approved"
                    );
                }
            }
            case APPROVED -> {
                if (target != PurchaseOrderStatus.PARTIALLY_PAID
                        && target != PurchaseOrderStatus.FULLY_PAID) {

                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Approved can only transition to Partially Paid or Fully Paid"
                    );
                }
            }
            case PARTIALLY_PAID -> {
                if (target != PurchaseOrderStatus.FULLY_PAID) {
                    throw new ResponseStatusException(
                            HttpStatus.BAD_REQUEST,
                            "Partially Paid can only transition to Fully Paid"
                    );
                }
            }
            default -> throw new IllegalStateException("Unexpected status: " + current);
        }
    }

}
