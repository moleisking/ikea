package com.ikea.warehouse.requests;

import com.ikea.warehouse.models.Inventory;
import com.ikea.warehouse.models.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class ProductsRequest {
    Set<ProductRequest> products;

   public List<Product> toProducts() {
        List<Product> productList = new ArrayList<Product>();
        for (ProductRequest productRequest : products) {
            productList.add(productRequest.toProduct());
        }       
        return productList;
    }

    public List<Inventory> toInventories() {
        List<Inventory> inventoryList = new ArrayList<Inventory>();
        for (ProductRequest productRequest : products) {
            inventoryList.addAll(productRequest.toInventory());
        }       
        return inventoryList;
    }
}
