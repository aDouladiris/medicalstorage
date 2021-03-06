package com.unipi.adouladiris.medicalstorage.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unipi.adouladiris.medicalstorage.utilities.JWToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.*;


// Filter class that exists at the beginning of the security chain checking each HTTP request.
@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    private DateTimeFormatter dateTimeFormatter;
    @Autowired
    public void setDateTimeFormatter(DateTimeFormatter dateTimeFormatter){this.dateTimeFormatter = dateTimeFormatter;}

    // Returns if request target endpoint should be filtered or not.
    // Only /api/v1/updateProduct/ endpoint should be filtered.
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        if(request.getRequestURI().contains("/api/v1/product/")) return false; // ShouldFilter
        else return true; // ShouldNotFilter
    }

    // If request target endpoint is filtered, then it will check request headers if containing Bearer field.
    // If exists, then it will reconstruct the JwT token as an object JWToken and validate it by examining its
    // expiring date.
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        JWToken jwToken;
        ObjectMapper mapper = new ObjectMapper();
        // Get jwt token and validate
        final String token = httpServletRequest.getHeader("Bearer") ;
        try {
            jwToken = new JWToken(token);
            if (!jwToken.isValid()) {
                httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
                httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
                mapper.writeValue(httpServletResponse.getWriter(), createResponseBodyWithError(HttpStatus.FORBIDDEN, null, "Token expired"));
                return; // return without proceeding further to the next filter at the chain.
            }
        } catch (NoSuchAlgorithmException ex) {
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(httpServletResponse.getWriter(), createResponseBodyWithError(HttpStatus.FORBIDDEN, ex, "Invalid token"));
            return; // return without proceeding further to the next filter at the chain.
        } catch (Exception ex){
            httpServletResponse.setStatus(HttpStatus.FORBIDDEN.value());
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            mapper.writeValue(httpServletResponse.getWriter(), createResponseBodyWithError(HttpStatus.FORBIDDEN, ex, "Token not found"));
            return; // return without proceeding further to the next filter at the chain.
        }

        // If token is valid, create a user with username and authority and set it at the spring security context.
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(jwToken.getSubject(), null, jwToken.getAudience());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        filterChain.doFilter(httpServletRequest, httpServletResponse); // Proceeding further to the next filter at the chain.
    }

    // Create a response similar to ResponseStatusException because if i throw a ResponseStatusException it will be printed as a response, but it will be visible at our console.
    private LinkedHashMap<String, Object> createResponseBodyWithError(HttpStatus httpStatus, Exception ex, String message){
        LinkedHashMap<String, Object> errorDetails = new LinkedHashMap();
        errorDetails.put("timestamp", dateTimeFormatter.format(Instant.now()));
        errorDetails.put("status", httpStatus.value());
        if(ex != null) errorDetails.put("error", ex.getMessage());
        errorDetails.put("message", message);
        errorDetails.put("path", "/api/v1/product/**");
        return errorDetails;
    }
}
