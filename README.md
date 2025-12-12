# Sports Equipment Online Store

A full-stack e-commerce web application for browsing and purchasing sports equipment and apparel, built with Spring Boot, Thymeleaf, and Bootstrap 5.

## Team Information

**Team Member:** Hudson Latimer
**Student ID:** W0510975
**Roles:**
- Frontend Developer
- Backend Developer
- Database Designer
- Testing & Documentation Lead

## Project Overview

This project is a complete online store application that allows users to:
- Browse sports equipment across multiple categories (Basketball, Soccer, Running, Fitness, Swimming, Hockey)
- Register for an account with secure password hashing
- Login and maintain a user profile
- Add products to a shopping cart (guest and authenticated sessions)
- Complete the checkout process with shipping information
- View order history
- Admin users can manage products (add, edit, delete)

## Technology Stack

- **Backend:** Spring Boot 3.2.0, Java 17
- **Security:** Spring Security with BCrypt password hashing
- **Database:** H2 (in-memory) with JPA/Hibernate ORM
- **Frontend:** Thymeleaf, Bootstrap 5, Bootstrap Icons
- **Build Tool:** Maven
- **Testing:** JUnit 5, Mockito, MockMVC

## Features Implemented

### Core Features (Priority 1)
✅ User Registration with email validation and BCrypt password hashing
✅ User Login/Logout with Spring Security sessions
✅ Product Catalog with grid layout, images, prices, and stock levels
✅ Product Details page with full information and add-to-cart
✅ Add items to cart with quantity selection and stock validation
✅ View cart with item list, subtotals, and total
✅ Remove items from cart
✅ Checkout process with shipping information collection
✅ Order confirmation with database storage and order number

### Enhanced Features (Priority 2)
✅ Category filtering (6 categories)
✅ Product search functionality
✅ Update cart quantities
✅ Order history in user profile
✅ Stock management and validation

### Admin Features (Priority 3)
✅ Admin product management (ROLE_ADMIN)
✅ Add, edit, and delete products
✅ Stock level monitoring

## Database Schema

### Entities
1. **User** - User accounts with authentication
2. **Category** - Product categories (Basketball, Soccer, etc.)
3. **Product** - Sports equipment items
4. **CartItem** - Shopping cart items (session or user-based)
5. **Order** - Completed orders
6. **OrderItem** - Individual items in an order

### Relationships
- User (1) → (Many) Orders
- User (1) → (Many) CartItems
- Category (1) → (Many) Products
- Product (1) → (Many) CartItems
- Product (1) → (Many) OrderItems
- Order (1) → (Many) OrderItems

## Project Structure

