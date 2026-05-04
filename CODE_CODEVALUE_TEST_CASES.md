# Code and CodeValue API Test Cases

## 📌 Prerequisites

1. Application running on `http://localhost:8080`
2. JWT token obtained by logging in with admin credentials:
   - Username: `nexus`
   - Password: `password`

## 🧪 Test Scenarios

### Step 1: Login and Get Token

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "nexus",
    "password": "password"
  }'

# Response will contain:
{
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    ...
  }
}

# Save the token as: TOKEN="eyJhbGciOiJIUzI1NiJ9..."
```

---

## Code Management Tests

### Test 1.1: Create Payment Method Code

**Request:**
```bash
curl -X POST http://localhost:8080/api/configuration/codes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeType": "PAYMENT_METHOD",
    "description": "Payment method types"
  }'
```

**Expected Response:**
```json
{
  "success": 1,
  "code": 200,
  "message": "Code created successfully",
  "data": {
    "id": 1,
    "codeType": "PAYMENT_METHOD",
    "description": "Payment method types",
    "status": "ACTIVE"
  }
}
```

**Save:** `PAYMENT_CODE_ID=1`

---

### Test 1.2: Create User Status Code

**Request:**
```bash
curl -X POST http://localhost:8080/api/configuration/codes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeType": "USER_STATUS",
    "description": "User account status"
  }'
```

**Expected Response:**
```json
{
  "success": 1,
  "code": 200,
  "data": {
    "id": 2,
    "codeType": "USER_STATUS",
    "description": "User account status",
    "status": "ACTIVE"
  }
}
```

**Save:** `USER_STATUS_CODE_ID=2`

---

### Test 1.3: Create Order Status Code

**Request:**
```bash
curl -X POST http://localhost:8080/api/configuration/codes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeType": "ORDER_STATUS",
    "description": "Order processing status"
  }'
```

**Expected Response:**
```json
{
  "success": 1,
  "code": 200,
  "data": {
    "id": 3,
    "codeType": "ORDER_STATUS",
    "status": "ACTIVE"
  }
}
```

**Save:** `ORDER_STATUS_CODE_ID=3`

---

### Test 1.4: Get All Codes

**Request:**
```bash
curl -X GET http://localhost:8080/api/configuration/codes \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response:**
```json
{
  "success": 1,
  "code": 200,
  "message": "Codes retrieved successfully",
  "data": [
    {
      "id": 1,
      "codeType": "PAYMENT_METHOD",
      "status": "ACTIVE"
    },
    {
      "id": 2,
      "codeType": "USER_STATUS",
      "status": "ACTIVE"
    },
    {
      "id": 3,
      "codeType": "ORDER_STATUS",
      "status": "ACTIVE"
    }
  ]
}
```

---

### Test 1.5: Get Code by ID

**Request:**
```bash
curl -X GET http://localhost:8080/api/configuration/codes/1 \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response:**
```json
{
  "success": 1,
  "code": 200,
  "message": "Code retrieved successfully",
  "data": {
    "id": 1,
    "codeType": "PAYMENT_METHOD",
    "description": "Payment method types",
    "status": "ACTIVE"
  }
}
```

---

### Test 1.6: Get Code by Type

**Request:**
```bash
curl -X GET http://localhost:8080/api/configuration/codes/type/PAYMENT_METHOD \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response:**
```json
{
  "success": 1,
  "code": 200,
  "message": "Code retrieved successfully",
  "data": {
    "id": 1,
    "codeType": "PAYMENT_METHOD",
    "description": "Payment method types",
    "status": "ACTIVE"
  }
}
```

---

### Test 1.7: Update Code

**Request:**
```bash
curl -X PUT http://localhost:8080/api/configuration/codes/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeType": "PAYMENT_METHOD",
    "description": "Updated: Payment method types for the system"
  }'
```

**Expected Response:**
```json
{
  "success": 1,
  "code": 200,
  "message": "Code updated successfully",
  "data": {
    "id": 1,
    "codeType": "PAYMENT_METHOD",
    "description": "Updated: Payment method types for the system",
    "status": "ACTIVE"
  }
}
```

---

## Code Value Management Tests

### Test 2.1: Create Payment Method Values

**Create CREDIT_CARD:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 1,
    "value": "CREDIT_CARD",
    "display": "Credit Card",
    "description": "Credit Card payment method",
    "orderPosition": 1
  }'
```

**Expected Response:**
```json
{
  "success": 1,
  "code": 200,
  "message": "CodeValue created successfully",
  "data": {
    "id": 1,
    "codeId": 1,
    "value": "CREDIT_CARD",
    "display": "Credit Card",
    "description": "Credit Card payment method",
    "orderPosition": 1,
    "status": "ACTIVE"
  }
}
```

**Save:** `CC_VALUE_ID=1`

---

**Create DEBIT_CARD:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 1,
    "value": "DEBIT_CARD",
    "display": "Debit Card",
    "description": "Debit Card payment method",
    "orderPosition": 2
  }'
```

---

**Create UPI:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 1,
    "value": "UPI",
    "display": "UPI Payment",
    "description": "Unified Payments Interface",
    "orderPosition": 3
  }'
```

---

### Test 2.2: Create User Status Values

**Create ACTIVE:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 2,
    "value": "ACTIVE",
    "display": "Active",
    "description": "User account is active",
    "orderPosition": 1
  }'
```

---

**Create INACTIVE:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 2,
    "value": "INACTIVE",
    "display": "Inactive",
    "description": "User account is inactive",
    "orderPosition": 2
  }'
```

---

**Create SUSPENDED:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 2,
    "value": "SUSPENDED",
    "display": "Suspended",
    "description": "User account is suspended",
    "orderPosition": 3
  }'
```

---

