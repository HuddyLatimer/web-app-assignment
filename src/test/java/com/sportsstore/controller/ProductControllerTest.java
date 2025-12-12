package com.sportsstore.controller;

import com.sportsstore.model.Category;
import com.sportsstore.model.Product;
import com.sportsstore.service.CategoryService;
import com.sportsstore.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private CategoryService categoryService;

    private Product testProduct;
    private Category testCategory;
    private List<Product> productList;
    private List<Category> categoryList;

    @BeforeEach
    void setUp() {
        // Set up test category
        testCategory = new Category();
        testCategory.setId(1L);
        testCategory.setName("Basketball");
        testCategory.setDescription("Basketball equipment");

        // Set up test product
        testProduct = new Product();
        testProduct.setId(1L);
        testProduct.setName("Test Basketball");
        testProduct.setDescription("Official test basketball");
        testProduct.setPrice(new BigDecimal("89.99"));
        testProduct.setImageUrl("https://example.com/ball.jpg");
        testProduct.setStockQuantity(50);
        testProduct.setCategory(testCategory);

        productList = Arrays.asList(testProduct);
        categoryList = Arrays.asList(testCategory);
    }

    @Test
    @WithMockUser
    void testListProducts_WithoutFilters() throws Exception {
        // Arrange
        when(productService.findAll()).thenReturn(productList);
        when(categoryService.findAll()).thenReturn(categoryList);

        // Act & Assert
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attributeExists("categories"))
                .andExpect(model().attribute("products", hasSize(1)))
                .andExpect(model().attribute("products", hasItem(
                        hasProperty("name", is("Test Basketball"))
                )));
    }

    @Test
    @WithMockUser
    void testListProducts_WithCategoryFilter() throws Exception {
        // Arrange
        when(productService.findByCategoryId(1L)).thenReturn(productList);
        when(categoryService.findAll()).thenReturn(categoryList);

        // Act & Assert
        mockMvc.perform(get("/products")
                        .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("selectedCategoryId", 1L))
                .andExpect(model().attribute("products", hasSize(1)));
    }

    @Test
    @WithMockUser
    void testListProducts_WithSearch() throws Exception {
        // Arrange
        when(productService.searchProducts("basketball")).thenReturn(productList);
        when(categoryService.findAll()).thenReturn(categoryList);

        // Act & Assert
        mockMvc.perform(get("/products")
                        .param("search", "basketball"))
                .andExpect(status().isOk())
                .andExpect(view().name("products"))
                .andExpect(model().attributeExists("products"))
                .andExpect(model().attribute("search", "basketball"))
                .andExpect(model().attribute("products", hasSize(1)));
    }

    @Test
    @WithMockUser
    void testProductDetail_Found() throws Exception {
        // Arrange
        when(productService.findById(1L)).thenReturn(Optional.of(testProduct));

        // Act & Assert
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("product-detail"))
                .andExpect(model().attributeExists("product"))
                .andExpect(model().attribute("product", hasProperty("name", is("Test Basketball"))))
                .andExpect(model().attribute("product", hasProperty("price", is(new BigDecimal("89.99")))));
    }

    @Test
    @WithMockUser
    void testProductDetail_NotFound() throws Exception {
        // Arrange
        when(productService.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/products/999"))
                .andExpect(status().is5xxServerError());
    }
}
