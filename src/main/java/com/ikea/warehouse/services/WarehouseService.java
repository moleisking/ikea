package com.ikea.warehouse.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.ikea.warehouse.models.Inventory;
import com.ikea.warehouse.models.Product;
import com.ikea.warehouse.repositories.ProductRepository;
import com.ikea.warehouse.requests.InventoryRequest;
import com.ikea.warehouse.requests.ProductsRequest;
import com.ikea.warehouse.repositories.ArticleRepository;
import com.ikea.warehouse.repositories.InventoryRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestBody;

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

    public List<Product> listProductsAvailability() {
        List<Product> products = productRepository.findAll(); // ImmutableList.copyOf(productRepository.findAll()); //productRepository.findAll();  //
        for (int i = 0; i < products.size() ; i++ ) {
            ArrayList<Integer> quotients = new ArrayList<>();
            for (Inventory inventory : products.get(i).getInventory()) {
                int quotient = inventory.getArticle().getStock() / inventory.getAmount_of();
                quotients.add(quotient);
            }
            products.get(i).setStock(quotients.indexOf(Collections.min(quotients)));           
        }
        return products;
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

    public void addProducts(@Valid @RequestBody ProductsRequest productsRequest) {

        List<Product> products = productsRequest.toProducts();
        products = (List<Product>) productRepository.saveAll(products);
        log.info("Products added {}", productsRequest.getProducts().size());

        List<Inventory> inventories = productsRequest.toInventories();
        inventories = (List<Inventory>) inventoryRepository.saveAll(inventories);
        log.info("Product inventory added {}", inventories.size());
    }

    public void addInventory(@Valid @RequestBody InventoryRequest inventoryRequest) {

        articleRepository.saveAll(inventoryRequest.toArticles());
        log.info("Article inventory added {}", inventoryRequest.getInventory().size());
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void tryLoadInventoryAndProducts() {

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