package com.unipi.adouladiris.medicalstorage.rest.dto;

import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.operable.*;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.mapping.Array;

import java.util.*;

public class ProductInsertRequestBody {

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
    public Object[] product;


    //    @ApiModelProperty(
//            notes = "Password of the User",
//            name="Password",
//            dataType = "String",
//            required=true,
//            example = "123"
//    )
//    private String password;
//
//    public String getUsername() {
//        return username;
//    }
//
//    public void setUsername(String username) {
//        this.username = username;
//    }
//
//    public String getPassword() {
//        return password;
//    }
//
//    public void setPassword(String password) {
//        this.password = password;
//    }


}
