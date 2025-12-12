package com.sportsstore.service;

import com.sportsstore.model.CartItem;
import com.sportsstore.model.Product;
import com.sportsstore.model.User;
import com.sportsstore.repository.CartItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final ProductService productService;

    @Autowired
    public CartService(CartItemRepository cartItemRepository, ProductService productService) {
        this.cartItemRepository = cartItemRepository;
        this.productService = productService;
    }

    // Guest cart methods (session-based)
    public List<CartItem> getGuestCart(String sessionId) {
        return cartItemRepository.findBySessionId(sessionId);
    }

    public void addToGuestCart(String sessionId, Long productId, int quantity) {
        Product product = productService.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Optional<CartItem> existingItem = cartItemRepository.findBySessionIdAndProduct(sessionId, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(product, quantity, sessionId);
            cartItemRepository.save(newItem);
        }
    }

    // User cart methods (authenticated)
    public List<CartItem> getUserCart(User user) {
        return cartItemRepository.findByUser(user);
    }

    public void addToUserCart(User user, Long productId, int quantity) {
        Product product = productService.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Optional<CartItem> existingItem = cartItemRepository.findByUserAndProduct(user, product);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + quantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem(product, quantity, user);
            cartItemRepository.save(newItem);
        }
    }

    // Update cart item quantity
    public void updateQuantity(Long cartItemId, int quantity) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        if (quantity <= 0) {
            cartItemRepository.delete(item);
        } else {
            item.setQuantity(quantity);
            cartItemRepository.save(item);
        }
    }

    // Remove item from cart
    public void removeItem(Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
    }

    // Clear cart
    public void clearGuestCart(String sessionId) {
        cartItemRepository.deleteBySessionId(sessionId);
    }

    public void clearUserCart(User user) {
        cartItemRepository.deleteByUser(user);
    }

    // Merge guest cart to user cart on login
    public void mergeGuestCartToUser(String sessionId, User user) {
        List<CartItem> guestItems = cartItemRepository.findBySessionId(sessionId);

        for (CartItem guestItem : guestItems) {
            Optional<CartItem> userItem = cartItemRepository.findByUserAndProduct(user, guestItem.getProduct());

            if (userItem.isPresent()) {
                // Merge quantities
                CartItem existing = userItem.get();
                existing.setQuantity(existing.getQuantity() + guestItem.getQuantity());
                cartItemRepository.save(existing);
            } else {
                // Move guest item to user
                guestItem.setUser(user);
                guestItem.setSessionId(null);
                cartItemRepository.save(guestItem);
            }
        }

        // Clean up remaining guest items
        cartItemRepository.deleteBySessionId(sessionId);
    }

    // Calculate totals
    public BigDecimal calculateTotal(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(item -> item.getProduct().getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Validate cart before checkout
    public boolean validateCart(List<CartItem> cartItems) {
        for (CartItem item : cartItems) {
            if (!productService.isInStock(item.getProduct().getId(), item.getQuantity())) {
                return false;
            }
        }
        return true;
    }
}
