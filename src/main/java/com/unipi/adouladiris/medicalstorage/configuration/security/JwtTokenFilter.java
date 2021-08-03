package com.unipi.adouladiris.medicalstorage.configuration.security;

import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import com.unipi.adouladiris.medicalstorage.utilities.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
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
import java.util.Enumeration;

import static java.util.Set.of;

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
            System.out.println("JwtFilter End NOT");
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        // Get jwt token and validate
        final String token = httpServletRequest.getHeader("Bearer") ; //header.split(" ")[1].trim();
        JWToken jwToken = null;
        try {
            jwToken = new JWToken(token);
            if (!jwToken.isValid()) {
                System.out.println("JwtFilter End NOT");
                filterChain.doFilter(httpServletRequest, httpServletResponse);
                return;
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        // Get user identity and set it on the spring security context
//        UserDetails userDetails = new Select().findUser(jwToken.getSubject()).getResult(User.class);
        UserDetails userDetails;

        try {
            userDetails  = userDetailsService.loadUserByUsername(jwToken.getSubject());
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            usernamePasswordAuthenticationToken
                    .setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            System.out.println("JwtFilter End OK");

        }catch (UsernameNotFoundException exception){
            System.out.println("JwtFilter End NOT");
        }

        filterChain.doFilter(httpServletRequest, httpServletResponse);

    }
}
