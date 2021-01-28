package com.ikea.warehouse.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponse {

    String name;
    Integer availableUnits;

    public ProductResponse (String name, Integer availableUnits) {     
        this.name = name;
        this.availableUnits = availableUnits;
    }    
}