### Test 2.3: Create Order Status Values

**Create PENDING:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 3,
    "value": "PENDING",
    "display": "Pending",
    "description": "Order is pending",
    "orderPosition": 1
  }'
```

---

**Create CONFIRMED:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 3,
    "value": "CONFIRMED",
    "display": "Confirmed",
    "description": "Order is confirmed",
    "orderPosition": 2
  }'
```

---

**Create SHIPPED:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 3,
    "value": "SHIPPED",
    "display": "Shipped",
    "description": "Order has been shipped",
    "orderPosition": 3
  }'
```

---

**Create DELIVERED:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 3,
    "value": "DELIVERED",
    "display": "Delivered",
    "description": "Order has been delivered",
    "orderPosition": 4
  }'
```

---

### Test 2.4: Get Code Values by Code ID

**Request:**
```bash
curl -X GET http://localhost:8080/api/configuration/code-values/code/1 \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response (ordered by position):**
```json
{
  "success": 1,
  "code": 200,
  "message": "CodeValues retrieved successfully",
  "data": [
    {
      "id": 1,
      "codeId": 1,
      "value": "CREDIT_CARD",
      "display": "Credit Card",
      "orderPosition": 1,
      "status": "ACTIVE"
    },
    {
      "id": 2,
      "codeId": 1,
      "value": "DEBIT_CARD",
      "display": "Debit Card",
      "orderPosition": 2,
      "status": "ACTIVE"
    },
    {
      "id": 3,
      "codeId": 1,
      "value": "UPI",
      "display": "UPI Payment",
      "orderPosition": 3,
      "status": "ACTIVE"
    }
  ]
}
```

---

### Test 2.5: Get Dropdown Options (PUBLIC API - NO AUTH)

**Request:**
```bash
curl http://localhost:8080/api/configuration/code-values/dropdown/PAYMENT_METHOD
```

**Expected Response:**
```json
{
  "success": 1,
  "code": 200,
  "message": "Dropdown options retrieved successfully",
  "data": [
    {
      "id": 1,
      "codeId": 1,
      "value": "CREDIT_CARD",
      "display": "Credit Card",
      "orderPosition": 1,
      "status": "ACTIVE"
    },
    {
      "id": 2,
      "codeId": 1,
      "value": "DEBIT_CARD",
      "display": "Debit Card",
      "orderPosition": 2,
      "status": "ACTIVE"
    },
    {
      "id": 3,
      "codeId": 1,
      "value": "UPI",
      "display": "UPI Payment",
      "orderPosition": 3,
      "status": "ACTIVE"
    }
  ]
}
```

---

**Get USER_STATUS Dropdown:**
```bash
curl http://localhost:8080/api/configuration/code-values/dropdown/USER_STATUS
```

---

**Get ORDER_STATUS Dropdown:**
```bash
curl http://localhost:8080/api/configuration/code-values/dropdown/ORDER_STATUS
```

---

### Test 2.6: Update Code Value

**Request:**
```bash
curl -X PUT http://localhost:8080/api/configuration/code-values/1 \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 1,
    "value": "CREDIT_CARD",
    "display": "Visa/Mastercard",
    "description": "Updated: Credit Card payment method",
    "orderPosition": 1
  }'
```

**Expected Response:**
```json
{
  "success": 1,
  "code": 200,
  "message": "CodeValue updated successfully",
  "data": {
    "id": 1,
    "codeId": 1,
    "value": "CREDIT_CARD",
    "display": "Visa/Mastercard",
    "description": "Updated: Credit Card payment method",
    "orderPosition": 1,
    "status": "ACTIVE"
  }
}
```

---

### Test 2.7: Delete Code Value

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/configuration/code-values/1 \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response:**
```json
{
  "success": 1,
  "code": 200,
  "message": "CodeValue deleted successfully"
}
```

---

### Test 2.8: Delete Code

**Request:**
```bash
curl -X DELETE http://localhost:8080/api/configuration/codes/1 \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response:**
```json
{
  "success": 1,
  "code": 200,
  "message": "Code deleted successfully"
}
```

---

## ❌ Error Test Cases

### Test 3.1: Create Duplicate Code Type

**Request:**
```bash
curl -X POST http://localhost:8080/api/configuration/codes \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeType": "PAYMENT_METHOD",
    "description": "Duplicate"
  }'
```

**Expected Response:**
```json
{
  "success": 0,
  "code": 400,
  "message": "Code type already exists: PAYMENT_METHOD"
}
```

---

### Test 3.2: Create Value for Non-existent Code

**Request:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 999,
    "value": "INVALID",
    "display": "Invalid",
    "orderPosition": 1
  }'
```

**Expected Response:**
```json
{
  "success": 0,
  "code": 500,
  "message": "Failed to create code value: Code not found with id: 999"
}
```

---

### Test 3.3: Get Non-existent Code

**Request:**
```bash
curl -X GET http://localhost:8080/api/configuration/codes/999 \
  -H "Authorization: Bearer $TOKEN"
```

**Expected Response:**
```json
{
  "success": 0,
  "code": 404,
  "message": "Code not found with id: 999"
}
```

---

### Test 3.4: Access Protected API Without Token

**Request:**
```bash
curl -X POST http://localhost:8080/api/configuration/codes \
  -H "Content-Type: application/json" \
  -d '{
    "codeType": "PAYMENT_METHOD",
    "description": "Test"
  }'
```

**Expected Response:**
```
Status: 401 Unauthorized
Body: Missing Token. Please Inform Authorized Person.
```

---

## 📊 Summary

- **Total Code Tests**: 7
- **Total Code Value Tests**: 8
- **Error Tests**: 4
- **Public API Tests**: 3 (dropdown endpoints)

All tests should pass with the expected responses shown above.

