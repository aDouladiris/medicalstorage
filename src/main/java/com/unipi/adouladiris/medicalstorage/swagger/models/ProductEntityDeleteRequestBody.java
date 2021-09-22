package com.unipi.adouladiris.medicalstorage.swagger.models;

import io.swagger.annotations.ApiModelProperty;

public class ProductEntityDeleteRequestBody {

    // All swagger request bodies will contain only information about uer input format examples.
    // For parsing the request body, we use the Data Transfer Class (DTO) RequestBodyParse.

    @ApiModelProperty(
            name="updateProduct",
            dataType = "array",
            required=true,
            example = "[{\"Substance\": {\"ZINADOL\": {\"Tab\": [{\"Γενικά\": {\"Category\": [{\"Περιγραφή\": {"+
                    "\"Item\": [{\"Title\": \"Φαρμακοτεχνική μορφή\", \"Description\": \"Δισκία επικαλυμμένα με υμένιο (TAB_FILM_COATED)\", \"Tag\": [\"\"]}]}}]}}]}}}]"
    )
    private Object[] product;
    public Object[] getProduct() {return product;}
    public void setProduct(Object[] product) {this.product = product;}
}
