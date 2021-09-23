package com.unipi.adouladiris.medicalstorage.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name="user")
public class User extends UserRole {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false, length = 1)
    private char enabled;

    // For bidirectional relationship with Users.
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "role_Id", nullable = false, referencedColumnName = "Id")
    @JsonIgnore
    private Role role;

    public User() {}
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = 'Y';
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        Role newRoleObject = new Role(role);
        this.role = newRoleObject;
        this.enabled = 'Y';
    }

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public Role getRole() {return role;}
    public void setRole(Role role) {this.role = role;}

    public String getAuthority() {return this.role.getAuthority();}

    public char getEnabled() {return enabled;}
    public void setEnabled(char enabled) { this.enabled = enabled; }
}
