package com.unipi.adouladiris.medicalstorage.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.operable.Substance;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.security.Principal;
import java.util.Collection;

@Entity
@Table(name="user")
public class User extends UserRole implements UserDetails {

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "enabled", nullable = false, length = 1)
    private char enabled;

    // For bidirectional relationship with Users.
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
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

    // TODO needs review
    public User(org.springframework.security.core.userdetails.User user){
        User userCustom = new Select().findUser(user.getUsername()).getResult(User.class);
        this.username = userCustom.getUsername();
        this.password = userCustom.getPassword();
        this.role = userCustom.getRole();
        this.enabled = 'Y';
    }

    public String getUsername() {return username;}

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        if(this.enabled == 'Y') return true;
        else return false;
    }

    public void setUsername(String username) {this.username = username;}

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_customer"); // TODO ROLE_ prefix
    }

    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}

    public Role getRole() {return role;}
    public void setRole(Role role) {this.role = role;}

    public String getAuthority() {return this.role.getAuthority();}

    public char getEnabled() {return enabled;}
}
