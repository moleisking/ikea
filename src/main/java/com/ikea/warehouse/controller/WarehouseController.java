package com.ikea.warehouse.controller;

import com.ikea.warehouse.models.Article;
import com.ikea.warehouse.models.Inventory;
import com.ikea.warehouse.models.Product;
import com.ikea.warehouse.repositories.ProductRepository;
import com.ikea.warehouse.requests.InventoryRequest;
import com.ikea.warehouse.requests.ProductsRequest;
import com.ikea.warehouse.repositories.ArticleRepository;
import com.ikea.warehouse.repositories.InventoryRepository;
import com.ikea.warehouse.services.WarehouseService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class WarehouseController {

    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    InventoryRepository inventoryRepository;

    @Autowired
    ProductRepository productRepository;

    @Autowired
    WarehouseService warehouseService;

    private Logger log = LoggerFactory.getLogger(WarehouseController.class.getName());

    @DeleteMapping(value = "/product/remove", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<Void> removeProduct(@Valid @RequestBody Product product) {

        for (Inventory inventory : product.getInventory()) {
            Optional<Article> articleOptional = articleRepository.findById(inventory.getArticle().getId());
            if (articleOptional.isPresent()) {
                Article article = articleOptional.get();
                if (article.getStock() > inventory.getAmount_of()) {
                    article.setStock(article.getStock() - inventory.getAmount_of());
                    articleRepository.save(article);
                    log.info("Inventory remove {}", inventory);
                } else {
                    log.info("Inventory stock insufficient {}", inventory);
                }
            } else {
                log.info("Inventory remove not found {}", inventory.getId());
            }
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/products/all")
    public ResponseEntity<List<Product>> listProducts() {
        List<Product> products = warehouseService.listProductsAvailability();
        return ResponseEntity.ok(products);
    }

    @PutMapping(value = "/products/add", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<Void> addProducts(@Valid @RequestBody ProductsRequest productsRequest) {

        warehouseService.addProducts(productsRequest);     
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/status")
    public ResponseEntity<String> status() {
        return ResponseEntity.ok("Service Up");
    }

    @PutMapping(value = "/inventory/add", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<Void> addInventory(@Valid @RequestBody InventoryRequest inventoryRequest) {

        warehouseService.addInventory(inventoryRequest);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
        return new ResponseEntity<>("not valid due to constraint error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<String> handleTransactionViolationException(TransactionSystemException e) {
        return new ResponseEntity<>("not valid due to transaction error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(TransactionSystemException e) {
        return new ResponseEntity<>("not valid due to JSON error: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
        return new ResponseEntity<>("not valid due to null error: " + e.getMessage(), HttpStatus.NOT_FOUND);
    }

}