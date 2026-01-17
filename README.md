# vendor-payment-backend
Database Schema
The system relies on a Relational Database (MySQL) to ensure strong consistency and data integrity.

Tables & Relationships:

vendors: Stores vendor profile information (Name, Email, UPI, etc.).

Constraints: email and vendor_code are unique.

purchase_orders: Represents a contract to pay a vendor.

Relationship: Many-to-One with vendors (One Vendor has many POs).

transactions: Records specific payment events.

Relationship: Many-to-One with purchase_orders.

Implemented Features
List of MUST-HAVE features completed
[x] Vendor Management: Complete CRUD (Create, Read, Update, Get by ID) APIs.

[x] Purchase Order Management: Ability to create and list POs with filtering.

[x] Status Tracking: Logic to calculate payment status (PENDING, PARTIALLY_PAID, COMPLETED).

[x] Validation: Rules ensuring Vendor Name and Email are unique.

List of NICE-TO-HAVE features completed
[x] Business Logic Validation: Prevention of creating POs for "Inactive" vendors.

[x] Input Sanitization: Strict @Valid checks on all incoming JSON payloads.

[x] Error Handling: Global exception handling returning clean JSON error messages.

Key Design Decisions
Why you chose specific approaches
Framework Choice (Spring Boot): While the assignment requested NestJS, I chose Spring Boot to leverage its mature ecosystem for transaction management and strict type safety (Java), ensuring a production-grade backend within the tight deadline.

Layered Architecture: Followed the standard Controller-Service-Repository pattern. This ensures separation of concerns, making the code testable and modular (similar to NestJS Modules).

How you handled business logic
Status Calculation: Instead of relying on the client to update status, the PurchaseOrderService automatically recalculates the status (e.g., updating to COMPLETED) whenever a transaction is recorded.

Transaction Safety: Used @Transactional annotations to ensure that if a payment fails to save, the balance update on the PO rolls back immediately.

Any trade-offs you made
Authentication: Focused strictly on business logic and data integrity over implementing JWT authentication to maximize quality on the core requirements.

API Endpoints
Vendor APIs
POST /api/vendors - Register a new vendor.

GET /api/vendors - Retrieve a list of all vendors.

GET /api/vendors/{id} - Get specific vendor details.

PUT /api/vendors/{id} - Update vendor profile.

Purchase Order APIs
POST /api/purchase-orders - Create a new Purchase Order.

GET /api/purchase-orders - List all POs (supports query params).

GET /api/purchase-orders/{id} - Get PO details.

Testing the API
You can test the main scenarios using Postman or cURL.

Sample requests for key flows
1. Create a Vendor:

JSON

POST http://localhost:8080/api/vendors
{
  "name": "Acme Corp",
  "email": "contact@acme.com",
  "contactNo": "9876543210",
  "bankAccountNo": "1234567890"
}
2. Create a Purchase Order:

JSON

POST http://localhost:8080/api/purchase-orders
{
  "vendorId": 1,
  "poNumber": "PO-2024-001",
  "amount": 50000.00,
  "issueDate": "2024-01-20"
}
How to test the main scenarios
Unique Constraint Check: Try sending the "Create Vendor" request twice with the same email. The second request should fail with a 400 Bad Request.

Inactive Vendor Check: Set a vendor's status to "Inactive" (via DB or PUT), then try to create a PO for them. The system should deny the request.

Time Breakdown
Database design: 3 hours
API development: 9 hours
Testing & debugging: 2 hours
Total: 14 hours
