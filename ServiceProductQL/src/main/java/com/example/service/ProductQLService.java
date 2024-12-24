package com.example.service;

import com.example.model.Product;
import com.example.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductQLService {
    private final ProductRepository productRepository;

    public ProductQLService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    @Transactional
    public Product updateProduct(Long id, Product productDetails) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        
        if (productDetails.getName() != null) 
            product.setName(productDetails.getName());
        if (productDetails.getPrice() != null) 
            product.setPrice(productDetails.getPrice());
        if (productDetails.getQuantity() != null) 
            product.setQuantity(productDetails.getQuantity());
        if (productDetails.getDescription() != null) 
            product.setDescription(productDetails.getDescription());
        
        return productRepository.save(product);
    }

    @Transactional
    public boolean deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }
     public List<Product> searchProducts(String keyword, Double minPrice, Double maxPrice) {
        return productRepository.findAll().stream()
            .filter(p -> p.getName().toLowerCase().contains(keyword.toLowerCase())
                && (minPrice == null || p.getPrice() >= minPrice)
                && (maxPrice == null || p.getPrice() <= maxPrice))
            .collect(Collectors.toList());
    }

    public ProductStats getProductStats() {
        List<Product> products = productRepository.findAll();
        return ProductStats.builder()
            .totalProducts(products.size())
            .averagePrice(products.stream()
                .mapToDouble(Product::getPrice)
                .average()
                .orElse(0.0))
            .outOfStockCount(products.stream()
                .filter(p -> p.getQuantity() <= 0)
                .count())
            .build();
    }

    @Transactional
    public List<Product> createProductsBatch(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @Transactional
    public List<Product> updateStockBatch(List<StockUpdate> updates) {
        List<Product> updatedProducts = new ArrayList<>();
        for (StockUpdate update : updates) {
            Product product = productRepository.findById(update.getId())
                .orElseThrow(() -> new RuntimeException("Product not found"));
            product.setQuantity(update.getQuantity());
            updatedProducts.add(product);
        }
        return productRepository.saveAll(updatedProducts);
    }

    public Page<Product> getProductsPaginated(int page, int size) {
        return productRepository.findAll(PageRequest.of(page, size));
    }
}
