package com.unipi.adouladiris.medicalstorage.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

// https://stackoverflow.com/questions/35890540/when-to-use-spring-securitys-antmatcher
// https://stackoverflow.com/questions/58995870/why-do-we-need-to-call-http-addfilterbefore-method-in-spring-security-configur

@Configuration
@EnableWebSecurity
// TODO "generated security password" message need to be removed.
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
// Override Spring Security default configuration by extending WebSecurityConfigurerAdapter.
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private static DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUsername("root");
        dataSource.setPassword("123");
        dataSource.setUrl("jdbc:hsqldb:file:C:/Users/Rg/IdeaProjects/medicalstorage/src/main/resources/database/medicalstorage");
        return dataSource;
    }

    private static String getQueryFromUserTable(){
        String queryUsers = new StringBuilder().append("select USER.USERNAME, USER.PASSWORD, USER.ENABLED from USER where USER.USERNAME=?").toString();
        return queryUsers;
    }

    private static String getQueryFromRoleTable(){
        String queryAuthorities = new StringBuilder().append("select U.USERNAME, R.AUTHORITY from ROLE R inner join USER U on R.ID = U.ROLE_ID where U.USERNAME=?").toString();
        return queryAuthorities;
    }

    // Authentication
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource())
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery(getQueryFromUserTable())
                .authoritiesByUsernameQuery(getQueryFromRoleTable())
                .rolePrefix("ROLE_"); // Framework needs ROLE_ prefix. Apply if there is not in database. TODO review db names and values
    }

    // Authorization
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().ignoringAntMatchers("/api/v1/**") // TODO These security options need explanation instead of disabled them.
                .and()
                .headers().frameOptions().sameOrigin() // TODO These security options need explanation instead of disabled them.
                .and()
                .authorizeRequests().antMatchers("/api/v1/**").hasAnyRole("admin", "customer")
                .and()
                .httpBasic();
    }


}





