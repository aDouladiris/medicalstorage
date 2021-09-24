package com.unipi.adouladiris.medicalstorage.entities.operables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTabCategoryItem;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@AttributeOverride(name = "name", column = @Column(name = "name")) // Override parent attribute name with this name field.
// Unique constraints for a pair of fields
@Table(name = "item", uniqueConstraints = @UniqueConstraint(columnNames={"name", "description"}))
public class Item extends Operable {

    // Redeclare this field because we do not want it unique in this table. Item instances can have any names or descriptions.
    @Column(name = "name", nullable = false, unique = false)
    protected String name;

    // Set max lob length 2147483647 bytes for maximum text capacity.
    @Lob
    @Column(name = "description", nullable = false, length = 2147483647)
    private String description;

    // Manages bidirectional relationship with SubstanceTabs.
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "item")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<SubstanceTabCategoryItem> substanceTabCategoryItemSet;

    public Item(){}
    public Item(String name, String description){ setName(name); setDescription(description); }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}
