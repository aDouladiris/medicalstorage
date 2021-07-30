package com.unipi.adouladiris.medicalstorage.entities.users;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.business.Product;
import com.unipi.adouladiris.medicalstorage.entities.Queryable;
import com.unipi.adouladiris.medicalstorage.entities.operable.Substance;

import javax.persistence.*;

@MappedSuperclass
//@Inheritance(strategy = InheritanceType.JOINED) ????
public abstract class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // A.I. not working within embedded keys
    @Column(name = "Id")
    private Integer Id;
    public Integer getId() {
        return Id;
    }

}
