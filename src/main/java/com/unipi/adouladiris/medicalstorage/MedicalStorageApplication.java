package com.unipi.adouladiris.medicalstorage;

import com.unipi.adouladiris.medicalstorage.database.dao.insert.Insert;
import com.unipi.adouladiris.medicalstorage.database.dao.result.DbResult;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.users.User;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;

import static org.springframework.web.servlet.function.RequestPredicates.GET;
import static org.springframework.web.servlet.function.RouterFunctions.route;

@SpringBootApplication
public class MedicalStorageApplication {

    public static void main(String[] args) {
//        new Insert().user("arg", "123", "admin");
//        DbResult dbResult = new Select().findUser("arg");
//        User user = dbResult.getResult(User.class);
//
//        System.out.println("username: " + user.getUsername());
//        System.out.println("password: " + user.getPassword());
//        System.out.println("role:     " + user.getAuthority());

        SpringApplication.run(MedicalStorageApplication.class, args); }

}
