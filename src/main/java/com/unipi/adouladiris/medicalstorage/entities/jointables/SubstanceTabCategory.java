package com.unipi.adouladiris.medicalstorage.entities.jointables;

import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
import com.unipi.adouladiris.medicalstorage.entities.operables.Category;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
// Unique constraints for a pair of fields
@Table(name="substance_tab_category", uniqueConstraints = @UniqueConstraint(columnNames={"category_Id", "substanceTab_Id"}) )
public class SubstanceTabCategory extends Joinable {

    // Manages bidirectional relationship with Categories.
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "category_Id", nullable = false, referencedColumnName = "Id")
    private Category category;

    // For bidirectional relationship with SubstanceTabs.
    @ManyToOne()
    @JoinColumn(name = "substanceTab_Id", nullable = false, referencedColumnName = "Id")
    private SubstanceTab substanceTab;

    // Manages bidirectional relationship with SubstanceTabCategoryItems.
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "substanceTabCategory")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<SubstanceTabCategoryItem> substanceTabCategoryItemSet;

    public SubstanceTabCategory(){}
    public SubstanceTabCategory(SubstanceTab substanceTab, Category category){ this.substanceTab = substanceTab; this.category = category; }

    public Category getCategory() { return category; }

    public void setCategory(Category category) {
        this.category = category;
    }

    public SubstanceTab getSubstanceTab() { return substanceTab; }

    public Set<SubstanceTabCategoryItem> getSubstanceTabCategoryItemSet() {
        return substanceTabCategoryItemSet;
    }
}
