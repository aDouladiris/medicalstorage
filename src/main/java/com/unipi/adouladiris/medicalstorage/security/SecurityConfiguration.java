package com.unipi.adouladiris.medicalstorage.security;
import com.unipi.adouladiris.medicalstorage.filters.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;

import javax.sql.DataSource;

//@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfiguration {

    @Configuration
    @Order(1)
    public static class UserSessionConfiguration extends WebSecurityConfigurerAdapter{ // Override Spring Security default configuration by extending WebSecurityConfigurerAdapter.

        private BCryptPasswordEncoder bCryptPasswordEncoder;
        @Autowired
        private void setbCryptPasswordEncoder(BCryptPasswordEncoder bCryptPasswordEncoder){this.bCryptPasswordEncoder = bCryptPasswordEncoder;}

        private DataSource dataSource;
        @Autowired
        private void setDataSource(DataSource dataSource){this.dataSource = dataSource;}

        private String queryFromUserTable;
        @Autowired
        private void setQueryFromUserTable(String queryFromUserTable){this.queryFromUserTable = queryFromUserTable;}

        private String queryFromRoleTable;
        @Autowired
        private void setQueryFromRoleTable(String queryFromRoleTable){this.queryFromRoleTable = queryFromRoleTable;}

        // Configure AuthenticationManager to perform Basic authentication taking username and password and
        // query them at database to find matching records.
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.jdbcAuthentication()
                    .dataSource(dataSource)
                    .passwordEncoder(bCryptPasswordEncoder)
                    .usersByUsernameQuery(queryFromUserTable)
                    .authoritiesByUsernameQuery(queryFromRoleTable)
                    .rolePrefix("ROLE_"); // Spring Security needs ROLE_ prefix. Apply if there is not in database.
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    // We need CSRF tokens to properly check for CSRF attacks and correctly forwarding our http requests.
                    // In our case, we just disable csrf for our particular endpoints.
                    .csrf()
                        .ignoringAntMatchers("/api/v1/user/**")
                        .ignoringAntMatchers("/swagger-ui.html?urls.primaryName=Medical%20Storage%20Api%20v1%20-%20User#/User")
                    .and()
                    .antMatcher("/api/v1/user/**")
                        .authorizeRequests().anyRequest().permitAll();
        }

        @Bean
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }
    }

    @Configuration
    @Order(2)
    public static class UserTokenConfiguration extends WebSecurityConfigurerAdapter{ // Override Spring Security default configuration by extending WebSecurityConfigurerAdapter.
        private JwtTokenFilter jwtTokenFilter;
        @Autowired
        public void jwtTokenFilter(JwtTokenFilter jwtTokenFilter){
            this.jwtTokenFilter = jwtTokenFilter;
        }

        // Configure the DefaultSecurityFilterChain by registering our custom Jwt filter right after the SecurityContextPersistenceFilter.
        // If the filter is successful then we assign a user with username and authority to the SecurityContext so we would not need an
        // AuthenticationManager later to avoid querying the database.
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .addFilterAfter(jwtTokenFilter, SecurityContextPersistenceFilter.class)
                    // We need CSRF tokens to properly check for CSRF attacks and correctly forwarding our http requests.
                    // In our case, we just disable csrf for our particular endpoints.
                    .csrf()
                        .ignoringAntMatchers("/api/v1/product/**")
                    .and()
                    .antMatcher("/api/v1/product/**")
                        .authorizeRequests().anyRequest().hasAnyRole("admin", "customer")
                        .and()
                        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        }
    }
}





