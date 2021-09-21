package com.unipi.adouladiris.medicalstorage.entities.operables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTab;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "tab")
public class Tab extends Operable {

    // Manages bidirectional relationship with SubstanceTabs.
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "tab")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Set<SubstanceTab> substanceTabSet;

    public Tab(){}
    public Tab(String name){ setName(name); }

    public Set<SubstanceTab> getSubstanceTabSet() { return substanceTabSet; }
    public void setSubstanceTabSet(Set<SubstanceTab> substanceTabSet) { this.substanceTabSet = substanceTabSet; }

}
