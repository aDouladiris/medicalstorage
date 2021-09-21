package com.unipi.adouladiris.medicalstorage.entities.operables;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.unipi.adouladiris.medicalstorage.entities.jointables.SubstanceTab;
import com.unipi.adouladiris.medicalstorage.entities.operables.abstractClass.Operable;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "substance")
public class Substance extends Operable {
    // Manages bidirectional relationship with SubstanceTabs.
    @JsonIgnore
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "substance")
    private Set<SubstanceTab> substanceTabSet;

    public Substance(){}
    public Substance(String name){ setName(name); }
}
