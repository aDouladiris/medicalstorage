package com.unipi.adouladiris.medicalstorage.configuration.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import com.unipi.adouladiris.medicalstorage.utilities.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import static java.util.Set.of;

//readhttps://www.baeldung.com/intercepting-filter-pattern-in-java

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private AuthenticationManager authenticationManagerToken;

    @Autowired
    public void authenticationManagerToken(@Qualifier("UserTokenAuthManager") AuthenticationManager authenticationManagerToken) {
        this.authenticationManagerToken = authenticationManagerToken;
    }

    private UserDetailsService userDetailsService;

    @Autowired
    private void userDetailsService(@Qualifier("UserTokenDetailsService") UserDetailsService userDetailsService){
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        //System.out.println("Should filter: " + request.getRequestURI());

        if(request.getRequestURI().contains("swagger")) return true;
        if(request.getRequestURI().contains("user")) return true;
        Set<String> pathToIgnore = new HashSet();
        pathToIgnore.add("/");
        pathToIgnore.add("/v2/api-docs");
        pathToIgnore.add("/csrf");
        return pathToIgnore.contains(request.getRequestURI());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Get authorization header and validate
        System.out.println("JwtFilter Start");

        if( httpServletRequest.getHeader("Bearer" ) == null ){
            System.out.println("JwtFilter End Bearer not in headers");
            //System.out.println(SecurityContextHolder.getContext().getAuthentication());
            SecurityContextHolder.getContext().getAuthentication().setAuthenticated(false);
            //httpServletResponse.setStatus(403);
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Get jwt token and validate
        final String token = httpServletRequest.getHeader("Bearer") ; //header.split(" ")[1].trim();

        if (token == null) {
            System.out.println("JwtFilter End Bearer not in headers 2nd check");
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        JWToken jwToken = null;
        try {
            jwToken = new JWToken(token);
            if (!jwToken.isValid()) {
                System.out.println("JwtFilter End Invalid token");
                //filterChain.doFilter(httpServletRequest, httpServletResponse);
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> errorDetails = new HashMap<>();
                errorDetails.put("message", "Invalid token");
                httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(httpServletResponse.getWriter(), errorDetails);
                return;
                //filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
            else {
                //filterChain.doFilter(httpServletRequest, httpServletResponse);
            }
        } catch (NoSuchAlgorithmException e) {
            //e.printStackTrace();
            System.out.println("JwtFilter End Invalid token");
            //filterChain.doFilter(httpServletRequest, httpServletResponse);
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", "Invalid token");
            errorDetails.put("exception", e.getMessage());
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(httpServletResponse.getWriter(), errorDetails);
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Get user identity and set it on the spring security context
        //UserDetails userDetails = new Select().findUser(jwToken.getSubject()).getResult(User.class);
        UserDetails userDetails;

        System.out.println("User!");
        try {
            //userDetails  = userDetailsService.loadUserByUsername(jwToken.getSubject());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(null, null,
                            jwToken.getAudience());
            System.out.println("Null User: " + usernamePasswordAuthenticationToken.toString());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            System.out.println("JwtFilter End OK");
            filterChain.doFilter(httpServletRequest, httpServletResponse);

        }catch (UsernameNotFoundException exception){
            System.out.println("JwtFilter End User not found");
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> errorDetails = new HashMap<>();
            errorDetails.put("message", "Invalid token");
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(httpServletResponse.getWriter(), errorDetails);
        }



    }
}