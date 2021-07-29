package com.unipi.adouladiris.medicalstorage.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;


import javax.sql.DataSource;


@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//    @Autowired
//    public void initialize(AuthenticationManagerBuilder builder, DataSource dataSource) throws Exception {
//        builder.jdbcAuthentication().dataSource(dataSource).withUser("dave")
//                .password("secret").roles("USER");
//    }


    // Authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
//        .jdbcAuthentication()
            .inMemoryAuthentication()
            .withUser("Argiris")
            .password("1")
            .roles("user");
    }



//    // Authorization
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
////        http.authorizeRequests()
////                .antMatchers("/product").hasAnyRole("user");
//    }




}
