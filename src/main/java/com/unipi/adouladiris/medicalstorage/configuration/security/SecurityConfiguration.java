package com.unipi.adouladiris.medicalstorage.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import javax.sql.DataSource;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.security.config.Customizer.withDefaults;

// https://stackoverflow.com/questions/35890540/when-to-use-spring-securitys-antmatcher
// https://stackoverflow.com/questions/58995870/why-do-we-need-to-call-http-addfilterbefore-method-in-spring-security-configur


@Configuration
@EnableWebSecurity
// Override Spring Security default configuration by extending WebSecurityConfigurerAdapter.
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // TODO get properties from JPA file
    // Authentication
    // Validate credentials from database.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUsername("root");
        dataSource.setPassword("123");
        dataSource.setUrl("jdbc:hsqldb:file:C:/Users/Rg/IdeaProjects/medicalstorage/src/main/resources/database/medicalstorage");

        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select USER.USERNAME, USER.PASSWORD, USER.ENABLED from USER where USER.USERNAME=?")
                .authoritiesByUsernameQuery("select U.USERNAME, R.AUTHORITY from ROLE R inner join USER U on R.ID = U.ROLE_ID where U.USERNAME=?")
                .rolePrefix("ROLE_"); // Framework needs ROLE_ prefix. Apply if there is not in database. TODO review db names and values
    }

    // Authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO better path: /api/. CORS at tomcat server. Need solution
        http
            .cors()
            .and()
            .csrf()
                .disable().
            authorizeRequests()
                .antMatchers(HttpMethod.GET, "/api/v1/user").hasRole("admin")
                .and()
                .httpBasic();


//        http.antMatcher("/api/v1/user")
//                .authorizeRequests(authorize -> authorize
//                        .anyRequest().hasRole("admin")
//                )
//                .httpBasic(withDefaults());


//            .and()
//            .authorizeRequests()
////                .antMatchers(HttpMethod.GET, "/api/v1/product/all").hasRole("admin")
//                //.antMatchers("/api/v1/**").hasRole("admin")
//                .anyRequest().permitAll()
//                .and()
//                .formLogin()
//                .and()
//                .httpBasic();



//        http.csrf()
//                .ignoringAntMatchers("/h2-console/**");
//        http.headers()
//                .frameOptions()
//                .sameOrigin();

    }







}





