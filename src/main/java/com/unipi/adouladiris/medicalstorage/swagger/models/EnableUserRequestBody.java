package com.unipi.adouladiris.medicalstorage.swagger.models;

import io.swagger.annotations.ApiModelProperty;

public class EnableUserRequestBody {

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
            notes = "Status of the User",
            name="Status",
            dataType = "Char",
            required=true,
            example = "Y"
    )
    private char enabled;

    @ApiModelProperty(
            notes = "Role of the User",
            name="Role",
            dataType = "String",
            required=true,
            example = "admin"
    )
    private String role;

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

    public char getEnabled() { return enabled; }
    public void setEnabled(char enabled) { this.enabled = enabled; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
