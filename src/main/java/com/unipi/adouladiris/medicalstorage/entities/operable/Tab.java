package com.unipi.adouladiris.medicalstorage.entities.operable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTab;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tab")
public class Tab extends Operable {

    // Manages bidirectional relationship with SubstanceTabs.
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "tab")
    private Set<SubstanceTab> substanceTabSet;

    public Tab(){}
    public Tab(String name){ setName(name); }

    public Set<SubstanceTab> getSubstanceTabSet() { return substanceTabSet; }
    public void setSubstanceTabSet(Set<SubstanceTab> substanceTabSet) { this.substanceTabSet = substanceTabSet; }

}
