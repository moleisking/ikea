package com.ikea.warehouse.repositories;

import com.ikea.warehouse.models.Article;
import com.ikea.warehouse.models.Inventory;
import com.ikea.warehouse.models.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Long> {
    
    @Query(nativeQuery = true, value = "SELECT TOP 1 * FROM Journey J WHERE J.state = 'inactive'")
    Optional<Product> findFirstByStateIsInactive();    

    @Query(nativeQuery = true, value = "SELECT TOP 1 * FROM Journey J WHERE J.id = ?1 AND J.state != 'done'")
    Optional<Article> findByIdAndIsNotDone(Integer id);   
}