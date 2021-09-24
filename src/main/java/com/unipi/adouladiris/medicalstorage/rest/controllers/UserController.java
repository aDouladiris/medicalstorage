package com.unipi.adouladiris.medicalstorage.rest.controllers;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.database.dao.Delete;
import com.unipi.adouladiris.medicalstorage.database.dao.Update;
import com.unipi.adouladiris.medicalstorage.swagger.SwaggerConfiguration;
import com.unipi.adouladiris.medicalstorage.database.dao.Insert;
import com.unipi.adouladiris.medicalstorage.database.result.DbResult;
import com.unipi.adouladiris.medicalstorage.swagger.models.*;
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

    //********************************************************************
    // Http request will be intercepted by Token filter before proceeding.
    //********************************************************************

    private static AuthenticationManager authenticationManagerUser;
    @Autowired
    public UserController(AuthenticationManager authenticationManagerUser){UserController.authenticationManagerUser = authenticationManagerUser;}

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder){this.bCryptPasswordEncoder = bCryptPasswordEncoder;}

    //************************** GET/ *************************
    @GetMapping(value = "information")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Get information about user from Security Context", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authorized User found!"),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource"),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found!"),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request")
    })
    public ResponseEntity<String> getUserInformation() {
        // Get user information from security context.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return new ResponseEntity(authentication.getPrincipal(), HttpStatus.OK);
    }
    //**********************************************************

    //************************** DELETE/ *************************
    @DeleteMapping(value = "delete")
    @PreAuthorize("hasAnyRole('admin')")
    @ApiOperation(value = "Delete user account from database (Admin only)", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User deleted!"),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource"),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found!"),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request")
    })
    @ApiImplicitParam(name = "body", dataTypeClass = DeleteUserRequestBody.class)
    public ResponseEntity<String> deleteUser(@RequestBody DeleteUserRequestBody body) {
        if(body.getUsername() == null || body.getUsername().length() == 0){
            Exception missingUsername = new Exception("Username is missing.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingUsername.getMessage(), missingUsername);
        }

        if(body.getPassword() == null || body.getPassword().length() == 0){
            Exception missingPassword = new Exception("Password is missing.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingPassword.getMessage(), missingPassword);
        }

        try {
            DbResult dbResult = new Delete().deleteUserByName(bCryptPasswordEncoder,body.getUsername(), body.getPassword());
            if(dbResult.getException() != null){
                return new ResponseEntity(dbResult.getException().getMessage(), HttpStatus.CONFLICT);
            }
            else{
                String currentUser = SecurityContextHolder.getContext().getAuthentication().getName();
                if(currentUser.equals(body.getUsername())){
                    // Empty security context.
                    SecurityContextHolder.getContext().setAuthentication(null);
                    return new ResponseEntity("User " + body.getUsername() + " has been deleted and logged out.", HttpStatus.OK);
                }else{
                    return new ResponseEntity("User " + body.getUsername() + " has been deleted.", HttpStatus.OK);
                }
            }
        }catch (Exception exception){
            if(exception.getMessage().equals("Wrong password")){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
            }
        }
    }
    //************************************************************

    //**************************** PUT/ **************************
    @PutMapping(value = "modify")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    @ApiOperation(value = "Change user password", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User password changed!"),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource"),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found!"),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request")
    })
    @ApiImplicitParam(name = "body", dataTypeClass = ModifyUserRequestBody.class)
    public ResponseEntity<String> modifyUser(@RequestBody ModifyUserRequestBody body) {
        if(body.getUsername() == null || body.getUsername().length() == 0){
            Exception missingUsername = new Exception("Username is missing.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingUsername.getMessage(), missingUsername);
        }

        if(body.getOldPassword() == null || body.getOldPassword().length() == 0){
            Exception missingPassword = new Exception("Old password is missing.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingPassword.getMessage(), missingPassword);
        }

        if(body.getNewPassword() == null || body.getNewPassword().length() == 0){
            Exception missingNewUsername = new Exception("New password is missing.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingNewUsername.getMessage(), missingNewUsername);
        }

        try {
            DbResult dbResult = new Update().modifyUserPassword(bCryptPasswordEncoder,body.getUsername(),body.getOldPassword(),body.getNewPassword());
            if(dbResult.getException() != null){
                return new ResponseEntity(dbResult.getException().getMessage(), HttpStatus.CONFLICT);
            }
            else{
                return new ResponseEntity("User's " + body.getUsername() + " password has been changed.", HttpStatus.OK);
            }
        }catch (Exception exception){
            if(exception.getMessage().equals("Old password and new password is the same")){
                throw new ResponseStatusException(HttpStatus.CONFLICT, exception.getMessage(), exception);
            }
            else if(exception.getMessage().equals("Wrong password")){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
            }
        }
    }

    @PutMapping(value = "status")
    @PreAuthorize("hasAnyRole('admin')")
    @ApiOperation(value = "Enable/Disable user and change authority (Admin only)", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User status changed!"),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource"),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found!"),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request")
    })
    @ApiImplicitParam(name = "body", dataTypeClass = EnableUserRequestBody.class)
    public ResponseEntity<String> statusRoleEnable(@RequestBody EnableUserRequestBody body) {
        if(body.getUsername() == null || body.getUsername().length() == 0){
            Exception missingUsername = new Exception("Username is missing.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingUsername.getMessage(), missingUsername);
        }

        if(body.getPassword() == null || body.getPassword().length() == 0){
            Exception missingPassword = new Exception("Old password is missing.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingPassword.getMessage(), missingPassword);
        }

        if(body.getRole() == null || body.getRole().length() == 0){
            Exception missingNewUsername = new Exception("User status is missing.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingNewUsername.getMessage(), missingNewUsername);
        }

        if(body.getEnabled() == ' '){
            Exception missingNewUsername = new Exception("User status enabled is missing.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, missingNewUsername.getMessage(), missingNewUsername);
        }

        try {
            DbResult dbResult = new Update().modifyUserRoleAndStatus(bCryptPasswordEncoder,body.getUsername(),body.getPassword(),
                    body.getRole(),body.getEnabled());
            if(dbResult.getException() != null){
                return new ResponseEntity(dbResult.getException().getMessage(), HttpStatus.CONFLICT);
            }
            else{
                return new ResponseEntity("User's " + body.getUsername() + " status has been changed.", HttpStatus.OK);
            }
        }catch (Exception exception){
            if(exception.getMessage().equals("Wrong password")){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
            }
            else{
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
            }
        }
    }
    //************************************************************

    //************************** POST/ *************************
    @PostMapping(value = "login")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "User login using Basic authentication", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Authenticated User logged in!"),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource"),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found!"),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request")
    })
    @ApiImplicitParam(name = "body", dataTypeClass = LoginUserRequestBody.class)
    public ResponseEntity<String> login(@RequestBody UserRequestBody body) {
        // Each database successful response will be wrapped in a ResponseEntity object.
        // In case of exception, the response will be wrapped in a ResponseStatusException object.
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

        // Return if same user is already logged in.
        if(SecurityContextHolder.getContext().getAuthentication().getName().equals(username)){
            Exception loggedUsername = new Exception("Username is already logged in.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, loggedUsername.getMessage(), loggedUsername);
        }

        // Use the authenticationManagerUser that we built in SecurityConfiguration.
        try {
            Authentication authentication = authenticationManagerUser.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();
            return new ResponseEntity("User " + authenticatedUser.getName() + " has logged in!", HttpStatus.OK);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }
    // We set logout as POST request in order to avoid link prefetching from web browsers.
    // If browser prefetch a GET link that logs a user out, it will execute a log out.
    // As soon as prefetching this particular link changes user state, it is better to avoid it using POST.
    @PostMapping(value = "logout")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    @ApiOperation(value = "User logout", response = String.class)
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
        // Empty user security context.
        SecurityContextHolder.getContext().setAuthentication(null);
        return new ResponseEntity("User " + response.get("User") + " logged out!", HttpStatus.OK);
    }

    @PostMapping(value = "register")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Register new user in database")
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
            // Each database successful response will be wrapped in a ResponseEntity object.
            // In case of exception, the response will be wrapped in a ResponseStatusException object.
            // If we get Unique Key constraint exception, the user already exists.
            if(message.contains("UK_") && message.contains("table: USER")) // Identify specific error for Unique Key constraints at User table.
                throw new ResponseStatusException(HttpStatus.CONFLICT, "User already exists.", dbResult.getException());
            else throw new ResponseStatusException(HttpStatus.CONFLICT, message, dbResult.getException());
        }
        return new ResponseEntity("User inserted at " + dbResult.getResult().toString(), HttpStatus.OK);
    }

    @PostMapping(value = "token")
    @PreAuthorize("permitAll()")
    @ApiOperation(value = "Get Json Web Authentication token", response = String.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "JsonWebToken generated!"),
            @ApiResponse(code = 401, message = "The user does not have valid authentication credentials for the target resource"),
            @ApiResponse(code = 403, message = "User does not have permission (Authorized but not enough privileges)"),
            @ApiResponse(code = 404, message = "The requested resource could not be found!"),
            @ApiResponse(code = 500, message = "Server Internal Error at executing request")
    })
    @ApiImplicitParam(name = "body", dataTypeClass = UserRequestBody.class)
    public ResponseEntity<String> token(@RequestBody UserRequestBody body) {
        // Each database successful response will be wrapped in a ResponseEntity object.
        // In case of exception, the response will be wrapped in a ResponseStatusException object.
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
            // Skip if same user is already logged in.
            if(!SecurityContextHolder.getContext().getAuthentication().getName().equals(username)){
                Authentication authentication = authenticationManagerUser.authenticate(new UsernamePasswordAuthenticationToken(username, password));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            // Get user from Security Context.
            Authentication authenticatedUser = SecurityContextHolder.getContext().getAuthentication();

            LinkedHashMap<String, String> response = new LinkedHashMap();
            //Create the token from username and authority.
            String bearerToken = new JWToken(authenticatedUser, expMinutes).toString();
            response.put("username", authenticatedUser.getName() );
            response.put("authority", authenticatedUser.getAuthorities().toString() );
            response.put("bearerToken", bearerToken);
            // Empty user security context.
            SecurityContextHolder.getContext().setAuthentication(null);
            return new ResponseEntity(response.toString(), HttpStatus.OK);
        }catch (Exception exception){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, exception.getMessage(), exception);
        }
    }
    //**********************************************************

}