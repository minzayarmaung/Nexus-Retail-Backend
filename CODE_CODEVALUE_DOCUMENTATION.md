# Code and CodeValue Flow - Dynamic Dropdown Implementation

## 📋 Overview

This implementation provides a complete dynamic dropdown system using Code and CodeValue entities, allowing administrators to manage dropdown options through CRUD APIs without code changes.

## 📊 Entity Relationships

```
Code (1) ─── (Many) CodeValue
├── id (PK)
├── code_type (UNIQUE)
├── description
└── status (inherited from Auditable)

CodeValue
├── id (PK)
├── code_id (FK)
├── value (UNIQUE per code)
├── display
├── description
├── order_position
└── status (inherited from Auditable)
```

## 🏗️ Project Structure

```
feature/configuration/
├── controller/
│   ├── CodeController.java
│   └── CodeValueController.java
├── service/
│   ├── CodeService.java
│   ├── CodeValueService.java
│   └── impl/
│       ├── CodeServiceImpl.java
│       └── CodeValueServiceImpl.java
└── dto/
    ├── request/
    │   ├── CodeRequest.java
    │   └── CodeValueRequest.java
    └── response/
        ├── CodeResponse.java
        └── CodeValueResponse.java

data/models/
├── Code.java
└── CodeValue.java

data/repositories/
├── CodeRepository.java
└── CodeValueRepository.java
```

## 🔌 API Endpoints

### Code Management APIs

#### 1. Create Code
```
POST /api/configuration/codes
Authorization: Bearer {token}

Request Body:
{
  "codeType": "PAYMENT_METHOD",
  "description": "Payment method types"
}

Response:
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

#### 2. Get All Codes
```
GET /api/configuration/codes
Authorization: Bearer {token}

Response:
{
  "success": 1,
  "code": 200,
  "message": "Codes retrieved successfully",
  "data": [
    {
      "id": 1,
      "codeType": "PAYMENT_METHOD",
      "description": "Payment method types",
      "status": "ACTIVE"
    }
  ]
}
```

#### 3. Get Code by ID
```
GET /api/configuration/codes/{id}
Authorization: Bearer {token}

Response:
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

#### 4. Get Code by Type
```
GET /api/configuration/codes/type/{codeType}
Authorization: Bearer {token}

Response:
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

#### 5. Update Code
```
PUT /api/configuration/codes/{id}
Authorization: Bearer {token}

Request Body:
{
  "codeType": "PAYMENT_METHOD",
  "description": "Updated description"
}

Response:
{
  "success": 1,
  "code": 200,
  "message": "Code updated successfully",
  "data": {
    "id": 1,
    "codeType": "PAYMENT_METHOD",
    "description": "Updated description",
    "status": "ACTIVE"
  }
}
```

#### 6. Delete Code
```
DELETE /api/configuration/codes/{id}
Authorization: Bearer {token}

Response:
{
  "success": 1,
  "code": 200,
  "message": "Code deleted successfully"
}
```

### Code Value Management APIs

#### 1. Create Code Value
```
POST /api/configuration/code-values
Authorization: Bearer {token}

Request Body:
{
  "codeId": 1,
  "value": "CREDIT_CARD",
  "display": "Credit Card",
  "description": "Credit Card payment method",
  "orderPosition": 1
}

Response:
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

#### 2. Get Code Values by Code ID
```
GET /api/configuration/code-values/code/{codeId}
Authorization: Bearer {token}

Response:
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
      "description": "Credit Card payment method",
      "orderPosition": 1,
      "status": "ACTIVE"
    },
    {
      "id": 2,
      "codeId": 1,
      "value": "DEBIT_CARD",
      "display": "Debit Card",
      "description": "Debit Card payment method",
      "orderPosition": 2,
      "status": "ACTIVE"
    }
  ]
}
```

#### 3. Get Code Value by ID
```
GET /api/configuration/code-values/{id}
Authorization: Bearer {token}

Response:
{
  "success": 1,
  "code": 200,
  "message": "CodeValue retrieved successfully",
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

#### 4. Get Dropdown Options (PUBLIC - No Auth Required) ⭐
```
GET /api/configuration/code-values/dropdown/{codeType}

Response:
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
      "description": "Credit Card payment method",
      "orderPosition": 1,
      "status": "ACTIVE"
    },
    {
      "id": 2,
      "codeId": 1,
      "value": "DEBIT_CARD",
      "display": "Debit Card",
      "description": "Debit Card payment method",
      "orderPosition": 2,
      "status": "ACTIVE"
    }
  ]
}
```

#### 5. Update Code Value
```
PUT /api/configuration/code-values/{id}
Authorization: Bearer {token}

