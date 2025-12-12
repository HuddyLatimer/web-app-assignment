package com.sportsstore.service;

import com.sportsstore.model.CartItem;
import com.sportsstore.model.Order;
import com.sportsstore.model.OrderItem;
import com.sportsstore.model.User;
import com.sportsstore.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductService productService;
    private final CartService cartService;

    @Autowired
    public OrderService(OrderRepository orderRepository, ProductService productService, CartService cartService) {
        this.orderRepository = orderRepository;
        this.productService = productService;
        this.cartService = cartService;
    }

    public Order createOrder(User user, List<CartItem> cartItems, String shippingAddress, String shippingCity, String shippingZip) {
        // Validate cart
        if (!cartService.validateCart(cartItems)) {
            throw new IllegalStateException("Some items are out of stock");
        }

        // Calculate total
        BigDecimal total = cartService.calculateTotal(cartItems);

        // Create order
        Order order = new Order(user, total, shippingAddress, shippingCity, shippingZip);
        order.setStatus("CONFIRMED");

        // Create order items and reduce stock
        for (CartItem cartItem : cartItems) {
            OrderItem orderItem = new OrderItem(
                    order,
                    cartItem.getProduct(),
                    cartItem.getQuantity(),
                    cartItem.getProduct().getPrice()
            );
            order.addOrderItem(orderItem);

            // Reduce stock
            productService.reduceStock(cartItem.getProduct().getId(), cartItem.getQuantity());
        }

        // Save order
        Order savedOrder = orderRepository.save(order);

        // Clear cart
        cartService.clearUserCart(user);

        return savedOrder;
    }

    public List<Order> findUserOrders(User user) {
        return orderRepository.findByUserOrderByOrderDateDesc(user);
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public List<Order> findByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    public void updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        order.setStatus(status);
        orderRepository.save(order);
    }
}
