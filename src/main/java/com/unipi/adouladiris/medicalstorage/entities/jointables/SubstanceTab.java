package com.unipi.adouladiris.medicalstorage.entities.jointables;

import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
import com.unipi.adouladiris.medicalstorage.entities.operables.Substance;
import com.unipi.adouladiris.medicalstorage.entities.operables.Tab;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
// Unique constraints for a pair of fields
@Table(name="substance_tab", uniqueConstraints = @UniqueConstraint(columnNames={"substance_Id", "tab_Id"}) )
public class SubstanceTab extends Joinable {

    // For bidirectional relationship with Substances.
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "substance_Id", nullable = false, referencedColumnName = "Id")
    private Substance substance;

    // For bidirectional relationship with Tabs.
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "tab_Id", nullable = false, referencedColumnName = "Id")
    private Tab tab;

    // For bidirectional relationship with SubstanceTabCategories.
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "substanceTab")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<SubstanceTabCategory> substanceTabCategorySet;

    public SubstanceTab(){}
    public SubstanceTab(Substance substance, Tab tab){ this.substance = substance; this.tab = tab; }

    public Substance getSubstance() { return substance; }
    public Tab getTab() { return tab; }

    public void setSubstance(Substance substance) { this.substance = substance; }
    public void setTab(Tab tab) { this.tab = tab; }

    public Set<SubstanceTabCategory> getSubstanceTabCategorySet() {
        return substanceTabCategorySet;
    }
}
