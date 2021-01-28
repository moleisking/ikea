package com.ikea.warehouse.models;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {
  
    @Id    
    String name;
    
    Double price;

    @OneToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER, mappedBy = "product")
    Set<Inventory> inventory = new HashSet<>();
   
}