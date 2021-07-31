package com.unipi.adouladiris.medicalstorage.rest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unipi.adouladiris.medicalstorage.business.Product;
import com.unipi.adouladiris.medicalstorage.database.dao.insert.Insert;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import com.unipi.adouladiris.medicalstorage.rest.controllers.abstractClass.RoutingController;
import com.unipi.adouladiris.medicalstorage.rest.dto.DataTransferObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@RestController
public class UserController extends RoutingController {

    @GetMapping(value = "/userInformation")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    public Object retrieveAuthentication() throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return objectToJSON(authentication.getPrincipal());
    }

    @GetMapping(value = "/logout")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    public ResponseEntity<String> logoutUser(@Autowired HttpSecurity http) throws Exception {

        http
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");


        return new ResponseEntity("TEST", HttpStatus.OK);
    }


}