package com.unipi.adouladiris.medicalstorage.database.session;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

// Class containing static information about jpa api.
// All dao classes extend DbEntitySessionManager class to have access to jpa and open a new static session if there is no session.
public abstract class DbEntitySessionManager {
    protected static EntityManagerFactory entityManagerFactory = Persistence
            .createEntityManagerFactory("javax.persistence.medical_storage.jpa");
    protected static SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
    protected static Session session = sessionFactory.openSession();
}
