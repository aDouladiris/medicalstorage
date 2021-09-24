package com.unipi.adouladiris.medicalstorage.entities.jointables;

import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
import com.unipi.adouladiris.medicalstorage.entities.operables.Tag;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
// Unique constraints for a pair of fields
@Table(name="substance_tab_category_item_tag", uniqueConstraints = @UniqueConstraint(columnNames={"substanceTabCategoryItem_Id", "tag_Id"}) )
public class SubstanceTabCategoryItemTag extends Joinable {

    // For bidirectional relationship with SubstanceTabCategoryItems.
    @ManyToOne
    @JoinColumn(name = "substanceTabCategoryItem_Id", nullable = false, referencedColumnName = "Id")
    private SubstanceTabCategoryItem substanceTabCategoryItem;

    // For bidirectional relationship with Tags.
    @JoinColumn(name = "tag_Id", nullable = false, referencedColumnName = "Id")
    @ManyToOne
    private Tag tag;

    public SubstanceTabCategoryItemTag(){}
    public SubstanceTabCategoryItemTag(SubstanceTabCategoryItem substanceTabCategoryItem, Tag tag) { this.substanceTabCategoryItem = substanceTabCategoryItem; this.tag = tag; }

    public Tag getTag() {return tag;}
    public void setTag(Tag tag) {this.tag = tag;}

    public SubstanceTabCategoryItem getSubstanceTabCategoryItem() {
        return substanceTabCategoryItem;
    }
}
