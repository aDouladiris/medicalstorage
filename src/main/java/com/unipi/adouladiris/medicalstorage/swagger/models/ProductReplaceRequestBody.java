package com.unipi.adouladiris.medicalstorage.swagger.models;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ProductReplaceRequestBody {

    // All swagger request bodies will contain only information about uer input format examples.
    // For parsing the request body, we use the Data Transfer Class (DTO) RequestBodyParse.

    @ApiModelProperty(
            name="replacement",
            dataType = "array",
            required=true,
            example = "{\"Tab\":[{\"Γενικά\": \"Γενικά replaced\"}]}"
    )
    private LinkedHashMap<String, ArrayList<LinkedHashMap>>replacement;
    public LinkedHashMap<String, ArrayList<LinkedHashMap>> getReplacement() {return replacement;}
    public void setReplacement(LinkedHashMap<String, ArrayList<LinkedHashMap>> replacement) {this.replacement = replacement;}

    @ApiModelProperty(
            name="product",
            dataType = "String",
            required=true,
            example = "Zinadol"
    )
    private Object[] product;

    public Object[] getProduct() {
        return product;
    }

    public void setProduct(Object[] product) {
        this.product = product;
    }
}