```
src/
├── main/
│   ├── java/com/sportsstore/
│   │   ├── SportsStoreApplication.java          # Main application class
│   │   ├── config/
│   │   │   └── SecurityConfig.java              # Spring Security configuration
│   │   ├── controller/
│   │   │   ├── HomeController.java              # Homepage
│   │   │   ├── UserController.java              # Registration, login, profile
│   │   │   ├── ProductController.java           # Product listing and details
│   │   │   ├── CartController.java              # Shopping cart
│   │   │   ├── CheckoutController.java          # Checkout and order confirmation
│   │   │   └── AdminController.java             # Admin product management
│   │   ├── model/
│   │   │   ├── User.java                        # User entity
│   │   │   ├── Category.java                    # Category entity
│   │   │   ├── Product.java                     # Product entity
│   │   │   ├── CartItem.java                    # CartItem entity
│   │   │   ├── Order.java                       # Order entity
│   │   │   └── OrderItem.java                   # OrderItem entity
│   │   ├── repository/
│   │   │   ├── UserRepository.java              # User data access
│   │   │   ├── CategoryRepository.java          # Category data access
│   │   │   ├── ProductRepository.java           # Product data access with custom queries
│   │   │   ├── CartItemRepository.java          # CartItem data access
│   │   │   └── OrderRepository.java             # Order data access
│   │   └── service/
│   │       ├── CustomUserDetailsService.java    # Spring Security UserDetailsService
│   │       ├── UserService.java                 # User business logic
│   │       ├── CategoryService.java             # Category business logic
│   │       ├── ProductService.java              # Product business logic
│   │       ├── CartService.java                 # Cart business logic with session support
│   │       └── OrderService.java                # Order business logic
│   └── resources/
│       ├── templates/
│       │   ├── fragments/
│       │   │   ├── header.html                  # Navigation bar fragment
│       │   │   └── footer.html                  # Footer fragment
│       │   ├── admin/
│       │   │   ├── products.html                # Admin product list
│       │   │   └── product-form.html            # Admin product form
│       │   ├── index.html                       # Homepage
│       │   ├── register.html                    # Registration page
│       │   ├── login.html                       # Login page
│       │   ├── products.html                    # Product catalog
│       │   ├── product-detail.html              # Product details
│       │   ├── cart.html                        # Shopping cart
│       │   ├── checkout.html                    # Checkout page
│       │   ├── order-confirmation.html          # Order confirmation
│       │   └── profile.html                     # User profile & order history
│       ├── application.properties               # Application configuration
│       └── data.sql                             # Seed data (6 categories, 20 products, 3 users)
└── test/
    └── java/com/sportsstore/
        ├── service/
        │   └── UserServiceTest.java             # UserService unit tests
        └── controller/
            └── ProductControllerTest.java       # ProductController integration tests
```

## Installation & Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6 or higher

### Steps

1. **Clone or extract the project**
   ```bash
   cd "c:\Users\HGL\Desktop\Web Application Programming\Final"
   ```

2. **Build the project**
   ```bash
   mvn clean install
   ```

3. **Run the application**
   ```bash
   mvn spring-boot:run
   ```

4. **Access the application**
   - Main Application: http://localhost:8080
   - H2 Console: http://localhost:8080/h2-console
     - JDBC URL: `jdbc:h2:mem:storedb`
     - Username: `sa`
     - Password: (leave blank)

## Default User Credentials

The application comes with pre-seeded users for testing:

### Admin Account
- **Username:** `admin`
- **Password:** `password123`
- **Access:** Full admin privileges including product management

### Regular User Accounts
- **Username:** `user1` / **Password:** `password123`
- **Username:** `johndoe` / **Password:** `password123`

## MVC Architecture

### Model
- Entity classes representing database tables
- JPA annotations for ORM mapping
- Validation constraints for data integrity

### View
- Thymeleaf templates with Bootstrap 5 styling
- Reusable fragments (header, footer)
- Responsive design for mobile and desktop
- Security-aware navigation (sec:authorize)

### Controller
- REST-like URL mappings
- Request parameter handling
- Form validation with BindingResult
- RedirectAttributes for flash messages
- Authentication and authorization checks

### Workflow Example (Add to Cart)
1. User clicks "Add to Cart" on product page
2. POST request to `/cart/add` with productId and quantity
3. CartController receives request
4. CartService checks if user is authenticated
   - If authenticated: saves to user's cart in database
   - If guest: saves to session-based cart
5. Stock validation performed
6. Cart item created/updated
7. Success message added to flash attributes
8. Redirect to cart page
9. CartController loads cart items
10. Thymeleaf renders cart.html with items and total

## Security Features

✅ **Password Hashing:** BCrypt with strength 10
✅ **Authentication:** Form-based login with Spring Security
✅ **Authorization:** Role-based access control (USER, ADMIN)
✅ **SQL Injection Prevention:** JPA parameterized queries
✅ **CSRF Protection:** Enabled for all POST requests
✅ **Session Management:** HttpOnly cookies, 30-minute timeout
✅ **Input Validation:** Jakarta Validation annotations

