package com.ikea.warehouse.controller;

import com.ikea.warehouse.models.Product;
import com.ikea.warehouse.requests.InventoryRequest;
import com.ikea.warehouse.requests.ProductsRequest;
import com.ikea.warehouse.responses.ProductResponse;
import com.ikea.warehouse.services.WarehouseService;
import java.util.List;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated
public class WarehouseController {  

    @Autowired
    WarehouseService warehouseService;

    private Logger log = LoggerFactory.getLogger(WarehouseController.class.getName());

    @DeleteMapping(value = "/product/remove", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<Void> removeProduct(@Valid @RequestBody Product productRequest) {

       if( warehouseService.removeProduct(productRequest)){
        return ResponseEntity.ok().build();
       } else{
        return ResponseEntity.notFound().build();
       }      
    }

    @GetMapping(value = "/products/all",produces = {
        MediaType.APPLICATION_JSON_VALUE })
    public ResponseEntity<List<ProductResponse>> listProducts() {
        List<ProductResponse> products = warehouseService.listProductsAvailability();
        return ResponseEntity.ok(products);
    }

    @PutMapping(value = "/products/add", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<Void> addProducts(@Valid @RequestBody ProductsRequest productsRequest) {

        warehouseService.addProducts(productsRequest);     
        return ResponseEntity.ok().build();
    }

    @PutMapping(value = "/inventory/add", consumes = { MediaType.APPLICATION_JSON_VALUE }, produces = {
            MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<Void> addInventory(@Valid @RequestBody InventoryRequest inventoryRequest) {

        warehouseService.addInventory(inventoryRequest);
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<String> handleConstraintViolationException(ConstraintViolationException e) {
      
        log.error(e.getMessage());
        return new ResponseEntity<>("Not valid due to constraint error.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity<String> handleTransactionViolationException(TransactionSystemException e) {
       
        log.error(e.getMessage());
        return new ResponseEntity<>("Not valid due to transaction error.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleValidationException(TransactionSystemException e) {
      
        log.error(e.getMessage());
        return new ResponseEntity<>("Not valid due to JSON error.", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<String> handleNullPointerException(NullPointerException e) {
       
        log.error(e.getMessage());
        return new ResponseEntity<>("Not valid due to null error." , HttpStatus.NOT_FOUND);
    }

}