package com.unipi.adouladiris.medicalstorage.database.operations.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public abstract class SessionManager {
    protected static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.medical_storage.jpa");
    protected static SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    protected static Session session = sessionFactory.openSession();
}
