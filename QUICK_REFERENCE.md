# Quick Reference - Code & CodeValue API

## 🚀 Quick Start

### 1. Login & Get Token
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"nexus","password":"password"}'
# Save TOKEN from response
```

### 2. Create Code Type
```bash
curl -X POST http://localhost:8080/api/configuration/codes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"codeType":"PAYMENT_METHOD","description":"Payment methods"}'
# Save CODE_ID from response
```

### 3. Add Code Values
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"codeId":1,"value":"CREDIT_CARD","display":"Credit Card","orderPosition":1}'
```

### 4. Get Dropdown (Frontend - No Auth)
```bash
curl http://localhost:8080/api/configuration/code-values/dropdown/PAYMENT_METHOD
```

---

## 📌 Common Code Types to Create

```bash
# Payment Methods
{"codeType":"PAYMENT_METHOD","description":"Payment method types"}

# User Status
{"codeType":"USER_STATUS","description":"User account status"}

# Order Status
{"codeType":"ORDER_STATUS","description":"Order processing status"}

# Product Category
{"codeType":"PRODUCT_CATEGORY","description":"Product categories"}

# Shipping Method
{"codeType":"SHIPPING_METHOD","description":"Shipping methods"}
```

---

## 📚 API Quick Reference

| Method | Endpoint | Auth | Description |
|--------|----------|------|-------------|
| POST | `/api/configuration/codes` | ✅ | Create code |
| GET | `/api/configuration/codes` | ✅ | Get all codes |
| GET | `/api/configuration/codes/{id}` | ✅ | Get code by ID |
| GET | `/api/configuration/codes/type/{type}` | ✅ | Get code by type |
| PUT | `/api/configuration/codes/{id}` | ✅ | Update code |
| DELETE | `/api/configuration/codes/{id}` | ✅ | Delete code |
| POST | `/api/configuration/code-values` | ✅ | Create value |
| GET | `/api/configuration/code-values/code/{id}` | ✅ | Get values by code |
| GET | `/api/configuration/code-values/{id}` | ✅ | Get value by ID |
| GET | `/api/configuration/code-values/dropdown/{type}` | ❌ | Get dropdown options |
| PUT | `/api/configuration/code-values/{id}` | ✅ | Update value |
| DELETE | `/api/configuration/code-values/{id}` | ✅ | Delete value |

---

## 💡 Real-World Example: E-Commerce

### Create Payment Methods
```json
POST /api/configuration/codes
{"codeType":"PAYMENT_METHOD","description":"Payment method types"}

Code ID: 1

ADD VALUES:
{codeId:1, value:"CREDIT_CARD", display:"Credit Card", orderPosition:1}
{codeId:1, value:"DEBIT_CARD", display:"Debit Card", orderPosition:2}
{codeId:1, value:"UPI", display:"UPI", orderPosition:3}
{codeId:1, value:"WALLET", display:"Digital Wallet", orderPosition:4}
```

### Create Order Status
```json
POST /api/configuration/codes
{"codeType":"ORDER_STATUS","description":"Order processing status"}

Code ID: 2

ADD VALUES:
{codeId:2, value:"PENDING", display:"Pending", orderPosition:1}
{codeId:2, value:"CONFIRMED", display:"Confirmed", orderPosition:2}
{codeId:2, value:"SHIPPED", display:"Shipped", orderPosition:3}
{codeId:2, value:"DELIVERED", display:"Delivered", orderPosition:4}
{codeId:2, value:"CANCELLED", display:"Cancelled", orderPosition:5}
{codeId:2, value:"RETURNED", display:"Returned", orderPosition:6}
```

### Use in Frontend
```javascript
// Get payment methods dropdown
fetch('/api/configuration/code-values/dropdown/PAYMENT_METHOD')
  .then(r => r.json())
  .then(data => {
    // data.data contains sorted list of payment options
    data.data.forEach(option => {
      // option.value = "CREDIT_CARD"
      // option.display = "Credit Card"
      // option.orderPosition = 1
    });
  });

// Get order status dropdown
fetch('/api/configuration/code-values/dropdown/ORDER_STATUS')
  .then(r => r.json())
  .then(data => {
    // data.data contains sorted list of order statuses
  });
```

---

## 🔄 Update Values

### To change display text:
```bash
curl -X PUT http://localhost:8080/api/configuration/code-values/{VALUE_ID} \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{"codeId":1,"value":"CREDIT_CARD","display":"Visa/Mastercard","orderPosition":1}'
```

### To reorder values:
```bash
# Change orderPosition and update multiple values
# Existing order: CC=1, DC=2, UPI=3
# Desired order: UPI=1, CC=2, DC=3

curl -X PUT .../code-values/1 \
  -d '{"codeId":1,"value":"CREDIT_CARD","display":"Credit Card","orderPosition":2}'

curl -X PUT .../code-values/2 \
  -d '{"codeId":1,"value":"DEBIT_CARD","display":"Debit Card","orderPosition":3}'

curl -X PUT .../code-values/3 \
  -d '{"codeId":1,"value":"UPI","display":"UPI","orderPosition":1}'
```

---

## 📊 Expected Response Formats

### Single Code Response
```json
{
  "id": 1,
  "codeType": "PAYMENT_METHOD",
  "description": "Payment method types",
  "status": "ACTIVE"
}
```

### Single Code Value Response
```json
{
  "id": 1,
  "codeId": 1,
  "value": "CREDIT_CARD",
  "display": "Credit Card",
  "description": "Credit Card payment",
  "orderPosition": 1,
  "status": "ACTIVE"
}
```

### Error Response
```json
{
  "success": 0,
  "code": 400,
  "message": "Code type already exists: PAYMENT_METHOD"
}
```

---

## ✅ Build Status

- **Compilation**: ✅ SUCCESSFUL
- **All Components**: ✅ CREATED
- **Security**: ✅ CONFIGURED
- **Documentation**: ✅ PROVIDED
- **Ready to Deploy**: ✅ YES

---

## 📖 More Info

- Full API Documentation: `CODE_CODEVALUE_DOCUMENTATION.md`
- Test Cases: `CODE_CODEVALUE_TEST_CASES.md`
- Implementation Details: `CODE_CODEVALUE_IMPLEMENTATION_SUMMARY.md`
- Swagger UI: `http://localhost:8080/swagger-ui.html`

---

## 🐛 Troubleshooting

**Q: Getting "Code not found" when creating values?**
A: Verify the codeId exists. Use GET `/api/configuration/codes` to find IDs.

**Q: Dropdown not returning values in correct order?**
A: CodeValues are ordered by `orderPosition` in the database. Update the position if needed.

**Q: Getting "Code type already exists"?**
A: Code types must be unique. Use a different code type or GET `/api/configuration/codes/type/{type}` to find existing.

**Q: Can't access dropdown endpoint?**
A: The `/api/configuration/code-values/dropdown/{type}` endpoint is public (no auth required).

**Q: Getting 401 Unauthorized on management endpoints?**
A: Add `Authorization: Bearer {token}` header with valid JWT token.

---

## 💾 Sample SQL for Testing

```sql
-- Your schema will auto-create with Hibernate
-- Just use the REST APIs to populate data
-- Or run these if needed:

INSERT INTO codes (code_type, description, status, created_at) 
VALUES ('PAYMENT_METHOD', 'Payment methods', 1, NOW());

INSERT INTO code_values (code_id, value, display, description, order_position, status, created_at) 
VALUES (1, 'CREDIT_CARD', 'Credit Card', 'Credit Card payment', 1, 1, NOW());
```

---

✨ **Implementation Complete and Ready to Use!** ✨

