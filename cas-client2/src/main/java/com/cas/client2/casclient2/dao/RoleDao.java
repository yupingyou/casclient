package com.cas.client2.casclient2.dao;

import com.cas.client2.casclient2.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role,String> {

}
