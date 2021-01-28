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
public class Article {
  
    @Id
    Integer id;  
  
    String name;

    Integer stock;

   @OneToMany(cascade = CascadeType.DETACH,  fetch = FetchType.EAGER,mappedBy = "article")
   Set<Inventory> inventory  = new HashSet<>();

}