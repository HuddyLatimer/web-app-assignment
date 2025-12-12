# Entity-Relationship Diagram

## Sports Equipment Online Store Database Schema

```
┌─────────────────────────┐
│       CATEGORIES        │
├─────────────────────────┤
│ PK  id (BIGINT)        │
│     name (VARCHAR)      │
│     description (TEXT)  │
└─────────────────────────┘
           │
           │ 1
           │
           │ has
           │
           │ N
           ▼
┌─────────────────────────────────┐
│          PRODUCTS               │
├─────────────────────────────────┤
│ PK  id (BIGINT)                │
│     name (VARCHAR)              │
│     description (TEXT)          │
│     price (DECIMAL(10,2))       │
│     image_url (VARCHAR)         │
│     stock_quantity (INTEGER)    │
│ FK  category_id (BIGINT)        │
└─────────────────────────────────┘
           │
           │ 1
           │
           │ used in
           ├──────────────────┐
           │ N                │ N
           ▼                  ▼
┌────────────────────────┐  ┌─────────────────────────────┐
│      CART_ITEMS        │  │       ORDER_ITEMS           │
├────────────────────────┤  ├─────────────────────────────┤
│ PK  id (BIGINT)       │  │ PK  id (BIGINT)            │
│ FK  product_id        │  │ FK  order_id (BIGINT)       │
│     quantity (INT)     │  │ FK  product_id (BIGINT)     │
│     session_id (VAR)   │  │     quantity (INTEGER)      │
│ FK  user_id (BIGINT)   │  │     price_at_purchase (DEC) │
└────────────────────────┘  └─────────────────────────────┘
           ▲                           ▲
           │ N                         │ N
           │                           │
           │ has                       │ contains
           │ 1                         │ 1
           │                           │
┌─────────────────────────────────┐   │
│           USERS                 │   │
├─────────────────────────────────┤   │
│ PK  id (BIGINT)                │   │
│     username (VARCHAR) UNIQUE   │   │
│     email (VARCHAR) UNIQUE      │   │
│     password (VARCHAR)          │   │
│     role (VARCHAR)              │   │
│     created_at (TIMESTAMP)      │   │
└─────────────────────────────────┘   │
           │                           │
           │ 1                         │
           │                           │
           │ places                    │
           │                           │
           │ N                         │
           ▼                           │
┌─────────────────────────────────────┐
│            ORDERS                   │
├─────────────────────────────────────┤
│ PK  id (BIGINT)                    │
│ FK  user_id (BIGINT)                │
│     order_date (TIMESTAMP)          │
│     total_amount (DECIMAL(10,2))    │
│     status (VARCHAR)                │
│     shipping_address (VARCHAR)      │
│     shipping_city (VARCHAR)         │
│     shipping_zip (VARCHAR)          │
└─────────────────────────────────────┘
```

## Entity Descriptions

### USERS
Stores user account information including authentication credentials.
- **Primary Key:** id
- **Unique Constraints:** username, email
- **Security:** Passwords stored with BCrypt hashing (strength 10)
- **Roles:** ROLE_USER (default), ROLE_ADMIN

### CATEGORIES
Product categories for organizing sports equipment.
- **Primary Key:** id
- **Unique Constraint:** name
- **Examples:** Basketball, Soccer, Running, Fitness, Swimming, Hockey

### PRODUCTS
Sports equipment and apparel items available for purchase.
- **Primary Key:** id
- **Foreign Key:** category_id → CATEGORIES(id)
- **Price:** Stored as DECIMAL(10,2) for currency precision
- **Stock Management:** stock_quantity tracks availability

### CART_ITEMS
Shopping cart items for both guest and authenticated users.
- **Primary Key:** id
- **Foreign Keys:**
  - product_id → PRODUCTS(id)
  - user_id → USERS(id) (nullable for guest carts)
- **Session Support:** session_id stores session-based carts for guests
- **Dual Mode:** Can be linked to either user_id OR session_id

### ORDERS
Completed purchase orders with shipping information.
- **Primary Key:** id
- **Foreign Key:** user_id → USERS(id)
- **Status Values:** PENDING, CONFIRMED, SHIPPED, DELIVERED, CANCELLED
- **Timestamps:** order_date auto-generated on creation

### ORDER_ITEMS
Individual line items within an order.
- **Primary Key:** id
- **Foreign Keys:**
  - order_id → ORDERS(id)
  - product_id → PRODUCTS(id)
- **Price Snapshot:** price_at_purchase stores price at time of order (historical pricing)

