package com.unipi.adouladiris.medicalstorage.entities.users;

import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;

import javax.persistence.*;

@MappedSuperclass
public abstract class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // A.I. not working within embedded keys
    @Column(name = "Id")
    private Integer Id;
    public Integer getId() {
        return Id;
    }

}

