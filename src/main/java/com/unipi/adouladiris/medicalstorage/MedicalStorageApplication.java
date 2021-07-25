package com.unipi.adouladiris.medicalstorage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@SpringBootApplication
public class MedicalStorageApplication {

    static EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.hibernate.medical_storage.jpa");

    public static void main(String[] args) {
        SpringApplication.run(MedicalStorageApplication.class, args);
    }

    @GetMapping("/hello")
    public  String helloWorld(){
        return "Hello World!";
    }

}
