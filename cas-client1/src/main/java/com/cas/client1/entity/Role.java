package com.cas.client1.entity;

import javax.persistence.*;
import java.util.List;

/**
 * @author Administrator
 */
@Entity
@Table(name = "s_role")
public class Role {
    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "role_name")
    private String roleName;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "s_role_res",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "res_id")
    )
    private List<Resource> resources;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "s_user_role",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> users;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public List<Resource> getResources() {
        return resources;
    }

    public void setResources(List<Resource> resources) {
        this.resources = resources;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
