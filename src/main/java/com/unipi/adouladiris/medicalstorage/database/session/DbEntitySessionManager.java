package com.unipi.adouladiris.medicalstorage.database.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.sql.DataSource;


@Component
public abstract class DbEntitySessionManager {
    //TODO review names. session -> em
    protected static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("javax.persistence.medical_storage.jpa");
    protected static SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    protected static Session session = sessionFactory.openSession();

}
