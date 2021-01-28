package com.ikea.warehouse.requests;

import com.ikea.warehouse.models.Article;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.Data;

@Data
public class InventoryRequest {
    Set<ArticleRequest> inventory;

   public List<Article> toArticles() {
        List<Article> articles = new ArrayList<Article>();
        for (ArticleRequest articleRequest : this.inventory) {
            articles.add(articleRequest.toArticle());
        }       
        return articles;
    }
}
