package com.unipi.adouladiris.medicalstorage.rest.dto;

import io.swagger.annotations.ApiModelProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ProductUpdateRequestBody {

    @ApiModelProperty(
            name="product",
            dataType = "array",
            required=true,
            example = "[{\"Substance\": {\"Zinadol\": {\"Tab\": [{\"Precaution\": {\"Category\": [{\"Contraindications\": {"+
                        "\"Item\": [{\"Title\": \"AlkoolReplaced\", \"Description\": \"TESTupdated\", \"Tag\": [\"PonokefalosReplaced\"]}]}}]}}]}}}]"



//            example =  "[{\"Substance\": {\"SubstanceDummyName\": " +
//                    "{\"Tab\": [{\"TabDummyName1\": " +
//                    "{\"Category\": [{\"CategoryDummyName1\":" +
//                    "{\"Item\": [" +
//                    "{\"Title\": \"TittleDummyName1\", \"Description\": \"DescriptionDummy1\", " +
//                    "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
//                    "{\"Title\": \"TittleDummyName2\", \"Description\": \"DescriptionDummy2\", " +
//                    "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
//                    "{\"Title\": \"TittleDummyName3\", \"Description\": \"DescriptionDummy3\", " +
//                    "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
//                    "]}}," +
//                    "{\"CategoryDummyName2\":" +
//                    "{\"Item\": [" +
//                    "{\"Title\": \"TittleDummyName1\", \"Description\": \"DescriptionDummy1\", " +
//                    "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
//                    "{\"Title\": \"TittleDummyName2\", \"Description\": \"DescriptionDummy2\", " +
//                    "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
//                    "{\"Title\": \"TittleDummyName3\", \"Description\": \"DescriptionDummy3\", " +
//                    "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
//                    "]}}]}}," +
//                    "{\"TabDummyName2\": " +
//                    "{\"Category\": [{\"CategoryDummyName3\":" +
//                    "{\"Item\": [" +
//                    "{\"Title\": \"TittleDummyName1\", \"Description\": \"DescriptionDummy1\", " +
//                    "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
//                    "{\"Title\": \"TittleDummyName2\", \"Description\": \"DescriptionDummy2\", " +
//                    "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
//                    "{\"Title\": \"TittleDummyName3\", \"Description\": \"DescriptionDummy3\", " +
//                    "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
//                    "]}}," +
//                    "{\"CategoryDummyName4\":" +
//                    "{\"Item\": [" +
//                    "{\"Title\": \"TittleDummyName1\", \"Description\": \"DescriptionDummy1\", " +
//                    "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
//                    "{\"Title\": \"TittleDummyName2\", \"Description\": \"DescriptionDummy2\", " +
//                    "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
//                    "{\"Title\": \"TittleDummyName3\", \"Description\": \"DescriptionDummy3\", " +
//                    "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
//                    "]}}]}}]}}}," +
//                    "{\"Substance\": {\"SubstanceDummyName2\": " +
//                    "{\"Tab\": [{\"TabDummyName1\": " +
//                    "{\"Category\": [{\"CategoryDummyName12\":" +
//                    "{\"Item\": [" +
//                    "{\"Title\": \"TittleDummyName12\", \"Description\": \"DescriptionDummy1\", " +
//                    "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
//                    "{\"Title\": \"TittleDummyName22\", \"Description\": \"DescriptionDummy2\", " +
//                    "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
//                    "{\"Title\": \"TittleDummyName32\", \"Description\": \"DescriptionDummy3\", " +
//                    "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
//                    "]}}," +
//                    "{\"CategoryDummyName22\":" +
//                    "{\"Item\": [" +
//                    "{\"Title\": \"TittleDummyName12\", \"Description\": \"DescriptionDummy1\", " +
//                    "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
//                    "{\"Title\": \"TittleDummyName22\", \"Description\": \"DescriptionDummy2\", " +
//                    "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
//                    "{\"Title\": \"TittleDummyName23\", \"Description\": \"DescriptionDummy3\", " +
//                    "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
//                    "]}}]}}," +
//                    "{\"TabDummyName2\": " +
//                    "{\"Category\": [" +
//                    "{\"CategoryDummyName3\":" +
//                    "{\"Item\": [" +
//                    "{\"Title\": \"TittleDummyName1\", \"Description\": \"DescriptionDummy1\", " +
//                    "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
//                    "{\"Title\": \"TittleDummyName2\", \"Description\": \"DescriptionDummy2\", " +
//                    "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
//                    "{\"Title\": \"TittleDummyName3\", \"Description\": \"DescriptionDummy3\", " +
//                    "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
//                    "]}}," +
//                    "{\"CategoryDummyName4\":" +
//                    "{\"Item\": [" +
//                    "{\"Title\": \"TittleDummyName1\", \"Description\": \"DescriptionDummy1\", " +
//                    "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
//                    "{\"Title\": \"TittleDummyName2\", \"Description\": \"DescriptionDummy2\", " +
//                    "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
//                    "{\"Title\": \"TittleDummyName3\", \"Description\": \"DescriptionDummy3\", " +
//                    "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
//                    "]}}]}}]}}}" +
//                    "]"
    )
    private Object[] product;
    public Object[] getProduct() {return product;}
    public void setProduct(Object[] product) {this.product = product;}
}



