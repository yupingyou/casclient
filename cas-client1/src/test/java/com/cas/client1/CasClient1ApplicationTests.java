package com.cas.client1;

import com.cas.client1.dao.UserDao;
import com.cas.client1.entity.User;
import com.cas.client1.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CasClient1ApplicationTests {

    @Autowired
    UserService userService;

    @Autowired
    UserDao userDao;

    @Test
    public void query() {
        System.out.println("init..............");
        List<User> users = userDao.getUserByUsernameContains("a");
        System.out.println("用户密码："+users.get(0).getPassword());
        User user = userDao.getUserById("1");
        System.out.println("用户名"+user.getUsername());
    }

    @Test
    public void add(){
        User user=new User();
        user.setId("3");
        user.setUsername("gga");
        user.setPassword("123");
        userDao.save(user);
        System.out.println("保存成功");
    }

}
