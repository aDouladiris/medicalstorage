package com.unipi.adouladiris.medicalstorage.database.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;

public abstract class SessionManager {
    protected static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.medical_storage.jpa");
    protected static SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    protected static Session session = sessionFactory.openSession();

    @Autowired
    private DataSource dataSource;

    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();

        dataSource.setDriverClassName("org.hsqldb.jdbcDriver");
        dataSource.setUsername("root");
        dataSource.setPassword("123");
        dataSource.setUrl("jdbc:hsqldb:file:C:/Users/Rg/IdeaProjects/medicalstorage/src/main/resources/database/medicalstorage");

        return dataSource;
    }


}
