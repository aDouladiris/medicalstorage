package com.unipi.adouladiris.medicalstorage.entities.jointables;

import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
import com.unipi.adouladiris.medicalstorage.entities.operable.Category;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="substance_tab_category", uniqueConstraints = @UniqueConstraint(columnNames={"category_Id", "substanceTab_Id"}) )
public class SubstanceTabCategory extends Joinable {

    // Manages bidirectional relationship with Categories.
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "category_Id", nullable = false, referencedColumnName = "Id")
    private Category category;

    // For bidirectional relationship with SubstanceTabs.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "substanceTab_Id", nullable = false, referencedColumnName = "Id")
    private SubstanceTab substanceTab;

    // Manages bidirectional relationship with SubstanceTabCategoryItems.
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "substanceTabCategory")
    private Set<SubstanceTabCategoryItem> substanceTabCategoryItemSet;

    public SubstanceTabCategory(){}
    public SubstanceTabCategory(SubstanceTab substanceTab, Category category){ this.substanceTab = substanceTab; this.category = category; }

    public Category getCategory() { return category; }
    public SubstanceTab getSubstanceTab() { return substanceTab; }

}
