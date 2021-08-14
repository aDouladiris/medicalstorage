package com.unipi.adouladiris.medicalstorage.database.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

public abstract class SessionManager {
    //TODO review names. session -> em
    protected static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.medical_storage.jpa");
    protected static SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    protected static Session session = sessionFactory.openSession();

    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Autowired
//    private Environment env;
//
//    @Primary
//    @Bean
//    public DataSource userDataSource() {
//
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));
//        dataSource.setUrl(env.getProperty("user.jdbc.url"));
//        dataSource.setUsername(env.getProperty("jdbc.user"));
//        dataSource.setPassword(env.getProperty("jdbc.pass"));
//
//        return dataSource;
//    }

//    @Autowired
//    private DataSource dataSource;

//    @Bean
//    @Autowired
//    public DataSource dataSource() {
//        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//
//        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
//        dataSource.setUsername("root");
//        dataSource.setPassword("123");
//        dataSource.setUrl("jdbc:hsqldb:file:C:/Users/Rg/IdeaProjects/medicalstorage/src/main/resources/database/medicalstorage");
//
//        return dataSource;
//    }


//    @ConfigurationProperties(prefix="spring.datasource")
//    @Bean
//    public DataSource getDataSource() { return DataSourceBuilder.create().build(); }


}
