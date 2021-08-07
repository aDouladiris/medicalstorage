package com.unipi.adouladiris.medicalstorage.configuration.security.filters;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

//@Component
//public class CustomLogoutFilter extends GenericFilterBean {
//
//
//    @Override
//    public void doFilter(ServletRequest servletRequest,
//                         ServletResponse servletResponse,
//                         FilterChain filterChain) throws IOException, ServletException {
//
//        HttpServletRequest servRequest = (HttpServletRequest) servletRequest;
//        HttpSession sessionReq = servRequest.getSession(false);
//
//        HttpServletResponse servResponse = (HttpServletResponse) servletResponse;
//        Boolean sessionRes = servResponse.isCommitted();
//
//
//
//        System.out.println("Protocol: " + servletRequest.getProtocol() );
//        if(sessionReq != null) System.out.println("Request: " + sessionReq.getServletContext().toString());
//        System.out.println("Response: " + sessionRes);
//        System.out.println("------------------------");
//
//        filterChain.doFilter(servletRequest, servletResponse);
//    }
//}
