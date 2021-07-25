package com.unipi.adouladiris.medicalstorage.entities.operable;

import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategory;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category extends Operable {

    // Manages bidirectional relationship with SubstanceTabCategories.
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "category")
    private Set<SubstanceTabCategory> substanceTabCategorySet;

    public Category(){}
    public Category(String name){ setName(name); }

}
