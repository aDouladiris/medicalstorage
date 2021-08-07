package com.unipi.adouladiris.medicalstorage.rest.controllers.users;

import com.unipi.adouladiris.medicalstorage.database.dao.insert.Insert;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import com.unipi.adouladiris.medicalstorage.rest.controllers.abstractClass.RoutingController;
import com.unipi.adouladiris.medicalstorage.rest.dto.RegisterUserRequestBody;
import com.unipi.adouladiris.medicalstorage.rest.dto.UserRequestBody;
import com.unipi.adouladiris.medicalstorage.utilities.JWToken;
import io.swagger.annotations.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;

import static java.lang.String.format;

@RestController
@Api(value = "Swagger2DemoRestController", description = "REST APIs related to Student Entity!!!!")
@RequestMapping("/api/v1/user/")
public class UserController {

    private AuthenticationManager authenticationManagerUser;

    @Autowired
    public UserController(@Qualifier("UserSessionAuthManager") AuthenticationManager authenticationManagerUser){
        this.authenticationManagerUser = authenticationManagerUser;
    }

    @GetMapping(value = "information")
    @ApiOperation(value = "Get User Information", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succeed|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "No User Logged In!"),
            @ApiResponse(code = 404, message = "not found!!!") })
//    @PreAuthorize("hasAnyRole('admin', 'customer')")
//    @PreAuthorize("isFullyAuthenticated()")
//    @PreAuthorize("permitAll()")
    public ResponseEntity<String> getUserInformation() {
        System.out.println("Register!");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity(authentication.getPrincipal(), HttpStatus.OK);
    }

    @PostMapping(value = "requestToken")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Request Auth Token using credentials", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succeed|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "No User Logged In!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @ApiImplicitParam(name = "body", dataTypeClass = UserRequestBody.class)
    public ResponseEntity<String> requestToken(@RequestBody UserRequestBody body) {
        System.out.println("-------Login Start-----------------------");

        if(body.getUsername() == null || body.getPassword() == null ) {
            return new ResponseEntity("Username or password missing.", HttpStatus.BAD_REQUEST);
        }

        String username = body.getUsername();
        String password = body.getPassword();

        // Use the authenticationManagerUser that we built in SecurityConfiguration.
        Authentication authentication;
        try {
            authentication = authenticationManagerUser
                    .authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (AuthenticationException exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.NOT_FOUND);
        }

        Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();

        //Create the token from username.
        String bearerToken = new JWToken(authenticatedUser).toString();

        JSONObject response = new JSONObject();
        response.put("Username", authenticatedUser.getName() );
        response.put("Authorities", authenticatedUser.getAuthorities().toString() );
        response.put("bearerToken", bearerToken);

//        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);

        System.out.println("-------Login End-----------------------");
        return new ResponseEntity(response.toString(), HttpStatus.OK);
    }

    @PostMapping(value = "register")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Don't have a account? Register here!")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succeed|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "forbidden!!!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    @ApiImplicitParam(name = "registerUserRequestBody", dataTypeClass = RegisterUserRequestBody.class)
    public ResponseEntity<RegisterUserRequestBody>  registerUser(@RequestBody RegisterUserRequestBody registerUserRequestBody) {
        System.out.println("Register!");
        String username = registerUserRequestBody.getUsername();
        String password = registerUserRequestBody.getPassword();
        String authority = registerUserRequestBody.getAuthorities()[0];

        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
        System.out.println("Authority: " + authority);

        DbResult dbResult = new Insert().user(username, password, authority);

        if(dbResult.getException() != null) return new ResponseEntity(dbResult.getException().getCause().getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        System.out.println(dbResult.getResult().toString());
        return new ResponseEntity("User inserted at " + dbResult.getResult().toString(), HttpStatus.OK);
    }

    // https://stackoverflow.com/questions/3521290/logout-get-or-post
    @PostMapping(value = "logout")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    @ApiOperation(value = "Perform User Logout", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Succeed|OK"),
            @ApiResponse(code = 401, message = "not authorized!"),
            @ApiResponse(code = 403, message = "No User Logged In!"),
            @ApiResponse(code = 404, message = "not found!!!") })
    public ResponseEntity<String> logoutUser() throws Exception {
        if (SecurityContextHolder.getContext() == null) return new ResponseEntity("No User Logged In!", HttpStatus.FORBIDDEN);
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity(SecurityContextHolder.getContext(), HttpStatus.OK);
    }


}