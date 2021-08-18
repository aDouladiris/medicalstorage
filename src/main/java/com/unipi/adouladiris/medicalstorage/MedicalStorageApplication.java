package com.unipi.adouladiris.medicalstorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.sql.DataSource;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;


// Is used to mark a configuration class that declares one or more @Bean methods
// and also triggers auto-configuration and component scanning.
// It is scanning our @Component classes that we build.
@SpringBootApplication
public class MedicalStorageApplication {

    // Application entry point.
    public static void main(String[] args) {
        SpringApplication.run(MedicalStorageApplication.class, args);
    }

    // Create an instance of a BCryptPasswordEncoder that we will use later for Basic Authentication.
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Create an instance of datasource for the Basic Authentication manager to look automatically in database.
    @Bean
    public DataSource dataSource(){
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUsername("root");
        dataSource.setPassword("123");
        dataSource.setUrl("jdbc:hsqldb:file:C:/Users/Rg/IdeaProjects/medicalstorage/src/main/resources/database/medicalstorage");
        return dataSource;
    }

    // Create custom query for the Basic Authentication manager in order to get username, password, role and availability in proper order from User entity.
    @Bean
    public String queryFromUserTable(){
        String queryUsers = new StringBuilder().append("select USER.USERNAME, USER.PASSWORD, USER.ENABLED from USER where USER.USERNAME=?").toString();
        return queryUsers;
    }

    // Create custom query for the Basic Authentication manager in order to get username and the corresponding role from my custom entities (User JOIN Role).
    @Bean
    public String queryFromRoleTable(){
        String queryAuthorities = new StringBuilder().append("select U.USERNAME, R.AUTHORITY from ROLE R inner join USER U on R.ID = U.ROLE_ID where U.USERNAME=?").toString();
        return queryAuthorities;
    }

    // Create a custom date formatter for timestamp to string.
    @Bean
    public DateTimeFormatter responseTimeFormatter(){
        return new DateTimeFormatterBuilder()
                .appendPattern("yyyy-MM-dd'T'HH:mm:ss.SSS")
                .appendOffset("+HH:MM", "+00:00")
                .toFormatter().withZone(ZoneId.from(ZoneOffset.UTC));
    }
}
