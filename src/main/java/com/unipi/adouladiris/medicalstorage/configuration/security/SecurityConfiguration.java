package com.unipi.adouladiris.medicalstorage.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
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

//https://stackoverflow.com/questions/35890540/when-to-use-spring-securitys-antmatcher

@Configuration
@EnableWebSecurity
// Override Spring Security default configuration by extending WebSecurityConfigurerAdapter.
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    // TODO get properties from JPA file
    private DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUsername("root");
        dataSource.setPassword("123");
        dataSource.setUrl("jdbc:hsqldb:file:C:/Users/Rg/IdeaProjects/medicalstorage/src/main/resources/database/medicalstorage");
        return dataSource;
    }

    // Authentication
    @Override
    // Validate credentials from database.
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(getDataSource())
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select USER.USERNAME, USER.PASSWORD, USER.ENABLED from USER where USER.USERNAME=?")
                .authoritiesByUsernameQuery("select U.USERNAME, R.AUTHORITY from ROLE R inner join USER U on R.ID = U.ROLE_ID where U.USERNAME=?");
    }

    // Authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // TODO better path: /api/. CORS at tomcat server. Need solution
        http.cors().and().csrf().disable();
        http.authorizeRequests()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin();



//        http.authorizeRequests()
////                .antMatchers("/**").hasRole("admin")
//                .antMatchers("/admin/").hasRole("admin")
//                .antMatchers("/customer/").hasRole("customer")
////                .antMatchers("/").permitAll()
//                .and()
//                .formLogin();


//                //.httpBasic();
    }


}


