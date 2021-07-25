package com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass;

import com.unipi.adouladiris.medicalstorage.entities.Queryable;

import javax.persistence.*;

@MappedSuperclass
//@Inheritance(strategy = InheritanceType.JOINED) ????
public abstract class Joinable extends Queryable {

    @javax.persistence.Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // A.I. not working within embedded keys
    @Column(name = "Id")
    private Integer Id;

    public Integer getId() { return Id; }

}
