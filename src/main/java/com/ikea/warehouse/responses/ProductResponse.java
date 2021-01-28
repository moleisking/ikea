package com.ikea.warehouse.responses;

import java.util.HashSet;
import java.util.Set;
import com.ikea.warehouse.models.Article;
import com.ikea.warehouse.models.ArticleProductKey;
import com.ikea.warehouse.models.Inventory;
import com.ikea.warehouse.models.Product;

import lombok.Data;

@Data
public class ProductResponse {
    String name;
    Integer stock;
}