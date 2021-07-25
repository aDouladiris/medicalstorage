package com.unipi.adouladiris.medicalstorage.entities.jointables;

import com.unipi.adouladiris.medicalstorage.entities.jointables.abstractClass.Joinable;
import com.unipi.adouladiris.medicalstorage.entities.operable.Substance;
import com.unipi.adouladiris.medicalstorage.entities.operable.Tab;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="substance_tab", uniqueConstraints = @UniqueConstraint(columnNames={"substance_Id", "tab_Id"}) )
public class SubstanceTab extends Joinable {

    // For bidirectional relationship with Substances.
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "substance_Id", nullable = false, referencedColumnName = "Id")
    private Substance substance;

    // For bidirectional relationship with Tabs.
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "tab_Id", nullable = false, referencedColumnName = "Id")
    private Tab tab;

    // For bidirectional relationship with SubstanceTabCategories.
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "substanceTab")
    private Set<SubstanceTabCategory> substanceTabCategorySet;

    public SubstanceTab(){}
    public SubstanceTab(Substance substance, Tab tab){ this.substance = substance; this.tab = tab; }

    public Substance getSubstance() { return substance; }
    public Tab getTab() { return tab; }

}
