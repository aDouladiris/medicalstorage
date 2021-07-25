package com.unipi.adouladiris.medicalstorage.entities.jointables;

import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
import com.unipi.adouladiris.medicalstorage.entities.operable.Tag;

import javax.persistence.*;

@Entity
@Table(name="substance_tab_category_item_tag", uniqueConstraints = @UniqueConstraint(columnNames={"substanceTabCategoryItem_Id", "tag_Id"}) )
public class SubstanceTabCategoryItemTag extends Joinable {

    // For bidirectional relationship with Substances.
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "substanceTabCategoryItem_Id", nullable = false, referencedColumnName = "Id")
    private SubstanceTabCategoryItem substanceTabCategoryItem;

    // For bidirectional relationship with Tabs.
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_Id", nullable = false, referencedColumnName = "Id")
    private Tag tag;

    public SubstanceTabCategoryItemTag(){}
    public SubstanceTabCategoryItemTag(SubstanceTabCategoryItem substanceTabCategoryItem, Tag tag) { this.substanceTabCategoryItem = substanceTabCategoryItem; this.tag = tag; }
}
