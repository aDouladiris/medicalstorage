package com.unipi.adouladiris.medicalstorage.entities.operables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategoryItemTag;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag extends Operable {

    // Manages bidirectional relationship with Tags.
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "tag")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<SubstanceTabCategoryItemTag> substanceTabCategoryItemTagSet;

    public Tag(){}
    public Tag(String name){ setName(name); }

}