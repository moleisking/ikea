package com.ikea.warehouse.repositories;

import com.ikea.warehouse.models.Inventory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends CrudRepository<Inventory, Long> { }