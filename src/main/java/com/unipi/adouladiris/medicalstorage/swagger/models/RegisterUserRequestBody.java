package com.unipi.adouladiris.medicalstorage.swagger.models;

import io.swagger.annotations.ApiModelProperty;



public class RegisterUserRequestBody {

    // All swagger request bodies will contain only information about uer input format examples.
    // For parsing the request body, we use the Data Transfer Class (DTO) RequestBodyParse.

    @ApiModelProperty(
            notes = "Name of the User",
            name="Username",
            dataType = "String",
            required=true,
            example = "arg"
    )
    private String username;

    @ApiModelProperty(
            notes = "Password of the User",
            name="Password",
            dataType = "String",
            required=true,
            example = "123"
    )
    private String password;

    @ApiModelProperty(
            notes = "Authority of the User",
            name="Authority",
            dataType = "array",
            required=true,
            example =  "[\"admin\"]"
    )
    private String[] authorities;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] getAuthorities() {
        return authorities;
    }

    public void setAuthorities(String[] authorities) {
        this.authorities = authorities;
    }
}
