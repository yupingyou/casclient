package com.cas.client2.casclient2.dao;

import com.cas.client2.casclient2.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User,String> {
    @Override
    List<User> findAll();

    List<User> findByUsername(String username);

    /**
     * 根据用户名like查询
     * @param username
     * @return
     */
    List<User> getUserByUsernameContains(String username);

    @Query("from User where id=:id")
    User getUserById(@Param("id") String id);

}
