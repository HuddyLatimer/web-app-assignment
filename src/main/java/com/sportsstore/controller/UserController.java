package com.sportsstore.controller;

import com.sportsstore.model.Order;
import com.sportsstore.model.User;
import com.sportsstore.service.CartService;
import com.sportsstore.service.OrderService;
import com.sportsstore.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final OrderService orderService;
    private final CartService cartService;

    @Autowired
    public UserController(UserService userService, OrderService orderService, CartService cartService) {
        this.userService = userService;
        this.orderService = orderService;
        this.cartService = cartService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@Valid @ModelAttribute("user") User user,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        // Check for validation errors
        if (result.hasErrors()) {
            return "register";
        }

        // Check if username exists
        if (userService.existsByUsername(user.getUsername())) {
            model.addAttribute("error", "Username already exists");
            return "register";
        }

        // Check if email exists
        if (userService.existsByEmail(user.getEmail())) {
            model.addAttribute("error", "Email already exists");
            return "register";
        }

        try {
            userService.registerUser(user.getUsername(), user.getEmail(), user.getPassword());
            redirectAttributes.addFlashAttribute("success", "Registration successful! Please log in.");
            return "redirect:/login";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register";
        }
    }

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/profile")
    public String showProfile(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Order> orders = orderService.findUserOrders(user);

        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        return "profile";
    }

    @PostMapping("/login-success")
    public String handleLoginSuccess(Authentication authentication, HttpSession session) {
        String username = authentication.getName();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Merge guest cart with user cart
        String sessionId = session.getId();
        cartService.mergeGuestCartToUser(sessionId, user);

        return "redirect:/";
    }
}
