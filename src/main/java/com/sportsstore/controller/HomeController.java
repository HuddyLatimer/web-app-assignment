package com.sportsstore.controller;

import com.sportsstore.model.Product;
import com.sportsstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;

    @Autowired
    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping({"/", "/home"})
    public String home(Model model) {
        List<Product> featuredProducts = productService.findInStock();
        // Limit to 8 featured products
        if (featuredProducts.size() > 8) {
            featuredProducts = featuredProducts.subList(0, 8);
        }
        model.addAttribute("featuredProducts", featuredProducts);
        return "index";
    }
}
