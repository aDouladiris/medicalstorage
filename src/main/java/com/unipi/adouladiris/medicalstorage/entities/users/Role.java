package com.unipi.adouladiris.medicalstorage.entities.users;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name="role")
public class Role  extends UserRole {

    @Column(name = "authority", nullable = false, unique = true)
    private String authority;

    // Manages bidirectional relationship with Role.
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "role")
    private Set<User> userSet;

    public Role() {}
    public Role(String authority) { this.authority = authority; }

    public String getAuthority() { return authority; }
    public void setAuthority(String authority) { this.authority = authority; }
}
