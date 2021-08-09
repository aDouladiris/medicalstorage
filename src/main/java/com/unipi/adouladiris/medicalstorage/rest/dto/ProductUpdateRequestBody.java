package com.unipi.adouladiris.medicalstorage.rest.dto;

import io.swagger.annotations.ApiModelProperty;

public class ProductUpdateRequestBody {

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


}



