package com.ikea.warehouse.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ikea.warehouse.models.Article;
import com.ikea.warehouse.models.Inventory;
import com.ikea.warehouse.models.Product;
import com.ikea.warehouse.repositories.ProductRepository;
import com.ikea.warehouse.requests.InventoryRequest;
import com.ikea.warehouse.requests.ProductsRequest;
import com.ikea.warehouse.responses.ProductResponse;
import com.ikea.warehouse.repositories.ArticleRepository;
import com.ikea.warehouse.repositories.InventoryRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.apache.commons.collections4.IteratorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class WarehouseService {

    @Value("${warehouse.import}")
    private Boolean warehouseImport;

    @Autowired
    private ArticleRepository articleRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Logger log = LoggerFactory.getLogger(WarehouseService.class.getName());

    public void addInventory(InventoryRequest inventoryRequest) {

        articleRepository.saveAll(inventoryRequest.toArticles());
        log.info("Article inventory added {}", inventoryRequest.getInventory().size());
    }

    public void addProducts(ProductsRequest productsRequest) {

        List<Product> products = productsRequest.toProducts();
        products = (List<Product>) productRepository.saveAll(products);
        log.info("Products added {}", productsRequest.getProducts().size());

        List<Inventory> inventories = productsRequest.toInventories();
        inventories = (List<Inventory>) inventoryRepository.saveAll(inventories);
        log.info("Product inventory added {}", inventories.size());
    }

    public Integer availableUnitsOfProduct(Product product) {
        ArrayList<Integer> quotients = new ArrayList<>();
        for (Inventory inventory : product.getInventory()) {
            int quotient = inventory.getArticle().getStock() / inventory.getAmount_of();
            quotients.add(quotient);
        }
        return Collections.min(quotients);
    }

    public List<ProductResponse> listProductsAvailability() {
        List<ProductResponse> productResponses = new ArrayList<>();
        try {
            List<Product> products = IteratorUtils.toList(productRepository.findAll().iterator());
            for (Product product : products) {
                productResponses.add(new ProductResponse(product.getName(), availableUnitsOfProduct(product)));
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        return productResponses;
    }

    private void loadInventory() {

        try {
            File file = ResourceUtils.getFile("classpath:static/inventory.json");
            InputStream in = new FileInputStream(file);
            ObjectMapper mapper = new ObjectMapper();
            InventoryRequest inventoryRequest = mapper.readValue(in, InventoryRequest.class);
            addInventory(inventoryRequest);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private void loadProducts() {

        try {
            File file = ResourceUtils.getFile("classpath:static/products.json");
            InputStream in = new FileInputStream(file);
            ObjectMapper mapper = new ObjectMapper();
            ProductsRequest productsRequest = mapper.readValue(in, ProductsRequest.class);
            addProducts(productsRequest);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public Boolean removeProduct(Product product) {
        product = productRepository.findByName(product.getName());
        if (product != null && availableUnitsOfProduct(product) > 0) {

            for (Inventory inventory : product.getInventory()) {
                Optional<Article> articleOptional = articleRepository.findById(inventory.getArticle().getId());
                if (articleOptional.isPresent()) {
                    Article article = articleOptional.get();
                    article.setStock(article.getStock() - inventory.getAmount_of());
                    articleRepository.save(article);
                    log.info("Inventory remove {}", inventory);
                }
            }
            return true;
        } else {
            log.error("Inventory stock insufficient {}", product.getName());
            return false;
        }

    }

    @Scheduled(initialDelay = 2000, fixedRate = 24 * 60 * 60 * 1000)
    public void tryLoadInventoryAndProducts() {
        log.error("tryLoadInventoryAndProducts started...");
        if (warehouseImport.equals(true)) {
            try {
                loadInventory();
                loadProducts();
            } catch (Exception ex) {
                log.error("WarehouseController and WarehouseService race condition {}", ex.getMessage());
            }
        }
    }

}