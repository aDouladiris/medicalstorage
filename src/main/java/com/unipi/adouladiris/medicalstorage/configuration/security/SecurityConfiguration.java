package com.unipi.adouladiris.medicalstorage.configuration.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import static java.lang.String.format;


// https://stackoverflow.com/questions/35890540/when-to-use-spring-securitys-antmatcher
// https://stackoverflow.com/questions/58995870/why-do-we-need-to-call-http-addfilterbefore-method-in-spring-security-configur
// checkhttps://www.toptal.com/spring/spring-security-tutorial
// newest????https://www.baeldung.com/spring-security-oauth-resource-server
// readhttps://medium.com/javarevisited/spring-security-jwt-authentication-in-detail-bb98b5055b50
@Configuration
@EnableWebSecurity
// TODO "generated security password" message need to be removed.
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
// Override Spring Security default configuration by extending WebSecurityConfigurerAdapter.
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

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

   // Authentication
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.jdbcAuthentication()
//                .dataSource(dataSource())
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .usersByUsernameQuery(getQueryFromUserTable())
//                .authoritiesByUsernameQuery(getQueryFromRoleTable())
//                .rolePrefix("ROLE_"); // Framework needs ROLE_ prefix. Apply if there is not in database. TODO review db names and values
//
//    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.userDetailsService(userDetailService);
//        auth.jdbcAuthentication()
//                .dataSource(dataSource())
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .usersByUsernameQuery(getQueryFromUserTable())
//                .authoritiesByUsernameQuery(getQueryFromRoleTable())
//                .rolePrefix("ROLE_"); // Framework needs ROLE_ prefix. Apply if there is not in database. TODO review db names and values

//        auth.userDetailsService(username -> userRepo
//                .findByUsername(username)
//                .orElseThrow(
//                        () -> new UsernameNotFoundException(
//                                format("User: %s, not found", username)
//                        )
//                ));
    }


    private final JwtTokenFilter jwtTokenFilter;
    private DatabaseUserDetailService userDetailService;

    @Autowired
    public SecurityConfiguration(JwtTokenFilter jwtTokenFilter, DatabaseUserDetailService userDetailService) {
        super();
        this.jwtTokenFilter = jwtTokenFilter;
        this.userDetailService = userDetailService;
        // Inherit security context in async function calls
        //SecurityContextHolder.setStrategyName(SecurityContextHolder.MODE_INHERITABLETHREADLOCAL);
    }

    // Authorization
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//
//        http
//                .csrf().ignoringAntMatchers("/api/v1/**") // TODO These security options need explanation instead of disabled them.
//                .and()
//                .headers().frameOptions().sameOrigin() // TODO These security options need explanation instead of disabled them.
//                .and()
//                .authorizeRequests().antMatchers("/api/v1/login").permitAll()
//                .and()
//                .authorizeRequests().antMatchers("/api/v1/**").hasAnyRole("admin", "customer")
//                .and()
//                .httpBasic();
//
//                // Set unauthorized requests exception handler
//                http = http
//                        .exceptionHandling()
//                        .authenticationEntryPoint(
//                                (request, response, ex) -> {
//                                    response.sendError(
//                                            HttpServletResponse.SC_UNAUTHORIZED,
//                                            ex.getMessage()
//                                    );
//                                }
//                        )
//                        .and();
//
//
//        // Add JWT token filter
//        http.addFilterBefore( jwtTokenFilter, UsernamePasswordAuthenticationFilter.class );
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Enable CORS and disable CSRF
//        http = http.cors().and().csrf().disable();
//
//        // Set session management to stateless
////        http = http
////                .sessionManagement()
////                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
////                .and();
////
////        // Set unauthorized requests exception handler
////        http = http
////                .exceptionHandling()
////                .authenticationEntryPoint(
////                        (request, response, ex) -> {
////                            response.sendError(
////                                    HttpServletResponse.SC_UNAUTHORIZED,
////                                    ex.getMessage()
////                            );
////                        }
////                )
////                .and();
//
//        // Set permissions on endpoints
//        http.authorizeRequests()
//                // Our public endpoints
//                .antMatchers("/api/v1/**").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .oauth2ResourceServer()
//                .jwt();
//
//
//        // Add JWT token filter
//        http.addFilterBefore(
//                jwtTokenFilter,
//                UsernamePasswordAuthenticationFilter.class
//        );

//        http
//                .csrf().ignoringAntMatchers("/api/v1/**") // TODO These security options need explanation instead of disabled them.
//                .and()
//                .headers().frameOptions().sameOrigin() // TODO These security options need explanation instead of disabled them.
//                .and()
//                .authorizeRequests().antMatchers("/api/v1/login").permitAll()
//                .and()
//                .authorizeRequests().antMatchers("/api/v1/**").hasAnyRole("admin", "customer")
//                .and()
//                .httpBasic();

        http.csrf().disable().authorizeRequests().antMatchers("/api/v1/**").permitAll().anyRequest()
                .authenticated().and().exceptionHandling().and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);



    }

//    @Bean
//    JwtDecoder jwtDecoder(OAuth2ResourceServerProperties properties) {
//        NimbusJwtDecoder jwtDecoder = NimbusJwtDecoder.withJwkSetUri(properties.getJwt().getJwkSetUri()).build();
//        jwtDecoder.setClaimSetConverter(new OrganizationSubClaimAdapter());
//        return jwtDecoder;
//    }



    @Override
    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }



//    @Override
//    @Bean("myDS")
//    public UserDetailsService userDetailsServiceBean() throws Exception {
//        return super.userDetailsServiceBean();
//    }

}





