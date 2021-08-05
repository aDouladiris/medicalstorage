package com.unipi.adouladiris.medicalstorage.entities.users;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.unipi.adouladiris.medicalstorage.database.dao.select.Select;
import com.unipi.adouladiris.medicalstorage.entities.operable.Substance;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.Example;
import io.swagger.annotations.ExampleProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import springfox.documentation.annotations.ApiIgnore;

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

    @Transient
    private String[] authorities;

    public User() {}
    public User(String username, String password, Role role) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.enabled = 'Y';

        this.authorities[0] = role.getAuthority();
    }

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        Role newRoleObject = new Role(role);
        this.role = newRoleObject;
        this.enabled = 'Y';

        this.authorities[0] = role;
    }

    // TODO needs review
    public User(org.springframework.security.core.userdetails.User user){
        User userCustom = new Select().findUser(user.getUsername()).getResult(User.class);
        this.username = userCustom.getUsername();
        this.password = userCustom.getPassword();
        this.role = userCustom.getRole();
        this.enabled = 'Y';

        this.authorities[0] = role.getAuthority();
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
