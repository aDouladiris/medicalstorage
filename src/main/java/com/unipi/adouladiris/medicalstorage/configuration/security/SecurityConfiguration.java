package com.unipi.adouladiris.medicalstorage.configuration.security;
import com.unipi.adouladiris.medicalstorage.configuration.security.filters.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Bean
    public static BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public static DataSource dataSource(){
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

    @Configuration
    @Order(1)
    public static class UserSessionConfiguration extends WebSecurityConfigurerAdapter{

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            System.out.println("Session AuthenticateManager has been build!");
            auth.jdbcAuthentication()
                    .dataSource(dataSource())
                    .passwordEncoder(bCryptPasswordEncoder())
                    .usersByUsernameQuery(getQueryFromUserTable())
                    .authoritiesByUsernameQuery(getQueryFromRoleTable())
                    .rolePrefix("ROLE_"); // Framework needs ROLE_ prefix. Apply if there is not in database. TODO review db names and values
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            System.out.println("Session Http Config has been build!");
//            http.sessionManagement( ).maximumSessions(1). maxSessionsPreventsLogin(false);

            http
                    .csrf() // TODO These security options need explanation instead of disabled them.
                        .ignoringAntMatchers("/**")
                    .and()
                    .headers().frameOptions().sameOrigin() // TODO These security options need explanation instead of disabled them.
                    .and()
                    .authorizeRequests()
                        .antMatchers("/api/v1/user/**").permitAll();
//                    .and()
//                    .httpBasic();
            http.httpBasic().disable();

        }

        // TODO review with Qualifiers.
        @Override
        @Primary
        @Bean("UserSessionAuthManager")
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

    }


    @Configuration
    @Order(2)
    public static class UserTokenConfiguration extends WebSecurityConfigurerAdapter{

        private DatabaseUserDetailService databaseUserDetailService;
        @Autowired
        public void userDetailService(DatabaseUserDetailService databaseUserDetailService) { this.databaseUserDetailService = databaseUserDetailService; }

        private JwtTokenFilter jwtTokenFilter;
        @Autowired
        public void jwtTokenFilter(JwtTokenFilter jwtTokenFilter){
            this.jwtTokenFilter = jwtTokenFilter;
        }

//        private AccessDeniedExceptionFilter accessDeniedExceptionFilter;
//        @Autowired
//        public void accessDeniedExceptionFilter(AccessDeniedExceptionFilter accessDeniedExceptionFilter){ this.accessDeniedExceptionFilter = accessDeniedExceptionFilter; }



        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            System.out.println("Token AuthenticateManager has been build!");
            auth.userDetailsService(databaseUserDetailService);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            System.out.println("Http Interceptor TokenFilter has been build!");
            //http.sessionManagement( ).maximumSessions(1). maxSessionsPreventsLogin(false);
            // TODO configuration for different users.

            //https://docs.spring.io/spring-security/site/docs/3.0.x/reference/security-filter-chain.html
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

            //http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);//
        }

//        private AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandler() {
//            @Override
//            public void handle(
//                    HttpServletRequest httpServletRequest,
//                    HttpServletResponse httpServletResponse,
//                    AccessDeniedException e) throws IOException, ServletException {
//
//
//
//
//            }
//        };

        @Override
        @Bean(value = "UserTokenAuthManager")
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        // TODO review with Qualifiers.
        @Override
        @Primary
        @Bean("UserTokenDetailsService")
        public UserDetailsService userDetailsServiceBean() throws Exception {
            return super.userDetailsServiceBean();
        }

    }


}





