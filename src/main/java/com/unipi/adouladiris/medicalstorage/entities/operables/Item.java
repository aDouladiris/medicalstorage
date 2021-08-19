package com.unipi.adouladiris.medicalstorage.entities.operables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategoryItem;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "item")
public class Item extends Operable {

    @Lob
    @Column(name = "description", nullable = false)
    private String description;

    // Manages bidirectional relationship with SubstanceTabs.
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "item")
    private Set<SubstanceTabCategoryItem> substanceTabCategoryItemSet;

    public Item(){}
    public Item(String name, String description){ setName(name); setDescription(description); }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

}
