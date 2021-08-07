package com.unipi.adouladiris.medicalstorage;

import com.unipi.adouladiris.medicalstorage.database.dao.insert.Insert;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.users.Role;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.net.URI;
import java.util.List;

import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@SpringBootApplication
public class MedicalStorageApplication {

    public static void main(String[] args) {
//        new Insert().user("cs", "456", "customer");
//        DbResult dbResult = new Select().findUser("arg");
//        User user = dbResult.getResult(User.class);
//
//        System.out.println("username: " + user.getUsername());
//        System.out.println("password: " + user.getPassword());
//        System.out.println("role:     " + user.getAuthority());


//        StringBuilder select = new StringBuilder();
//        select.append("FROM Role as role WHERE role.authority = :tmpName ");
//
//        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("org.hibernate.medical_storage.jpa");
//        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
//        Session session = sessionFactory.openSession();
//        if ( !session.getTransaction().isActive() ) { session.getTransaction().begin(); }
////
////        Role role = session.find(Role.class, 1);
////        session.getTransaction().commit();
//
//        Query query = session.createQuery( select.toString() );
//        query.setParameter("tmpName", "admin");
//        //System.out.println("Query: " + query.unwrap(org.hibernate.Query.class).getQueryString());
//        List<Object[]> queryResultList = query.getResultList(); // Result contains rows, row contains columns
//
//        System.out.println("Result class: " + queryResultList.get(0).toString() );
////        DbResult dbResult = new DbResult();
////        if( queryResultList.isEmpty() ){ return dbResult; }
////        dbResult.setResult( queryResultList.get(0) );
////
////        System.out.println(role.getId());
////        System.out.println(role.getAuthority());



        SpringApplication.run(MedicalStorageApplication.class, args);

    }

}