## Relationships

### One-to-Many Relationships

1. **CATEGORIES → PRODUCTS** (1:N)
   - One category can have many products
   - Each product belongs to exactly one category
   - Cascade: Products are deleted when category is deleted

2. **USERS → CART_ITEMS** (1:N)
   - One user can have many cart items
   - Each cart item belongs to at most one user (nullable for guests)
   - Cascade: Cart items are deleted when user is deleted

3. **USERS → ORDERS** (1:N)
   - One user can place many orders
   - Each order belongs to exactly one user
   - Cascade: Orders are preserved when user is deleted (for records)

4. **PRODUCTS → CART_ITEMS** (1:N)
   - One product can appear in many cart items
   - Each cart item references exactly one product
   - No cascade: Cart items can be deleted independently

5. **PRODUCTS → ORDER_ITEMS** (1:N)
   - One product can appear in many order items
   - Each order item references exactly one product
   - No cascade: Order items preserved for historical data

6. **ORDERS → ORDER_ITEMS** (1:N)
   - One order contains many order items
   - Each order item belongs to exactly one order
   - Cascade: Order items are deleted when order is deleted

## Database Constraints

### Primary Keys
- All tables use auto-incrementing BIGINT primary keys

### Foreign Keys
- All foreign keys enforce referential integrity
- ON DELETE behavior varies by relationship

### Unique Constraints
- USERS: username, email
- CATEGORIES: name

### Not Null Constraints
- All primary keys
- USERS: username, email, password, role
- CATEGORIES: name
- PRODUCTS: name, price, stock_quantity, category_id
- CART_ITEMS: product_id, quantity
- ORDERS: user_id, order_date, total_amount, status, shipping fields
- ORDER_ITEMS: order_id, product_id, quantity, price_at_purchase

### Check Constraints (Application Level)
- PRODUCTS.price > 0
- PRODUCTS.stock_quantity >= 0
- CART_ITEMS.quantity >= 1
- ORDER_ITEMS.quantity >= 1

## Indexes

Indexes are automatically created on:
- Primary keys (id columns)
- Foreign keys (category_id, product_id, user_id, order_id)
- Unique constraints (username, email, category name)

Additional indexes could be added for:
- CART_ITEMS.session_id (for guest cart lookups)
- ORDERS.order_date (for date-based queries)
- PRODUCTS.name (for search queries)

## Data Types

### Numeric
- **BIGINT:** All ID fields (supports large datasets)
- **INTEGER:** Quantity fields
- **DECIMAL(10,2):** Currency fields (price, total_amount)

### String
- **VARCHAR(50):** usernames
- **VARCHAR(255):** emails, names, URLs
- **VARCHAR(500):** short descriptions
- **TEXT:** Long descriptions

### Date/Time
- **TIMESTAMP:** created_at, order_date

## Sample Data Queries

### Get all products in a category
```sql
SELECT p.* FROM products p
JOIN categories c ON p.category_id = c.id
WHERE c.name = 'Basketball';
```

### Get user's cart with product details
```sql
SELECT ci.*, p.name, p.price, p.stock_quantity
FROM cart_items ci
JOIN products p ON ci.product_id = p.id
WHERE ci.user_id = 1;
```

### Get guest cart by session
```sql
SELECT ci.*, p.name, p.price
FROM cart_items ci
JOIN products p ON ci.product_id = p.id
WHERE ci.session_id = 'ABC123XYZ';
```

### Get user's order history
```sql
SELECT o.*, COUNT(oi.id) as item_count
FROM orders o
LEFT JOIN order_items oi ON o.id = oi.order_id
WHERE o.user_id = 1
GROUP BY o.id
ORDER BY o.order_date DESC;
```

### Get order details with items
```sql
SELECT o.*, oi.*, p.name as product_name
FROM orders o
JOIN order_items oi ON o.id = oi.order_id
JOIN products p ON oi.product_id = p.id
WHERE o.id = 1;
```

## Database Configuration

- **Database Type:** H2 (in-memory)
- **JDBC URL:** jdbc:h2:mem:storedb
- **Schema Generation:** Hibernate auto-DDL (create-drop)
- **Data Initialization:** data.sql (seed data)
- **Transaction Management:** Spring @Transactional

## Security Considerations

1. **Password Storage:** BCrypt hashed passwords (never plaintext)
2. **SQL Injection Prevention:** JPA parameterized queries
3. **Prepared Statements:** All user input sanitized
4. **No Sensitive Data in Logs:** Passwords excluded from toString()
