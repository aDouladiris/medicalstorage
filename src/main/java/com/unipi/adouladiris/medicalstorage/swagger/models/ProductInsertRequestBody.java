package com.unipi.adouladiris.medicalstorage.swagger.models;

import io.swagger.annotations.ApiModelProperty;

public class ProductInsertRequestBody {

    // All swagger request bodies will contain only information about uer input format examples.
    // For parsing the request body, we use the Data Transfer Class (DTO) RequestBodyParse.

    @ApiModelProperty(
            name="product",
            dataType = "array",
            required=true,
            example =  "[{\"Substance\": {\"SubstanceDummyName\": " +
                            "{\"Tab\": [{\"TabDummyName1\": " +
                                    "{\"Category\": [{\"CategoryDummyName1\":" +
                                    "{\"Item\": [" +
                                        "{\"Title\": \"TittleDummyName1\", \"Description\": \"DescriptionDummy1\", " +
                                        "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
                                        "{\"Title\": \"TittleDummyName2\", \"Description\": \"DescriptionDummy2\", " +
                                        "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
                                        "{\"Title\": \"TittleDummyName3\", \"Description\": \"DescriptionDummy3\", " +
                                        "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
                                        "]}}," +
                                "{\"CategoryDummyName2\":" +
                                        "{\"Item\": [" +
                                        "{\"Title\": \"TittleDummyName1\", \"Description\": \"DescriptionDummy1\", " +
                                        "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
                                        "{\"Title\": \"TittleDummyName2\", \"Description\": \"DescriptionDummy2\", " +
                                        "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
                                        "{\"Title\": \"TittleDummyName3\", \"Description\": \"DescriptionDummy3\", " +
                                        "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
                                        "]}}]}}," +
                                "{\"TabDummyName2\": " +
                                "{\"Category\": [{\"CategoryDummyName3\":" +
                                "{\"Item\": [" +
                                        "{\"Title\": \"TittleDummyName1\", \"Description\": \"DescriptionDummy1\", " +
                                        "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
                                        "{\"Title\": \"TittleDummyName2\", \"Description\": \"DescriptionDummy2\", " +
                                        "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
                                        "{\"Title\": \"TittleDummyName3\", \"Description\": \"DescriptionDummy3\", " +
                                        "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
                                    "]}}," +
                            "{\"CategoryDummyName4\":" +
                                        "{\"Item\": [" +
                                        "{\"Title\": \"TittleDummyName1\", \"Description\": \"DescriptionDummy1\", " +
                                        "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
                                        "{\"Title\": \"TittleDummyName2\", \"Description\": \"DescriptionDummy2\", " +
                                        "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
                                        "{\"Title\": \"TittleDummyName3\", \"Description\": \"DescriptionDummy3\", " +
                                        "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
                                    "]}}]}}]}}}," +
                            "{\"Substance\": {\"SubstanceDummyName2\": " +
                                "{\"Tab\": [{\"TabDummyName1\": " +
                                    "{\"Category\": [{\"CategoryDummyName12\":" +
                                    "{\"Item\": [" +
                                        "{\"Title\": \"TittleDummyName12\", \"Description\": \"DescriptionDummy1\", " +
                                        "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
                                        "{\"Title\": \"TittleDummyName22\", \"Description\": \"DescriptionDummy2\", " +
                                        "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
                                        "{\"Title\": \"TittleDummyName32\", \"Description\": \"DescriptionDummy3\", " +
                                        "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
                                    "]}}," +
                                    "{\"CategoryDummyName22\":" +
                                    "{\"Item\": [" +
                                        "{\"Title\": \"TittleDummyName12\", \"Description\": \"DescriptionDummy1\", " +
                                        "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
                                        "{\"Title\": \"TittleDummyName22\", \"Description\": \"DescriptionDummy2\", " +
                                        "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
                                        "{\"Title\": \"TittleDummyName23\", \"Description\": \"DescriptionDummy3\", " +
                                        "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
                                    "]}}]}}," +
                                    "{\"TabDummyName2\": " +
                                    "{\"Category\": [" +
                                    "{\"CategoryDummyName3\":" +
                                    "{\"Item\": [" +
                                        "{\"Title\": \"TittleDummyName1\", \"Description\": \"DescriptionDummy1\", " +
                                        "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
                                        "{\"Title\": \"TittleDummyName2\", \"Description\": \"DescriptionDummy2\", " +
                                        "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
                                        "{\"Title\": \"TittleDummyName3\", \"Description\": \"DescriptionDummy3\", " +
                                        "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
                                    "]}}," +
                                    "{\"CategoryDummyName4\":" +
                                    "{\"Item\": [" +
                                        "{\"Title\": \"TittleDummyName1\", \"Description\": \"DescriptionDummy1\", " +
                                        "\"Tag\": [\"TagDummyName1\", \"TagDummyName2\", \"TagDummyName3\"]}, " +
                                        "{\"Title\": \"TittleDummyName2\", \"Description\": \"DescriptionDummy2\", " +
                                        "\"Tag\": [\"TagDummyName4\", \"TagDummyName5\", \"TagDummyName6\"]}, " +
                                        "{\"Title\": \"TittleDummyName3\", \"Description\": \"DescriptionDummy3\", " +
                                        "\"Tag\": [\"TagDummyName7\", \"TagDummyName8\", \"TagDummyName9\"]} " +
                                    "]}}]}}]}}}" +
                    "]"
    )
    private Object[] product;

    public Object[] getProduct() {
        return product;
    }

    public void setProduct(Object[] product) {
        this.product = product;
    }
}
