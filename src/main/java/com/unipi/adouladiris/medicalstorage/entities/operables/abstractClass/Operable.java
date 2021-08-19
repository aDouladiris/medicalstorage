package com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass;

import com.sun.istack.NotNull;
import com.unipi.adouladiris.medicalstorage.domain.Product;
import com.unipi.adouladiris.medicalstorage.entities.Queryable;
import com.unipi.adouladiris.medicalstorage.entities.operables.Substance;

import javax.persistence.*;

@MappedSuperclass
//@Inheritance(strategy = InheritanceType.JOINED) ????
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

    @Override
    public int compareTo(@NotNull Operable o) {
        StringBuilder name1 = new StringBuilder(), name2 = new StringBuilder();
        if ( this.getClass().getSimpleName().equals("Product") && o.getClass().getSimpleName().equals("Product") ){
            // Get the very first key which is Substance.
            //TODO better implementation
            for ( Substance substance : Product.class.cast(this).getProduct().keySet() ){ name1.append( substance.getName() ); }
            for ( Substance substance : Product.class.cast(o).getProduct().keySet() ){ name2.append( substance.getName() ); }
            return name1.toString().compareToIgnoreCase( name2.toString() );
        }
        else return this.getName().compareToIgnoreCase( o.getName() );
    }

}
