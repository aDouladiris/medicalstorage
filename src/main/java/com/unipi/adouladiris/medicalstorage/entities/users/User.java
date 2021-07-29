package com.unipi.adouladiris.medicalstorage.entities.users;

import com.unipi.adouladiris.medicalstorage.entities.operable.Substance;

import javax.persistence.*;

@Entity
@Table(name="user")
public class User extends UserRole {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    // For bidirectional relationship with Users.
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "role_Id", nullable = false, referencedColumnName = "Id")
    private Role role;

    public User() {}
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public Role getRole() {return role;}
    public void setRole(Role role) {this.role = role;}
}
