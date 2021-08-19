package com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.Queryable;
import com.unipi.adouladiris.medicalstorage.entities.operables.Substance;

import javax.persistence.*;

@MappedSuperclass
public abstract class Operable extends Queryable implements Comparable<Operable>  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // A.I. not working within embedded keys
    @Column(name = "Id")
    private Integer Id;

    @Column(name = "name", nullable = false, unique = true)
    protected String name;

    public Integer getId() {
        return Id;
    }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    // We cannot compare Object as they are not primitive types.
    // But we can extract the common field name and compare the corresponding letters to sort the Object alphabetically.
    @Override
    public int compareTo(@NotNull Operable operableToCompare) {
        StringBuilder name1 = new StringBuilder(), name2 = new StringBuilder();
        if ( this.getClass().getSimpleName().equals("Product") && operableToCompare.getClass().getSimpleName().equals("Product") ){
            // Get the very first key which is Substance.
            Product.class.cast(this).getProduct().keySet().forEach(key -> name1.append( key.getName() ));
            Product.class.cast(operableToCompare).getProduct().keySet().forEach(key -> name2.append( key.getName() ));
            return name1.toString().compareToIgnoreCase( name2.toString() );
        }
        else return this.getName().compareToIgnoreCase( operableToCompare.getName() );
    }
}
