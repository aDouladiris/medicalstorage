package com.unipi.adouladiris.medicalstorage.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;

import java.lang.reflect.Array;



public class RegisterUserRequestBody {

    @ApiModelProperty(
            notes = "Name of the User",
            name="Username",
            dataType = "String",
            required=true,
            example = "DummyName"
    )
    private String username;

    @ApiModelProperty(
            notes = "Password of the User",
            name="Password",
            dataType = "String",
            required=true,
            example = "DummyPassword"
    )
    private String password;

    @ApiModelProperty(
            notes = "Authority of the User",
            name="Authority",
            dataType = "array",
            required=true,
            example =  "[\"admin\", \"customer\"]"
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
