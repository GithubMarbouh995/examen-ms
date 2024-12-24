package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductQLService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ProductQLController {
    private final ProductQLService productService;

    public ProductQLController(ProductQLService productService) {
        this.productService = productService;
    }

    @QueryMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @QueryMapping
    public Product getProductById(@Argument Long id) {
        return productService.getProductById(id);
    }

    @MutationMapping
    public Product createProduct(@Argument String name, 
                               @Argument Double price, 
                               @Argument Integer quantity, 
                               @Argument String description) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setDescription(description);
        return productService.createProduct(product);
    }

    @MutationMapping
    public Product updateProduct(@Argument Long id, 
                               @Argument String name, 
                               @Argument Double price, 
                               @Argument Integer quantity, 
                               @Argument String description) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        product.setQuantity(quantity);
        product.setDescription(description);
        return productService.updateProduct(id, product);
    }

    @MutationMapping
    public boolean deleteProduct(@Argument Long id) {
        return productService.deleteProduct(id);
    }
     @QueryMapping
    public List<Product> searchProducts(@Argument String keyword, 
                                      @Argument Double minPrice, 
                                      @Argument Double maxPrice) {
        return productService.searchProducts(keyword, minPrice, maxPrice);
    }

    @QueryMapping
    public ProductStats getProductStats() {
        return productService.getProductStats();
    }

    @QueryMapping
    public ProductPage getProductsPaginated(@Argument int page, @Argument int size) {
        Page<Product> productPage = productService.getProductsPaginated(page, size);
        return new ProductPage(productPage);
    }

    @MutationMapping
    public List<Product> createProductsBatch(@Argument List<ProductInput> products) {
        List<Product> productEntities = products.stream()
            .map(this::convertToEntity)
            .collect(Collectors.toList());
        return productService.createProductsBatch(productEntities);
    }

    @MutationMapping
    public List<Product> updateStockBatch(@Argument List<StockUpdateInput> updates) {
        return productService.updateStockBatch(updates);
    }

    private Product convertToEntity(ProductInput input) {
        Product product = new Product();
        product.setName(input.getName());
        product.setPrice(input.getPrice());
        product.setQuantity(input.getQuantity());
        product.setDescription(input.getDescription());
        return product;
    }
}
