package com.unipi.adouladiris.medicalstorage.rest.controllers;

import com.unipi.adouladiris.medicalstorage.configuration.SwaggerConfiguration;
import com.unipi.adouladiris.medicalstorage.database.dao.Insert;
import com.unipi.adouladiris.medicalstorage.database.result.DbResult;
import com.unipi.adouladiris.medicalstorage.rest.dto.RegisterUserRequestBody;
import com.unipi.adouladiris.medicalstorage.rest.dto.UserRequestBody;
import com.unipi.adouladiris.medicalstorage.utilities.JWToken;
import io.swagger.annotations.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api/v1/user/")
@Api(tags = { SwaggerConfiguration.UserController })
public class UserController {

    private static AuthenticationManager authenticationManagerUser;
    @Autowired
    public UserController(AuthenticationManager authenticationManagerUser){UserController.authenticationManagerUser = authenticationManagerUser;}

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder){this.bCryptPasswordEncoder = bCryptPasswordEncoder;}

    //************************** GET/ *************************
    @GetMapping(value = "information")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Get User Information", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized User found!"),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource"),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found!"),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request")
    })
    public ResponseEntity<String> getUserInformation() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity(authentication.getPrincipal(), HttpStatus.OK);
    }
    //**********************************************************

    //************************** POST/ *************************
    // https://stackoverflow.com/questions/3521290/logout-get-or-post
    @PostMapping(value = "logout")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    @ApiOperation(value = "Perform User Logout", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authenticated User logged out!"),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource"),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found!"),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request")
    })
    public ResponseEntity<String> logoutUser() throws Exception {
        //if (SecurityContextHolder.getContext().getAuthentication() == null) return new ResponseEntity("No User Logged In!", HttpStatus.FORBIDDEN);
        JSONObject response = new JSONObject();
        response.put("User", SecurityContextHolder.getContext().getAuthentication().getName());
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity("User " + response.get("User") + " logged out!", HttpStatus.OK);
    }

    @PostMapping(value = "register")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Don't have a account? Register here!")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User has been created."),
            @ApiResponse(code = 400, message = "Conflict occurred."),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource."),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found."),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request.")
    })
    @ApiImplicitParam(name = "registerUserRequestBody", dataTypeClass = RegisterUserRequestBody.class)
    public ResponseEntity<RegisterUserRequestBody>  registerUser(@RequestBody RegisterUserRequestBody registerUserRequestBody) {
        String username = registerUserRequestBody.getUsername();
        String password = registerUserRequestBody.getPassword();
        String authority = registerUserRequestBody.getAuthorities()[0];
        DbResult dbResult = new Insert().user(bCryptPasswordEncoder, username, password, authority);

        if(dbResult.getException() != null){
            String message = dbResult.getException().getCause().getMessage();
            if(message.contains("UK_SB8BBOUER5WAK8VYIIY4PF2BX table: USER"))
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists.", dbResult.getException());
            else throw new ResponseStatusException(HttpStatus.CONFLICT, message, dbResult.getException());
        }
        return new ResponseEntity("User inserted at " + dbResult.getResult().toString(), HttpStatus.OK);
    }

    @PostMapping(value = "requestToken")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Request Auth Token using credentials", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "JsonWebToken generated!"),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource"),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found!"),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request")
    })
    @ApiImplicitParam(name = "body", dataTypeClass = UserRequestBody.class)
    public ResponseEntity<String> requestToken(@RequestBody UserRequestBody body) {

        if(body.getUsername() == null) {
            Exception missingUsername = new Exception("Username is missing.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingUsername.getMessage(), missingUsername);
        }
        else if(body.getPassword() == null ) {
            Exception missingPassword = new Exception("Password is missing.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingPassword.getMessage(), missingPassword);
        }
        String username = body.getUsername();
        String password = body.getPassword();
        String expMinutes = body.getExpMinutes();

        // Use the authenticationManagerUser that we built in SecurityConfiguration.
        try {
            Authentication authentication = authenticationManagerUser.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();

            LinkedHashMap<String, String> response = new LinkedHashMap();
            //Create the token from username and authority.
            String bearerToken = new JWToken(authenticatedUser, expMinutes).toString();
            response.put("username", authenticatedUser.getName() );
            response.put("authority", authenticatedUser.getAuthorities().toString() );
            response.put("bearerToken", bearerToken);
            return new ResponseEntity(response.toString(), HttpStatus.OK);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }
    //**********************************************************

}