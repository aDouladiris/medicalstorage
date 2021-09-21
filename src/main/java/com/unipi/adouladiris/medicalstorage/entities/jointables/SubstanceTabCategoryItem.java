package com.unipi.adouladiris.medicalstorage.entities.jointables;

import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
import com.unipi.adouladiris.medicalstorage.entities.operables.Item;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="substance_tab_category_item", uniqueConstraints = @UniqueConstraint(columnNames={"item_Id", "substanceTabCategory_Id"}) )
public class SubstanceTabCategoryItem extends Joinable {

    // For bidirectional relationship with Items.
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "item_Id", nullable = false, referencedColumnName = "Id")
    private Item item;

    // For bidirectional relationship with SubstanceTabCategories.
    @ManyToOne()
    @JoinColumn(name = "substanceTabCategory_Id", nullable = false, referencedColumnName = "Id")
    private SubstanceTabCategory substanceTabCategory;

    // Manages bidirectional relationship with Tags.
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "substanceTabCategoryItem")
    private Set<SubstanceTabCategoryItemTag> substanceTabCategoryItemTagSet;

    public SubstanceTabCategoryItem(){}
    public SubstanceTabCategoryItem(SubstanceTabCategory substanceTabCategory, Item item) { this.item = item; this.substanceTabCategory = substanceTabCategory; }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public SubstanceTabCategory getSubstanceTabCategory() {
        return substanceTabCategory;
    }

    public void setSubstanceTabCategory(SubstanceTabCategory substanceTabCategory) {
        this.substanceTabCategory = substanceTabCategory;
    }
}
