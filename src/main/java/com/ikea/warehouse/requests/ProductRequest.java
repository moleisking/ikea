package com.ikea.warehouse.requests;

import java.util.HashSet;
import java.util.Set;
import com.ikea.warehouse.models.Article;
import com.ikea.warehouse.models.ArticleProductKey;
import com.ikea.warehouse.models.Inventory;
import com.ikea.warehouse.models.Product;
import lombok.Data;

@Data
public class ProductRequest {
    String name;
    Set<ArticleRequest> contain_articles;     

    public Product toProduct() {      
       
        Product product = new Product();
        product.setName(this.name);    
        return product;
    }

    public Set<Inventory> toInventory() {
      
        Set<Inventory> inventories = new HashSet<Inventory>();
        Product product = new Product();
        product.setName(this.name);

        for (ArticleRequest articleRequest : contain_articles) {
            Article article = articleRequest.toArticle();
            Inventory inventory = new Inventory();
            inventory.setId(new ArticleProductKey(article,product));
            inventory.setAmount_of(articleRequest.getAmount_of());
            inventory.setArticle(article );   
            inventory.setProduct(product);
            inventories.add(inventory);
        }       
     
        return inventories;
    }
}