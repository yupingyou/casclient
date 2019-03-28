package com.security.securitysimple.dao;

import com.security.securitysimple.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Administrator
 */
@Repository
public interface ResourceDao extends JpaRepository<Resource,String> {

    @Query("from Resource order by priority")
    List<Resource> getAllResource();
}
