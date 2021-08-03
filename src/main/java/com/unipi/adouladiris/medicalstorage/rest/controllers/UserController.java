package com.unipi.adouladiris.medicalstorage.rest.controllers;

import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import com.unipi.adouladiris.medicalstorage.rest.controllers.abstractClass.RoutingController;
import com.unipi.adouladiris.medicalstorage.utilities.JWToken;
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

import javax.servlet.http.HttpServletRequest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;

import static java.lang.String.format;

@RestController
//@RequestMapping("/user/")
public class UserController extends RoutingController {

    private AuthenticationManager authenticationManagerUser;

    @Autowired
    public UserController(@Qualifier("UserSessionAuthManager") AuthenticationManager authenticationManagerUser){
        this.authenticationManagerUser = authenticationManagerUser;
    }

    @GetMapping(value = "information")
    @PreAuthorize("hasAnyRole('admin', 'customer')")
    public Object retrieveAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal();
    }

    @PostMapping(value = "login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<String> login(@RequestBody Map<String, Object> body) {
        System.out.println("-------Login Start-----------------------");

        if(!body.containsKey("username") || !body.containsKey("password") ) {
            return new ResponseEntity("Username or password missing.", HttpStatus.BAD_REQUEST);
        }

        String username = (String) body.get("username");
        String password = (String) body.get("password");

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
//        JSONObject jwtPayload = new JSONObject();
//        jwtPayload.put("sub", authenticatedUser.getName());
//        ArrayList<String> aud = new ArrayList();
//        authentication.getAuthorities().forEach( item -> aud.add(item.getAuthority()) );
//        jwtPayload.put("aud", aud);
//        LocalDateTime ldt = LocalDateTime.now().plusDays(60);
//        jwtPayload.put("exp", ldt.toEpochSecond(ZoneOffset.UTC)); //this needs to be configured
//        String bearerToken = new JWToken(jwtPayload).toString();

        String bearerToken = new JWToken(authenticatedUser).toString();

        JSONObject response = new JSONObject();
        response.put("Username", authenticatedUser.getName() );
        response.put("Authorities", authenticatedUser.getAuthorities().toString() );
        response.put("bearerToken", bearerToken);

        System.out.println("-------Login End-----------------------");
        return new ResponseEntity(response.toString(), HttpStatus.OK);
    }

//    @Override
//    public UserDetails loadUserByUsername(String username) {
//        System.out.println("Session: loadUserByUsername");
//
//        User customUser = new Select().findUser(username).getResult(User.class);
//
//        org.springframework.security.core.userdetails.User.UserBuilder builder = null;
//        if (customUser != null) {
//            builder = org.springframework.security.core.userdetails.User.withUsername(username);
//            builder.password(new BCryptPasswordEncoder().encode(customUser.getPassword()));
//
//            String[] roles = new String[1];
//            roles[0] = customUser.getRole().getAuthority();
//
//            builder.roles( roles );
//        } else {
//            throw new UsernameNotFoundException("User not found.");
//        }
//
//        System.out.println("User: " + builder.toString());
//
//        return builder.build();
//
//    }





    //    @WebServlet(name = "LogoutServlet", urlPatterns = {"/logout"})
//    public class LogoutServlet extends HttpServlet {
//        @Override
//        protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//            HttpSession session = request.getSession(false);
//            // Destroys the session for this user.
//            if (session != null)
//                session.invalidate();
//            // Redirects back to the initial page.
//            response.sendRedirect(request.getContextPath());
//        }
//    }




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



//    @GetMapping(value = "/logout")
//    @PreAuthorize("hasAnyRole('admin', 'customer')")
//    public ResponseEntity<Object> logoutUser() throws Exception {
//        http
//                .logout()
//                .invalidateHttpSession(true)
//                .deleteCookies("JSESSIONID");
//
//        SecurityContextHolder.getContext().setAuthentication(null);
//
//        return new ResponseEntity("User logout", HttpStatus.OK);
//    }


}