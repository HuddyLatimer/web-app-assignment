package com.sportsstore.controller;

import com.sportsstore.model.CartItem;
import com.sportsstore.model.User;
import com.sportsstore.service.CartService;
import com.sportsstore.service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserService userService;

    @Autowired
    public CartController(CartService cartService, UserService userService) {
        this.cartService = cartService;
        this.userService = userService;
    }

    @GetMapping
    public String viewCart(Authentication authentication, HttpSession session, Model model) {
        List<CartItem> cartItems;

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username)
                    .orElseThrow(() -> new RuntimeException("User not found"));
            cartItems = cartService.getUserCart(user);
        } else {
            String sessionId = session.getId();
            cartItems = cartService.getGuestCart(sessionId);
        }

        BigDecimal total = cartService.calculateTotal(cartItems);

        model.addAttribute("cartItems", cartItems);
        model.addAttribute("total", total);
        return "cart";
    }

    @PostMapping("/add")
    public String addToCart(@RequestParam Long productId,
                            @RequestParam(defaultValue = "1") int quantity,
                            Authentication authentication,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        try {
            if (authentication != null && authentication.isAuthenticated()) {
                String username = authentication.getName();
                User user = userService.findByUsername(username)
                        .orElseThrow(() -> new RuntimeException("User not found"));
                cartService.addToUserCart(user, productId, quantity);
            } else {
                String sessionId = session.getId();
                cartService.addToGuestCart(sessionId, productId, quantity);
            }

            redirectAttributes.addFlashAttribute("success", "Item added to cart");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to add item to cart: " + e.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/update/{id}")
    public String updateQuantity(@PathVariable Long id,
                                 @RequestParam int quantity,
                                 RedirectAttributes redirectAttributes) {
        try {
            cartService.updateQuantity(id, quantity);
            redirectAttributes.addFlashAttribute("success", "Cart updated");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update cart: " + e.getMessage());
        }

        return "redirect:/cart";
    }

    @PostMapping("/remove/{id}")
    public String removeItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            cartService.removeItem(id);
            redirectAttributes.addFlashAttribute("success", "Item removed from cart");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to remove item: " + e.getMessage());
        }

        return "redirect:/cart";
    }
}
