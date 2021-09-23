package com.unipi.adouladiris.medicalstorage.swagger.models;

import io.swagger.annotations.ApiModelProperty;

public class ModifyUserRequestBody {

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
            notes = "Old password of the User",
            name="Password",
            dataType = "String",
            required=true,
            example = "123"
    )
    private String oldPassword;

    @ApiModelProperty(
            notes = "New password of the User",
            name="Password",
            dataType = "String",
            required=true,
            example = "456"
    )
    private String newPassword;

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getOldPassword() { return oldPassword; }
    public void setOldPassword(String oldPassword) { this.oldPassword = oldPassword; }

    public String getNewPassword() { return newPassword; }
    public void setNewPassword(String newPassword) { this.newPassword = newPassword; }
}
