# Architecture Diagram - Sports Equipment Online Store

## MVC Architecture Overview

```
┌─────────────────────────────────────────────────────────────────────┐
│                           CLIENT (Browser)                          │
│                    Bootstrap 5 + Thymeleaf Views                    │
└──────────────────────────────┬──────────────────────────────────────┘
                               │ HTTP Request/Response
                               │
┌──────────────────────────────▼──────────────────────────────────────┐
│                     SPRING BOOT APPLICATION                         │
│                                                                     │
│  ┌────────────────────────────────────────────────────────────┐   │
│  │                    SECURITY LAYER                          │   │
│  │  ┌──────────────────────────────────────────────────┐     │   │
│  │  │         Spring Security Filter Chain             │     │   │
│  │  │  - Authentication Filter                         │     │   │
│  │  │  - Authorization Filter                          │     │   │
│  │  │  - CSRF Protection                               │     │   │
│  │  │  - Session Management                            │     │   │
│  │  └──────────────────────────────────────────────────┘     │   │
│  │  ┌──────────────────────────────────────────────────┐     │   │
│  │  │      CustomUserDetailsService                    │     │   │
│  │  │  - loadUserByUsername()                          │     │   │
│  │  │  - BCrypt password verification                  │     │   │
│  │  └──────────────────────────────────────────────────┘     │   │
│  └────────────────────────────────────────────────────────────┘   │
│                               │                                     │
│  ┌────────────────────────────▼────────────────────────────────┐  │
│  │                  CONTROLLER LAYER                           │  │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │  │
│  │  │HomeController│ │UserController│ │ProductCtrl   │        │  │
│  │  │              │ │              │ │              │        │  │
│  │  │GET /         │ │GET /register │ │GET /products │        │  │
│  │  │GET /home     │ │POST /register│ │GET /prod/{id}│        │  │
│  │  └──────────────┘ │GET /login    │ └──────────────┘        │  │
│  │                   │GET /profile  │                          │  │
│  │  ┌──────────────┐ └──────────────┘ ┌──────────────┐        │  │
│  │  │CartController│                  │CheckoutCtrl  │        │  │
│  │  │              │ ┌──────────────┐ │              │        │  │
│  │  │GET /cart     │ │AdminCtrl     │ │GET /checkout │        │  │
│  │  │POST /cart/add│ │              │ │POST /checkout│        │  │
│  │  │POST /cart/upd│ │GET /admin/** │ │GET /order-   │        │  │
│  │  │POST /cart/del│ │POST /admin/**│ │ confirmation │        │  │
│  │  └──────────────┘ └──────────────┘ └──────────────┘        │  │
│  └──────────────────────────┬──────────────────────────────────┘  │
│                             │ Calls                                │
│  ┌──────────────────────────▼──────────────────────────────────┐  │
│  │                   SERVICE LAYER                             │  │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │  │
│  │  │ UserService  │ │CategorySvc   │ │ProductSvc    │        │  │
│  │  │              │ │              │ │              │        │  │
│  │  │registerUser()│ │findAll()     │ │findAll()     │        │  │
│  │  │findByUsernam │ │findById()    │ │findById()    │        │  │
│  │  │existsByEmail │ │save()        │ │findByCategory│        │  │
│  │  └──────────────┘ └──────────────┘ │searchProducts│        │  │
│  │                                     │reduceStock() │        │  │
│  │  ┌──────────────┐ ┌──────────────┐ └──────────────┘        │  │
│  │  │ CartService  │ │ OrderService │                          │  │
│  │  │              │ │              │                          │  │
│  │  │getGuestCart()│ │createOrder() │                          │  │
│  │  │getUserCart() │ │findUserOrder │                          │  │
│  │  │addToCart()   │ │findById()    │                          │  │
│  │  │updateQty()   │ └──────────────┘                          │  │
│  │  │removeItem()  │                                           │  │
│  │  │mergeCart()   │                                           │  │
│  │  │calculateTotal│                                           │  │
│  │  └──────────────┘                                           │  │
│  └──────────────────────────┬──────────────────────────────────┘  │
│                             │ Uses                                 │
│  ┌──────────────────────────▼──────────────────────────────────┐  │
│  │                 REPOSITORY LAYER (DAO)                      │  │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │  │
│  │  │UserRepository│ │CategoryRepo  │ │ProductRepo   │        │  │
│  │  │extends JPA   │ │extends JPA   │ │extends JPA   │        │  │
│  │  │              │ │              │ │              │        │  │
│  │  │findByUsernam │ │findByName()  │ │findByCategory│        │  │
│  │  │findByEmail() │ │              │ │searchProducts│        │  │
│  │  │existsByUser  │ │              │ │              │        │  │
│  │  └──────────────┘ └──────────────┘ └──────────────┘        │  │
│  │                                                              │  │
│  │  ┌──────────────┐ ┌──────────────┐                          │  │
│  │  │CartItemRepo  │ │OrderRepo     │                          │  │
│  │  │extends JPA   │ │extends JPA   │                          │  │
│  │  │              │ │              │                          │  │
│  │  │findBySession │ │findByUser    │                          │  │
│  │  │findByUser()  │ │findByStatus()│                          │  │
│  │  │deleteBySession│ │              │                          │  │
│  │  └──────────────┘ └──────────────┘                          │  │
│  └──────────────────────────┬──────────────────────────────────┘  │
│                             │ JPA/Hibernate                        │
│  ┌──────────────────────────▼──────────────────────────────────┐  │
│  │                      MODEL LAYER                            │  │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │  │
│  │  │   User       │ │  Category    │ │  Product     │        │  │
│  │  │  @Entity     │ │  @Entity     │ │  @Entity     │        │  │
│  │  │              │ │              │ │              │        │  │
│  │  │id, username  │ │id, name      │ │id, name      │        │  │
│  │  │email, pwd    │ │description   │ │description   │        │  │
│  │  │role          │ │              │ │price, stock  │        │  │
│  │  │              │ │              │ │category FK   │        │  │
│  │  └──────────────┘ └──────────────┘ └──────────────┘        │  │
│  │                                                              │  │
│  │  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐        │  │
│  │  │  CartItem    │ │   Order      │ │  OrderItem   │        │  │
│  │  │  @Entity     │ │  @Entity     │ │  @Entity     │        │  │
│  │  │              │ │              │ │              │        │  │
│  │  │id, product FK│ │id, user FK   │ │id, order FK  │        │  │
│  │  │quantity      │ │orderDate     │ │product FK    │        │  │
│  │  │sessionId     │ │totalAmount   │ │quantity      │        │  │
│  │  │user FK       │ │status        │ │priceAtPurch  │        │  │
│  │  └──────────────┘ │shipping info │ └──────────────┘        │  │
│  │                   └──────────────┘                          │  │
│  └──────────────────────────┬──────────────────────────────────┘  │
│                             │                                      │
└─────────────────────────────┼──────────────────────────────────────┘
                              │
                    ┌─────────▼──────────┐
                    │   H2 DATABASE      │
                    │   (In-Memory)      │
                    │                    │
                    │ jdbc:h2:mem:storedb│
                    └────────────────────┘
```

