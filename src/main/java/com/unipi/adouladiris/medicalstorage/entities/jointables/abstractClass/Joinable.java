package com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass;

import com.unipi.adouladiris.medicalstorage.entities.Queryable;

import javax.persistence.*;

// Marked class in order for entities that extends this class will inherit these fields.
// Additionally, this class won't be translated as entity, thus it won't exists at database.
@MappedSuperclass
public abstract class Joinable extends Queryable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // A.I. not working within embedded keys
    @Column(name = "Id")
    private Integer Id;

    public Integer getId() { return Id; }

}
