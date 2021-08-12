package com.unipi.adouladiris.medicalstorage.rest.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ProductReplaceRequestBody {

    @ApiModelProperty(
            name="replacement",
            dataType = "array",
            required=true,
            example = "{\"Tab\":[{\"Precaution\": \"PrecautionReplaced\"}]}"
    )
    private LinkedHashMap<String, ArrayList<LinkedHashMap>>replacement;
    public LinkedHashMap<String, ArrayList<LinkedHashMap>> getReplacement() {return replacement;}
    public void setReplacement(LinkedHashMap<String, ArrayList<LinkedHashMap>> replacement) {this.replacement = replacement;}
}
