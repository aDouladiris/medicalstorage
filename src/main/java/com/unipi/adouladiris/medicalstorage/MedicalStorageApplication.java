package com.unipi.adouladiris.medicalstorage;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// Is used to mark a configuration class that declares one or more @Bean methods
// and also triggers auto-configuration and component scanning.
// It is scanning our @Component classes that we build.
public class MedicalStorageApplication {

    // Application entry point.
    public static void main(String[] args) {
        SpringApplication.run(MedicalStorageApplication.class, args);
    }

}
