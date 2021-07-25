package com.unipi.adouladiris.medicalstorage.entities.operable;

import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTab;
import com.unipi.adouladiris.medicalstorage.entities.operable.abstractClass.Operable;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "substance")
public class Substance extends Operable {

    // Manages bidirectional relationship with SubstanceTabs.
    @OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "substance")
    private Set<SubstanceTab> substanceTabSet;

    public Substance(){}
    public Substance(String name){ setName(name); }

}
