package com.unipi.adouladiris.medicalstorage.configuration.security;

import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import com.unipi.adouladiris.medicalstorage.utilities.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

import static java.util.Set.of;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Get authorization header and validate

        System.out.println(httpServletRequest.getHeader("Bearer"));
//        final String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
//        System.out.println("header: " + header);



//        if ( header == null || header.isEmpty() || !header.startsWith("Bearer ")) {
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//            return;
//        }

        if( httpServletRequest.getHeader("Bearer") == null ){
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

        // Get user identity and set it on the spring security context
        User user = new Select().findUser(jwToken.getSubject()).getResult(User.class);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());

        authentication
                .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));

        System.out.println(authentication.getPrincipal());
        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
