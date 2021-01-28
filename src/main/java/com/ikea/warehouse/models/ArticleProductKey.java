package com.ikea.warehouse.models;

import java.util.Objects;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class ArticleProductKey implements Serializable {

    private static final long serialVersionUID = -5165990246223667705L;

    @Column(name = "article_id")
    Integer articleId;

    @Column(name = "product_name")
    String productName;
     
    public ArticleProductKey() {    
    }

    public ArticleProductKey(Article article, Product product) {     
        this.articleId = article.getId();
        this.productName = product.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ArticleProductKey articleProductKey = (ArticleProductKey) o;
        return articleId.equals(articleProductKey.articleId) &&
        productName.equals(articleProductKey.productName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(articleId, productName);
    }
}
