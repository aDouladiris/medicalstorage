package com.unipi.adouladiris.medicalstorage.swagger.models;
import io.swagger.annotations.ApiModelProperty;

public class ProductUpdateRequestBody {

    // All swagger request bodies will contain only information about uer input format examples.
    // For parsing the request body, we use the Data Transfer Class (DTO) RequestBodyParse.

    @ApiModelProperty(
            name="product",
            dataType = "array",
            required=true,
            example = "[{\"Substance\": {\"Zinadol\": {\"Tab\": [{\"Precaution\": {\"Category\": [{\"Contraindications\": {"+
                        "\"Item\": [{\"Title\": \"AlkoolReplaced\", \"Description\": \"TESTupdated\", \"Tag\": [\"PonokefalosReplaced\"]}]}}]}}]}}}]"
    )
    private Object[] product;
    public Object[] getProduct() {return product;}
    public void setProduct(Object[] product) {this.product = product;}
}



