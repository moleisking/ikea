package com.ikea.warehouse.models;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import lombok.Data;

@Data
@Entity
public class Inventory {

    @EmbeddedId
    ArticleProductKey id;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH, optional = false)
    @MapsId("articleId")
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH, optional = false)
    @MapsId("productName")
    @JoinColumn(name = "product_name")
    private Product product;

    @Column(name = "amount_of")
    Integer amount_of;

    public Inventory() {
    }

    public Inventory(Article article, Product product, Integer amount_of) {
        this.id = new ArticleProductKey(article, product);
        this.article = article;
        this.product = product;
        this.amount_of = amount_of;
    }    
}