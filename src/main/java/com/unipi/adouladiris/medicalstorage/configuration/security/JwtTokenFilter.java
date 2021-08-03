package com.unipi.adouladiris.medicalstorage.configuration.security;

import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import com.unipi.adouladiris.medicalstorage.utilities.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Enumeration;

import static java.util.Set.of;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private AuthenticationManager authenticationManagerToken;

    @Autowired
    public void authenticationManagerToken(@Qualifier("UserTokenAuthManager") AuthenticationManager authenticationManagerToken) {
        this.authenticationManagerToken = authenticationManagerToken;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Get authorization header and validate
        System.out.println("JwtFilter Start");
//        System.out.println(httpServletRequest.getHeader("Bearer"));
//        final String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

//        Enumeration headerNames = httpServletRequest.getHeaderNames();
//
//        while (headerNames.hasMoreElements()){
//            System.out.println(headerNames.nextElement().toString());
//        }

        if( httpServletRequest.getHeader("Bearer" ) == null ){
            System.out.println("JwtFilter End");
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Get jwt token and validate
        final String token = httpServletRequest.getHeader("Bearer") ; //header.split(" ")[1].trim();
        JWToken jwToken = null;
        try {
            jwToken = new JWToken(token);
            if (!jwToken.isValid()) {
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        System.out.println(jwToken.getSubject());
        System.out.println(jwToken.getAudience());

        // Get user identity and set it on the spring security context
        UserDetails user = new Select().findUser(jwToken.getSubject()).getResult(User.class);

        String pass = "$2y$12$2IipZAG3JJF7fF4Pwf0LKO/I5MK46MG8rU.4zi8Ai.dbCTgkJZSJq";

//        System.out.println(user.getUsername());
//        System.out.println(user.getPassword());
//        System.out.println(user.getAuthorities().toString());
        UsernamePasswordAuthenticationToken userToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
        System.out.println(userToken);
        Authentication authentication = authenticationManagerToken.authenticate(userToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        System.out.println("Build ready: " + SecurityContextHolder.getContext().getAuthentication());

        System.out.println(SecurityContextHolder.getContext().getAuthentication());

        filterChain.doFilter(httpServletRequest, httpServletResponse);
        System.out.println("JwtFilter End");

    }
}
