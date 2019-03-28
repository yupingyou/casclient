package com.security.securitysimple.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "s_user")
public class User implements UserDetails {
    @Id
    private String id;
    @Column(name = "username")
    private String username;
    @Column(name = "password")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "s_user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles;

    public User() {
    }

    public User(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Transient
    List<GrantedAuthority> grantedAuthorities=new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (grantedAuthorities.size()==0){
            if (!CollectionUtils.isEmpty(roles)){
                for (Role role:roles){
                    List<Resource> resources = role.getResources();
                    if (!CollectionUtils.isEmpty(resources)){
                        for (Resource resource:resources){
                            grantedAuthorities.add(new SimpleGrantedAuthority(resource.getResCode()));
                        }
                    }
                }
            }
            grantedAuthorities.add(new SimpleGrantedAuthority("AUTH_0"));
        }
        return grantedAuthorities;
    }
    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
