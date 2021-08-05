package com.unipi.adouladiris.medicalstorage.configuration.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

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
    public static PasswordEncoder passwordEncoder(){
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
                    .passwordEncoder(passwordEncoder())
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
                        .ignoringAntMatchers("/information")
                        .ignoringAntMatchers("/requestToken")
                        .ignoringAntMatchers("/register")
                    .and()
                    .headers().frameOptions().sameOrigin() // TODO These security options need explanation instead of disabled them.
                    .and()
                    .authorizeRequests()
                        .antMatchers("/information").permitAll()
                        .antMatchers("/requestToken").permitAll()
                        .antMatchers("/register").permitAll()
                    .and()
                    .httpBasic();

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

//        @Override
//        public void init(WebSecurity web) throws Exception {
//            web.ignoring().antMatchers("/api/v1/login");
//        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            System.out.println("Token AuthenticateManager has been build!");
            auth.userDetailsService(databaseUserDetailService);
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            System.out.println("Http Interceptor TokenFilter has been build!");
            http.sessionManagement( ).maximumSessions(1). maxSessionsPreventsLogin(false);
            // TODO configuration for different users.
            http.csrf().disable()
                    .authorizeRequests().antMatchers("/api/v1/product/**").permitAll().anyRequest()
                    .authenticated().and().exceptionHandling().and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

            http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        }

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