Request Body:
{
  "codeId": 1,
  "value": "CREDIT_CARD",
  "display": "Updated Credit Card",
  "description": "Updated description",
  "orderPosition": 1
}

Response:
{
  "success": 1,
  "code": 200,
  "message": "CodeValue updated successfully",
  "data": {
    "id": 1,
    "codeId": 1,
    "value": "CREDIT_CARD",
    "display": "Updated Credit Card",
    "description": "Updated description",
    "orderPosition": 1,
    "status": "ACTIVE"
  }
}
```

#### 6. Delete Code Value
```
DELETE /api/configuration/code-values/{id}
Authorization: Bearer {token}

Response:
{
  "success": 1,
  "code": 200,
  "message": "CodeValue deleted successfully"
}
```

## 🔐 Security

- **Protected Endpoints**: All Code and CodeValue management APIs require JWT authentication
- **Public API**: `GET /api/configuration/code-values/dropdown/{codeType}` is public for UI dropdown population
- **Added to Whitelist**: `/api/configuration/code-values/dropdown/**` in SecurityConstants

## 💡 Usage Examples

### Example 1: Payment Methods Dropdown

1. **Create Code:**
```bash
curl -X POST http://localhost:8080/api/configuration/codes \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "codeType": "PAYMENT_METHOD",
    "description": "Payment method types"
  }'
```

2. **Add Values:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 1,
    "value": "CREDIT_CARD",
    "display": "Credit Card",
    "orderPosition": 1
  }'

curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 1,
    "value": "DEBIT_CARD",
    "display": "Debit Card",
    "orderPosition": 2
  }'
```

3. **Get Dropdown (Frontend - No Auth):**
```bash
curl http://localhost:8080/api/configuration/code-values/dropdown/PAYMENT_METHOD
```

### Example 2: User Status Dropdown

1. **Create Code:**
```bash
curl -X POST http://localhost:8080/api/configuration/codes \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "codeType": "USER_STATUS",
    "description": "User account status"
  }'
```

2. **Add Values:**
```bash
curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 2,
    "value": "ACTIVE",
    "display": "Active",
    "orderPosition": 1
  }'

curl -X POST http://localhost:8080/api/configuration/code-values \
  -H "Authorization: Bearer {token}" \
  -H "Content-Type: application/json" \
  -d '{
    "codeId": 2,
    "value": "INACTIVE",
    "display": "Inactive",
    "orderPosition": 2
  }'
```

## 🎯 Key Features

✅ **Dynamic Configuration** - No code changes needed to add/modify dropdown options
✅ **Ordered Display** - Code values support order_position for custom sorting
✅ **Public API** - Dropdown options accessible without authentication
✅ **Complete CRUD** - Full Create, Read, Update, Delete operations
✅ **Unique Constraints** - Code type and values are appropriately unique
✅ **Auditing** - Auto-tracking of creation/modification/deletion by user
✅ **Status Management** - Built-in status field for active/inactive toggling
✅ **Swagger Documentation** - All endpoints documented with request/response examples
✅ **Error Handling** - Comprehensive error responses with HTTP status codes

## 📝 Database Schema

### codes table
```sql
CREATE TABLE codes (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code_type VARCHAR(255) NOT NULL UNIQUE,
    description VARCHAR(500),
    status INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    created_by BIGINT,
    last_modified_by BIGINT,
    deleted_by BIGINT,
    INDEX index_code_type (code_type)
);
```

### code_values table
```sql
CREATE TABLE code_values (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code_id BIGINT NOT NULL,
    value VARCHAR(255) NOT NULL,
    display VARCHAR(255) NOT NULL,
    description VARCHAR(500),
    order_position INT NOT NULL,
    status INT,
    created_at TIMESTAMP,
    updated_at TIMESTAMP,
    deleted_at TIMESTAMP,
    created_by BIGINT,
    last_modified_by BIGINT,
    deleted_by BIGINT,
    FOREIGN KEY (code_id) REFERENCES codes(id),
    INDEX index_code_id (code_id),
    INDEX index_code_value (value)
);
```

## ✅ Compilation Status

**BUILD SUCCESSFUL** - All components compile without errors

## 🚀 Next Steps

1. Start the application
2. Login to get JWT token
3. Access Swagger UI at `http://localhost:8080/swagger-ui.html`
4. Create codes and values through the Code Management APIs
5. Access dropdown options publicly using the `/dropdown/{codeType}` endpoint

