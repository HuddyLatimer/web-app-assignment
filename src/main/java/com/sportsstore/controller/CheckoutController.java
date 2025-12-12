package com.sportsstore.controller;

import com.sportsstore.model.CartItem;
import com.sportsstore.model.Order;
import com.sportsstore.model.User;
import com.sportsstore.service.CartService;
import com.sportsstore.service.OrderService;
import com.sportsstore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
public class CheckoutController {

    private final CartService cartService;
    private final OrderService orderService;
    private final UserService userService;

    @Autowired
    public CheckoutController(CartService cartService, OrderService orderService, UserService userService) {
        this.cartService = cartService;
        this.orderService = orderService;
        this.userService = userService;
    }

    @GetMapping("/checkout")
    public String showCheckoutForm(Authentication authentication, Model model, RedirectAttributes redirectAttributes) {
        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Please log in to checkout");
            return "redirect:/login";
        }

        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<CartItem> cartItems = cartService.getUserCart(user);

        if (cartItems.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Your cart is empty");
            return "redirect:/cart";
        }

        BigDecimal total = cartService.calculateTotal(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        model.addAttribute("user", user);
        return "checkout";
    }

    @PostMapping("/checkout")
    public String processCheckout(@RequestParam String shippingAddress,
                                   @RequestParam String shippingCity,
                                   @RequestParam String shippingZip,
                                   Authentication authentication,
                                   RedirectAttributes redirectAttributes) {
        try {
            String username = authentication.getName();
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<CartItem> cartItems = cartService.getUserCart(user);

            if (cartItems.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Your cart is empty");
                return "redirect:/cart";
            }

            Order order = orderService.createOrder(user, cartItems, shippingAddress, shippingCity, shippingZip);

            redirectAttributes.addFlashAttribute("success", "Order placed successfully!");
            return "redirect:/order-confirmation/" + order.getId();
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Checkout failed: " + e.getMessage());
            return "redirect:/checkout";
        }
    }

    @GetMapping("/order-confirmation/{id}")
    public String orderConfirmation(@PathVariable Long id, Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Order order = orderService.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Security check: ensure order belongs to current user
        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized access to order");
        }

        model.addAttribute("order", order);
        return "order-confirmation";
    }
}