## Request Flow Example: "Add Product to Cart"

```
1. USER ACTION
   │
   ├─► User clicks "Add to Cart" button on product page
   │
2. HTTP REQUEST
   │
   ├─► POST /cart/add
   │   Parameters: productId=5, quantity=2
   │
3. SECURITY LAYER
   │
   ├─► Spring Security intercepts request
   ├─► Checks if user is authenticated
   ├─► Validates CSRF token
   ├─► Creates/retrieves session
   │
4. CONTROLLER
   │
   ├─► CartController.addToCart() receives request
   ├─► Extracts parameters: productId, quantity
   ├─► Gets authentication status
   │
5. SERVICE LAYER
   │
   ├─► CartService.addToCart() is called
   ├─► If authenticated:
   │   ├─► UserService.findByUsername() → Get User entity
   │   └─► CartService.addToUserCart(user, productId, quantity)
   │
   ├─► If guest:
   │   ├─► HttpSession.getId() → Get session ID
   │   └─► CartService.addToGuestCart(sessionId, productId, quantity)
   │
6. REPOSITORY LAYER
   │
   ├─► ProductRepository.findById(productId) → Get Product
   ├─► CartItemRepository.findByUserAndProduct() → Check existing
   │
   ├─► If exists:
   │   └─► Update quantity
   │
   ├─► If new:
   │   └─► Create new CartItem
   │
   └─► CartItemRepository.save() → Persist to database
   │
7. DATABASE
   │
   ├─► INSERT or UPDATE cart_items table
   ├─► Transaction committed
   │
8. RESPONSE
   │
   ├─► CartController adds flash message: "Item added to cart"
   ├─► Redirects to /cart
   │
9. VIEW RENDERING
   │
   ├─► CartController.viewCart() is called
   ├─► CartService.getUserCart() or getGuestCart() → Fetch items
   ├─► CartService.calculateTotal() → Calculate total price
   ├─► Model populated with cartItems and total
   ├─► Thymeleaf renders cart.html
   │
10. USER SEES
    │
    └─► Cart page with added product displayed
```

## Component Interactions

### Registration Flow
```
Browser → UserController.register()
          ├─► UserService.registerUser()
          │   ├─► UserRepository.existsByUsername() → Check duplicate
          │   ├─► UserRepository.existsByEmail() → Check duplicate
          │   ├─► PasswordEncoder.encode() → Hash password
          │   └─► UserRepository.save() → Create user
          └─► Redirect to /login
```

