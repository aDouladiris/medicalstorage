package com.unipi.adouladiris.medicalstorage.entities.operables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategory;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "category")
public class Category extends Operable {

    // Manages bidirectional relationship with SubstanceTabCategories.
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "category")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<SubstanceTabCategory> substanceTabCategorySet;

    public Category(){}
    public Category(String name){ setName(name); }

}
