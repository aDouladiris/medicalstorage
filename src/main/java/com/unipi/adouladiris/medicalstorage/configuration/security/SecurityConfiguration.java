package com.unipi.adouladiris.medicalstorage.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;



import javax.sql.DataSource;

//https://stackoverflow.com/questions/35890540/when-to-use-spring-securitys-antmatcher

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    // Authentication
    @Autowired
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

//        auth.jdbcAuthentication()
//                .dataSource(dataSource)
//                .withUser(User.withUsername("arg")
//                        .password(passwordEncoder().encode("123"))
//                        .roles("admin"));


        auth.jdbcAuthentication()
//                .withDefaultSchema()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, enabled from USER where USERNAME=?")
                .authoritiesByUsernameQuery("select U.USERNAME, AUTHORITY from role inner join USER U on ROLE.ID = U.ROLE_ID where U.USERNAME=?");


    }


    // Authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .antMatcher("/**") // better path: /api/
                .authorizeRequests()
                .anyRequest().hasRole("admin")
                .and()
                .httpBasic();
    }



}


