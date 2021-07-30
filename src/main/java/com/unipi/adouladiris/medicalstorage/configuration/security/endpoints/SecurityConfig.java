package com.unipi.adouladiris.medicalstorage.configuration.security.endpoints;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.ObjectInputFilter;
import java.util.logging.Logger;

//@Configuration
//@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

//    /**
//     * Following Multiple HttpSecurity approach:
//     * http://docs.spring.io/spring-security/site/docs/3.2.x/reference/htmlsingle/#multiple-httpsecurity
//     */
//    @Configuration
//    @Order(1)
//    public static class ManagerEndpointsSecurityConfig extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .antMatcher("/management/**").authorizeRequests().anyRequest().hasRole("admin").and()
//                    .httpBasic();
//        }
//    }
//
//    /**
//     * Following Multiple HttpSecurity approach:
//     * http://docs.spring.io/spring-security/site/docs/3.2.x/reference/htmlsingle/#multiple-httpsecurity
//     */
//    @Configuration
//    public static class ResourceEndpointsSecurityConfig extends WebSecurityConfigurerAdapter {
//
//
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//
//            http
//                    //fyi: This adds it to the spring security proxy filter chain
//                    .addFilterBefore(createBBAuthenticationFilter(), BasicAuthenticationFilter.class)
//            ;
//        }
//    }



//    @Configuration
//    @Order(2)
//    public static class AdminEndpointsSecurityConfig extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .antMatcher("/product/admin").authorizeRequests().anyRequest().hasRole("admin").and()
//                    .httpBasic();
//        }
//    }
//    @Configuration
//    @Order(3)
//    public static class CustomerEndpointsSecurityConfig extends WebSecurityConfigurerAdapter {
//
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .antMatcher("/product/cs/**").authorizeRequests().anyRequest().hasRole("customer").and()
//                    .httpBasic();
//        }
//    }



}