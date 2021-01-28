package com.ikea.warehouse.repositories;

import com.ikea.warehouse.models.Product;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Integer> {   

    @Query(nativeQuery = true, value = "SELECT p.name, p.price, i.amount_of, a.stock " +
    " FROM Product p " +
    " LEFT OUTER JOIN Inventory i ON p.name = i.product_name " +
    " LEFT OUTER JOIN Article a ON i.article_id = a.id")
    List<Product> findAllWithStock();   

    @Query(nativeQuery = true, value = "SELECT p.* FROM Product p")
    List<Product> findAll();   
}