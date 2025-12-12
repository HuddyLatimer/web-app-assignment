-- Insert Categories
INSERT INTO categories (id, name, description) VALUES
(1, 'Basketball', 'Basketball equipment and apparel'),
(2, 'Soccer', 'Soccer equipment and gear'),
(3, 'Running', 'Running shoes and accessories'),
(4, 'Fitness', 'Fitness and gym equipment'),
(5, 'Swimming', 'Swimming gear and accessories'),
(6, 'Hockey', 'Hockey equipment and apparel');

-- Insert Products
INSERT INTO products (id, name, description, price, image_url, stock_quantity, category_id) VALUES
-- Basketball Products
(1, 'Spalding NBA Official Basketball', 'Official NBA game basketball with superior grip and control', 89.99, NULL, 50, 1),
(2, 'Nike Basketball Shoes Pro', 'High-performance basketball shoes with excellent ankle support', 149.99, NULL, 30, 1),
(3, 'Basketball Hoop Portable', 'Adjustable height portable basketball hoop for home use', 299.99, NULL, 15, 1),
(4, 'Basketball Training Jersey', 'Moisture-wicking basketball jersey for intense training', 34.99, NULL, 100, 1),

-- Soccer Products
(5, 'Adidas Soccer Ball Pro', 'Professional match soccer ball with excellent flight characteristics', 79.99, NULL, 60, 2),
(6, 'Nike Mercurial Cleats', 'Lightweight soccer cleats for maximum speed and agility', 199.99, NULL, 40, 2),
(7, 'Soccer Goal Training Set', 'Portable soccer goal perfect for backyard training', 129.99, NULL, 20, 2),
(8, 'Soccer Shin Guards', 'Professional-grade shin guards with excellent protection', 24.99, NULL, 80, 2),

-- Running Products
(9, 'Nike Air Zoom Running Shoes', 'Cushioned running shoes for long-distance comfort', 159.99, NULL, 45, 3),
(10, 'Running Watch GPS', 'Advanced GPS running watch with heart rate monitor', 249.99, NULL, 25, 3),
(11, 'Compression Running Shorts', 'Lightweight compression shorts for optimal performance', 44.99, NULL, 70, 3),
(12, 'Hydration Running Belt', 'Water bottle belt for long-distance running', 29.99, NULL, 55, 3),

-- Fitness Products
(13, 'Adjustable Dumbbell Set', 'Complete adjustable dumbbell set from 5-50 lbs', 349.99, NULL, 20, 4),
(14, 'Yoga Mat Premium', 'Extra-thick yoga mat with non-slip surface', 39.99, NULL, 90, 4),
(15, 'Resistance Bands Set', 'Complete resistance bands set with 5 different strengths', 24.99, NULL, 100, 4),
(16, 'Foam Roller Pro', 'High-density foam roller for muscle recovery', 34.99, NULL, 65, 4),

-- Swimming Products
(17, 'Speedo Competition Goggles', 'Professional swimming goggles with anti-fog coating', 29.99, NULL, 75, 5),
(18, 'Arena Swim Cap Silicone', 'Durable silicone swim cap for competitive swimming', 14.99, NULL, 120, 5),

-- Hockey Products
(19, 'CCM Hockey Stick Pro', 'Professional-grade carbon fiber hockey stick', 189.99, NULL, 35, 6),
(20, 'Bauer Hockey Helmet', 'Safety-certified hockey helmet with adjustable fit', 129.99, NULL, 40, 6);

-- Insert Default Users (passwords are BCrypt hashed for "password123")
INSERT INTO users (id, username, email, password, role, created_at) VALUES
(1, 'admin', 'admin@sportsstore.com', '$2a$10$xN3LI/AjqicFYZFruSwve.681477XaVNaUQbr6EEflbWf.BXvjI1u', 'ROLE_ADMIN', CURRENT_TIMESTAMP),
(2, 'user1', 'user1@example.com', '$2a$10$xN3LI/AjqicFYZFruSwve.681477XaVNaUQbr6EEflbWf.BXvjI1u', 'ROLE_USER', CURRENT_TIMESTAMP),
(3, 'johndoe', 'john@example.com', '$2a$10$xN3LI/AjqicFYZFruSwve.681477XaVNaUQbr6EEflbWf.BXvjI1u', 'ROLE_USER', CURRENT_TIMESTAMP);

-- Reset auto-increment sequences to avoid ID conflicts when adding new records
ALTER TABLE users ALTER COLUMN id RESTART WITH 4;
ALTER TABLE categories ALTER COLUMN id RESTART WITH 7;
ALTER TABLE products ALTER COLUMN id RESTART WITH 21;
