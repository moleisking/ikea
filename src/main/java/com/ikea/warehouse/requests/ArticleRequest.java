package com.ikea.warehouse.requests;

import com.ikea.warehouse.models.Article;

import lombok.Data;

@Data
public class ArticleRequest {
    
    Integer art_id;    
  
    String name;

    Integer stock;

    Integer amount_of;

    Article  toArticle(){
      Article  article = new Article();
      article.setId(this.art_id);
      article.setName(this.name);
      article.setStock(this.stock);
      return article;
    }
}
