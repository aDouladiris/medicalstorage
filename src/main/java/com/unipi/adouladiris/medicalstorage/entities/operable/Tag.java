package com.unipi.adouladiris.medicalstorage.entities.operable;

import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategoryItemTag;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tag")
public class Tag extends Operable {

    // Manages bidirectional relationship with Tags.
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "tag")
    private Set<SubstanceTabCategoryItemTag> substanceTabCategoryItemTagSet;

    public Tag(){}
    public Tag(String name){ setName(name); }

}