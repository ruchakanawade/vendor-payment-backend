-- Create Schems by name: vendor_payment_tracking_system

-- ============================================================
-- MSME Vendor Payment Tracking System - MySQL Schema
-- With JWT Authentication Support
-- Database: vendor_payment_tracking_system
-- ============================================================

-- ============================================================
-- CREATE DATABASE
-- ============================================================

CREATE DATABASE IF NOT EXISTS vendor_payment_tracking_system;
USE vendor_payment_tracking_system;

-- ============================================================
-- ROLES TABLE
-- ============================================================

CREATE TABLE roles (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    name            VARCHAR(50) NOT NULL UNIQUE,
    description     VARCHAR(200)
) ENGINE=InnoDB;

-- ============================================================
-- USERS TABLE (JWT AUTHENTICATION)
-- ============================================================

CREATE TABLE users (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,

    username        VARCHAR(100) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    email           VARCHAR(150),
    is_active        BOOLEAN NOT NULL DEFAULT TRUE,

    role_id         BIGINT NULL,

    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                   ON UPDATE CURRENT_TIMESTAMP,

    CONSTRAINT fk_user_role
        FOREIGN KEY (role_id)
        REFERENCES roles(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE INDEX idx_users_username ON users(username);
CREATE INDEX idx_users_active ON users(is_active);

-- ============================================================
-- VENDORS TABLE
-- ============================================================

CREATE TABLE vendors (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,

    name                VARCHAR(150) NOT NULL,
    contact_person      VARCHAR(150),
    email               VARCHAR(150) NOT NULL,
    phone               VARCHAR(50),

    payment_terms_days  INT NOT NULL,
    status              ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',

    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                       ON UPDATE CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMP NULL,

    created_by          BIGINT NULL,
    updated_by          BIGINT NULL,

    CONSTRAINT uq_vendor_name UNIQUE (name),
    CONSTRAINT uq_vendor_email UNIQUE (email),

    CONSTRAINT chk_vendor_payment_terms
        CHECK (payment_terms_days IN (7, 15, 30, 45, 60)),

    CONSTRAINT fk_vendor_created_by
        FOREIGN KEY (created_by) REFERENCES users(id),

    CONSTRAINT fk_vendor_updated_by
        FOREIGN KEY (updated_by) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE INDEX idx_vendors_status ON vendors(status);

-- ============================================================
-- PURCHASE ORDERS TABLE
-- ============================================================

CREATE TABLE purchase_orders (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,

    po_number           VARCHAR(50) NOT NULL,
    vendor_id           BIGINT NOT NULL,

    po_date             DATE NOT NULL,
    due_date            DATE NOT NULL,

    total_amount        DECIMAL(14,2) NOT NULL,
    status              ENUM('DRAFT', 'APPROVED', 'PARTIALLY_PAID', 'FULLY_PAID')
                            NOT NULL DEFAULT 'DRAFT',

    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                       ON UPDATE CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMP NULL,

    created_by          BIGINT NULL,
    updated_by          BIGINT NULL,

    CONSTRAINT uq_po_number UNIQUE (po_number),

    CONSTRAINT chk_po_total_amount
        CHECK (total_amount >= 0),

    CONSTRAINT fk_po_vendor
        FOREIGN KEY (vendor_id)
        REFERENCES vendors(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_po_created_by
        FOREIGN KEY (created_by) REFERENCES users(id),

    CONSTRAINT fk_po_updated_by
        FOREIGN KEY (updated_by) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE INDEX idx_po_vendor_id ON purchase_orders(vendor_id);
CREATE INDEX idx_po_status ON purchase_orders(status);
CREATE INDEX idx_po_po_date ON purchase_orders(po_date);

-- ============================================================
-- PURCHASE ORDER ITEMS TABLE
-- ============================================================

CREATE TABLE purchase_order_items (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,

    purchase_order_id   BIGINT NOT NULL,

    description         VARCHAR(255) NOT NULL,
    quantity            INT NOT NULL,
    unit_price          DECIMAL(14,2) NOT NULL,

    line_total           DECIMAL(14,2)
        GENERATED ALWAYS AS (quantity * unit_price) STORED,

    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT chk_item_quantity
        CHECK (quantity > 0),

    CONSTRAINT chk_item_unit_price
        CHECK (unit_price >= 0),

    CONSTRAINT fk_item_po
        FOREIGN KEY (purchase_order_id)
        REFERENCES purchase_orders(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE INDEX idx_po_items_po_id ON purchase_order_items(purchase_order_id);

-- ============================================================
-- PAYMENTS TABLE
-- ============================================================

CREATE TABLE payments (
    id                  BIGINT AUTO_INCREMENT PRIMARY KEY,

    payment_reference   VARCHAR(50) NOT NULL,
    purchase_order_id   BIGINT NOT NULL,

    payment_date        DATE NOT NULL,
    amount_paid         DECIMAL(14,2) NOT NULL,
    payment_method      ENUM('CASH', 'CHEQUE', 'NEFT', 'RTGS', 'UPI') NOT NULL,
    notes               VARCHAR(500),

    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
                                       ON UPDATE CURRENT_TIMESTAMP,
    deleted_at          TIMESTAMP NULL,

    created_by          BIGINT NULL,
    updated_by          BIGINT NULL,

    CONSTRAINT uq_payment_reference UNIQUE (payment_reference),

    CONSTRAINT chk_payment_amount
        CHECK (amount_paid > 0),

    CONSTRAINT fk_payment_po
        FOREIGN KEY (purchase_order_id)
        REFERENCES purchase_orders(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CONSTRAINT fk_payment_created_by
        FOREIGN KEY (created_by) REFERENCES users(id),

    CONSTRAINT fk_payment_updated_by
        FOREIGN KEY (updated_by) REFERENCES users(id)
) ENGINE=InnoDB;

CREATE INDEX idx_payments_po_id ON payments(purchase_order_id);
CREATE INDEX idx_payments_payment_date ON payments(payment_date);

-- ============================================================
-- END OF SCHEMA
-- ============================================================
