package com.unipi.adouladiris.medicalstorage.rest.controllers;
import com.sun.security.auth.UserPrincipal;
import com.unipi.adouladiris.medicalstorage.utilities.JWToken;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import com.unipi.adouladiris.medicalstorage.rest.controllers.abstractClass.RoutingController;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONObject;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import static java.lang.String.format;

@RestController
public class UserController extends RoutingController implements UserDetailsService {

    private AuthenticationManager authenticationManager;
    private HttpSecurity http;
    private UserDetailsService userDetailsService;

    private final String jwtSecret = "zdtlD3JK56m6wTTgsNFhqzjqP";
    private final String jwtIssuer = "example.io";

    @Autowired
    public UserController(AuthenticationManager authenticationManager, HttpSecurity http, @Qualifier("myDS") UserDetailsService userDetailsService){
        this.authenticationManager = authenticationManager;
        this.http = http;
        this.userDetailsService = userDetailsService;
    }


    @GetMapping(value = "/userInformation")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    public Object retrieveAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal();
    }


    @PostMapping(value = "/login")
    @PreAuthorize("permitAll()")
    public String login(@RequestBody Map<String, Object> body) throws JsonProcessingException, NoSuchAlgorithmException, SQLException {

        JSONObject parsedBody = new JSONObject();

        for (Map.Entry entry : body.entrySet()){
            parsedBody.put( entry.getKey().toString(), entry.getValue() );
        }

        Authentication authenticate = authenticationManager
                .authenticate( new UsernamePasswordAuthenticationToken(parsedBody.get("username"), parsedBody.get("password")) );

        userDetailsService.loadUserByUsername(parsedBody.get("username").toString());


        //Create the token from username.
        JSONObject jwtPayload = new JSONObject();
        jwtPayload.put("sub", parsedBody.get("username"));

        ArrayList<String> aud = new ArrayList();
        authenticate.getAuthorities().forEach( item -> aud.add(item.getAuthority()) );
        jwtPayload.put("aud", aud);

        LocalDateTime ldt = LocalDateTime.now().plusDays(60);
        jwtPayload.put("exp", ldt.toEpochSecond(ZoneOffset.UTC)); //this needs to be configured

        String bearerToken = new JWToken(jwtPayload).toString();
        System.out.println("bearerToken: " + bearerToken);



        //receive the bearer token
        JWToken incomingToken = new JWToken(bearerToken);

        JSONObject response = new JSONObject();
        response.put("isValid", incomingToken.isValid());
        response.put("message", incomingToken.getSubject());

        return response.toString();

        //return new ResponseEntity(objectToJSON(), HttpStatus.OK);
    }

    @Override
    public UserDetails loadUserByUsername(String username) {

//        User user = userRepository.findByUsername(username);
//        if (user == null) {
//            throw new UsernameNotFoundException(username);
//        }
//        return new UserPrincipal(user);

        return new InMemoryUserDetailsManager(
                org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                        .username("cs")
                        .password("456")
                        .roles("ROLE_customer")
                        .build()).loadUserByUsername("cs");

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