### Login Flow
```
Browser → Spring Security /login
          ├─► CustomUserDetailsService.loadUserByUsername()
          │   └─► UserRepository.findByUsername() → Get User
          ├─► PasswordEncoder.matches() → Verify password
          ├─► Create Authentication object
          ├─► Create session
          ├─► CartService.mergeGuestCartToUser() → Merge carts
          └─► Redirect to /
```

### Checkout Flow
```
Browser → CheckoutController.processCheckout()
          ├─► UserService.findByUsername() → Get User
          ├─► CartService.getUserCart() → Get cart items
          ├─► OrderService.createOrder()
          │   ├─► CartService.validateCart() → Check stock
          │   ├─► CartService.calculateTotal() → Get total
          │   ├─► Create Order entity
          │   ├─► For each CartItem:
          │   │   ├─► Create OrderItem
          │   │   └─► ProductService.reduceStock()
          │   ├─► OrderRepository.save() → Persist order
          │   └─► CartService.clearUserCart() → Empty cart
          └─► Redirect to /order-confirmation/{id}
```

## Technology Stack Layers

### Presentation Layer
- **Thymeleaf:** Server-side template engine
- **Bootstrap 5:** CSS framework
- **Bootstrap Icons:** Icon library
- **HTML5/CSS3:** Markup and styling

### Application Layer
- **Spring MVC:** Web framework
- **Spring Security:** Authentication/Authorization
- **Validation:** Jakarta Bean Validation
- **Session Management:** HttpSession

### Business Logic Layer
- **Service Classes:** Business logic
- **Transaction Management:** @Transactional
- **Password Encoding:** BCrypt

### Data Access Layer
- **Spring Data JPA:** Repository abstraction
- **Hibernate:** ORM implementation
- **JPQL:** Query language

### Persistence Layer
- **H2 Database:** In-memory database
- **JDBC:** Database connectivity

## Design Patterns Used

1. **MVC Pattern:** Separation of concerns (Model, View, Controller)
2. **Repository Pattern:** Data access abstraction
3. **Service Layer Pattern:** Business logic encapsulation
4. **Dependency Injection:** Spring IoC container
5. **Front Controller:** DispatcherServlet
6. **Template Method:** Thymeleaf templates with fragments
7. **DTO Pattern:** Form objects for data transfer
8. **Facade Pattern:** Service layer facades

## Security Architecture

```
┌─────────────────────────────────────┐
│     HTTP Request                    │
└───────────────┬─────────────────────┘
                │
    ┌───────────▼──────────────┐
    │  Security Filter Chain   │
    ├──────────────────────────┤
    │ 1. CSRF Filter           │
    │ 2. Logout Filter         │
    │ 3. Authentication Filter │
    │ 4. Authorization Filter  │
    │ 5. Session Management    │
    └───────────┬──────────────┘
                │
    ┌───────────▼──────────────┐
    │  Authentication Manager  │
    ├──────────────────────────┤
    │ - UserDetailsService     │
    │ - PasswordEncoder        │
    └───────────┬──────────────┘
                │
    ┌───────────▼──────────────┐
    │  Authorization Manager   │
    ├──────────────────────────┤
    │ - hasRole()              │
    │ - isAuthenticated()      │
    └───────────┬──────────────┘
                │
    ┌───────────▼──────────────┐
    │  Security Context        │
    ├──────────────────────────┤
    │ - Authentication         │
    │ - Principal (User)       │
    └──────────────────────────┘
```

## Session Management Architecture

```
┌─────────────────────────────────────────┐
│        Guest User Session               │
├─────────────────────────────────────────┤
│ Session ID: ABC123                      │
│ Cart Items → session_id = 'ABC123'      │
│ Timeout: 30 minutes                     │
└─────────────────────────────────────────┘
                    │
                    │ User logs in
                    │
                    ▼
┌─────────────────────────────────────────┐
│     Authenticated User Session          │
├─────────────────────────────────────────┤
│ Session ID: XYZ789                      │
│ User: user1                             │
│ Cart Items → user_id = 1                │
│ Merged from: session 'ABC123'           │
│ Timeout: 30 minutes                     │
└─────────────────────────────────────────┘
```

## Configuration Structure

```
application.properties
├─► Server Config (port 8080)
├─► Database Config (H2)
├─► JPA Config (ddl-auto, logging)
├─► Thymeleaf Config (cache, prefix)
└─► Session Config (timeout, cookies)

SecurityConfig.java
├─► Password Encoder (BCrypt)
├─► Security Filter Chain
│   ├─► URL Authorization
│   ├─► Form Login
│   ├─► Logout
│   └─► CSRF Protection

data.sql
├─► Seed Categories (6)
├─► Seed Products (20)
└─► Seed Users (3)
```