### Protected Routes
- `/cart/**` - Requires authentication
- `/checkout/**` - Requires authentication
- `/profile/**` - Requires authentication
- `/admin/**` - Requires ROLE_ADMIN

### Public Routes
- `/`, `/home` - Homepage
- `/products/**` - Product browsing
- `/register` - User registration
- `/login` - User login

## Session Management

### Guest Users
- Cart stored with session ID
- 30-minute session timeout
- Session-based cart items in database

### Authenticated Users
- Cart linked to User entity
- Persistent across devices
- Cart merging on login (guest cart + user cart)

### Cart Merging Logic
When a guest user logs in:
1. System retrieves guest cart (by session ID)
2. System retrieves user cart (by User entity)
3. For each guest item:
   - If product already in user cart: quantities are added
   - If product not in user cart: item moved to user cart
4. Guest cart items deleted
5. User sees combined cart

## Testing

### Unit Tests (JUnit 5 + Mockito)
**UserServiceTest** - Tests user registration, authentication, and validation
- ✅ Successful user registration
- ✅ Username already exists exception
- ✅ Email already exists exception
- ✅ Find user by username
- ✅ Check username/email existence

### Integration Tests (MockMVC)
**ProductControllerTest** - Tests product listing and details endpoints
- ✅ List all products
- ✅ Filter products by category
- ✅ Search products
- ✅ View product details
- ✅ Handle product not found

### Running Tests
```bash
mvn test
```

## Key Technologies & Concepts

### Spring Boot Features Used
- Spring Data JPA with Hibernate
- Spring Security with custom UserDetailsService
- Spring MVC with Thymeleaf
- Form validation with Jakarta Bean Validation
- Session management
- Flash attributes for messaging

### Database Features
- H2 in-memory database
- JPA entity relationships (OneToMany, ManyToOne)
- Custom JPQL queries
- Transaction management (@Transactional)
- Automatic schema generation (ddl-auto)
- Data initialization (data.sql)

### Frontend Features
- Bootstrap 5 responsive grid
- Bootstrap Icons
- Thymeleaf template fragments
- Form handling with Thymeleaf
- Conditional rendering (th:if)
- Security integration (sec:authorize)

## API Endpoints

### Public Endpoints
- `GET /` - Homepage
- `GET /products` - Product list (with optional filters)
- `GET /products/{id}` - Product details
- `GET /register` - Registration form
- `POST /register` - Submit registration
- `GET /login` - Login form
- `POST /login` - Submit login

### Authenticated Endpoints
- `GET /cart` - View cart
- `POST /cart/add` - Add item to cart
- `POST /cart/update/{id}` - Update cart item quantity
- `POST /cart/remove/{id}` - Remove item from cart
- `GET /checkout` - Checkout form
- `POST /checkout` - Process order
- `GET /order-confirmation/{id}` - Order confirmation
- `GET /profile` - User profile and order history
- `POST /logout` - Logout

### Admin Endpoints
- `GET /admin/products` - Product management
- `GET /admin/products/new` - Add product form
- `GET /admin/products/edit/{id}` - Edit product form
- `POST /admin/products/save` - Save product
- `POST /admin/products/delete/{id}` - Delete product

## Future Enhancements

- Product reviews and ratings
- Advanced filtering (price range, brand)
- Payment gateway integration
- Email notifications for orders
- Product image uploads
- Wishlist functionality
- Inventory alerts for admins
- Sales analytics dashboard
- Multi-address support
- Order tracking
- Promotional codes/discounts

## References

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Thymeleaf Documentation](https://www.thymeleaf.org/)
- [Bootstrap 5 Documentation](https://getbootstrap.com/)
- [W3Schools HTML/CSS/JavaScript](https://www.w3schools.com/)
- [BCrypt Password Hashing](https://www.baeldung.com/spring-security-registration-password-encoding-bcrypt)
- [MockMVC Testing Reference](https://spring.io/guides/gs/testing-web/)


