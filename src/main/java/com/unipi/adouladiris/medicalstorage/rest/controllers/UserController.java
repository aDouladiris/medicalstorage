package com.unipi.adouladiris.medicalstorage.rest.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unipi.adouladiris.medicalstorage.business.Product;
import com.unipi.adouladiris.medicalstorage.database.dao.insert.Insert;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import com.unipi.adouladiris.medicalstorage.rest.controllers.abstractClass.RoutingController;
import com.unipi.adouladiris.medicalstorage.rest.dto.DataTransferObject;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
import java.util.*;

import static java.lang.String.format;

@RestController
public class UserController extends RoutingController {

    private AuthenticationManager authenticationManager;
    private HttpSecurity http;

    private final String jwtSecret = "zdtlD3JK56m6wTTgsNFhqzjqP";
    private final String jwtIssuer = "example.io";

    @Autowired
    public UserController(AuthenticationManager authenticationManager, HttpSecurity http){
        this.authenticationManager = authenticationManager;
        this.http = http;
    }


    @GetMapping(value = "/userInformation")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    public Object retrieveAuthentication() throws JsonProcessingException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return objectToJSON(authentication.getPrincipal());
    }


    @PostMapping(value = "/login")
    @PreAuthorize("permitAll()")
    public String login(@RequestBody JSONObject body) throws JsonProcessingException {

        Authentication authenticate = authenticationManager
                .authenticate( new UsernamePasswordAuthenticationToken(body.get("username"), body.get("password")) );

        User user = new Select().findUser(body.get("username").toString()).getResult(User.class);

        return Jwts.builder()
                .setSubject(format("%s,%s", user.getId(), user.getUsername()))
                .setIssuer(jwtIssuer)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 7 * 24 * 60 * 60 * 1000)) // 1 week
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();


        //return new ResponseEntity(objectToJSON(), HttpStatus.OK);
    }




//
//    @PostMapping(value = "/loginp")
//    @PreAuthorize("permitAll()")
//    public Object login(@RequestBody body) {
//
//            Authentication authenticate = authenticationManager
//                    .authenticate( new UsernamePasswordAuthenticationToken(username, password) );
//
//
//
//            return user;
//
////            user
////
////            Jwt jwt = new Jwt();
////
////            return ResponseEntity.ok()
////                    .header(
////                            HttpHeaders.AUTHORIZATION,
////                            jwtTokenUtil.generateAccessToken(user)
////                    )
////                    .body(userViewMapper.toUserView(user));
////        } catch (BadCredentialsException ex) {
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//
//    }



    @GetMapping(value = "/logout")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    public ResponseEntity<Object> logoutUser() throws Exception {
        http
                .logout()
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID");

        SecurityContextHolder.getContext().setAuthentication(null);

        return new ResponseEntity("User logout", HttpStatus.OK);
    }


}