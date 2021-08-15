package com.unipi.adouladiris.medicalstorage.configuration.security;
import com.unipi.adouladiris.medicalstorage.configuration.security.filters.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.session.SessionManagementFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import java.io.IOException;

import static java.lang.String.format;


// https://stackoverflow.com/questions/35890540/when-to-use-spring-securitys-antmatcher
// https://stackoverflow.com/questions/58995870/why-do-we-need-to-call-http-addfilterbefore-method-in-spring-security-configur
// checkhttps://www.toptal.com/spring/spring-security-tutorial
// newest????https://www.baeldung.com/spring-security-oauth-resource-server
// readhttps://medium.com/javarevisited/spring-security-jwt-authentication-in-detail-bb98b5055b50

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
// Override Spring Security default configuration by extending WebSecurityConfigurerAdapter.
public class SecurityConfiguration {

    private static BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder){SecurityConfiguration.bCryptPasswordEncoder = bCryptPasswordEncoder;}

    private static DataSource dataSource;
    @Autowired
    private void setDataSource(DataSource dataSource){SecurityConfiguration.dataSource = dataSource;}

    private static String queryFromUserTable;
    @Autowired
    private void setQueryFromUserTable(String queryFromUserTable){SecurityConfiguration.queryFromUserTable = queryFromUserTable;}

    private static String queryFromRoleTable;
    @Autowired
    private void setQueryFromRoleTable(String queryFromRoleTable){SecurityConfiguration.queryFromRoleTable = queryFromRoleTable;}

    @Configuration
    @Order(1)
    public static class UserSessionConfiguration extends WebSecurityConfigurerAdapter{

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.jdbcAuthentication()
                    .dataSource(dataSource)
                    .passwordEncoder(bCryptPasswordEncoder)
                    .usersByUsernameQuery(queryFromUserTable)
                    .authoritiesByUsernameQuery(queryFromRoleTable)
                    .rolePrefix("ROLE_"); // Framework needs ROLE_ prefix. Apply if there is not in database.
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .csrf() // TODO These security options need explanation instead of disabled them.
                        .ignoringAntMatchers("/**")
                    .and()
                    .headers().frameOptions().sameOrigin() // TODO These security options need explanation instead of disabled them.
                    .and()
                    .authorizeRequests()
                        .antMatchers("/api/v1/user/**").permitAll();
            http.httpBasic().disable();
        }

        // TODO review with Qualifiers.
        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }

    @Configuration
    @Order(2)
    public static class UserTokenConfiguration extends WebSecurityConfigurerAdapter{
        private JwtTokenFilter jwtTokenFilter;
        @Autowired
        public void jwtTokenFilter(JwtTokenFilter jwtTokenFilter){
            this.jwtTokenFilter = jwtTokenFilter;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .addFilterBefore(jwtTokenFilter, SessionManagementFilter.class)
                    //.addFilterBefore(accessDeniedExceptionFilter, AccessDeniedExceptionFilter.class)
                    .csrf().disable()
                    .antMatcher("/api/v1/product/**")
                        .authorizeRequests().anyRequest().hasAnyRole("admin", "customer")
                    .and()
                    .sessionManagement()
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                    .and()
                    .httpBasic();
        }
    }
}





