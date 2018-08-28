package com.cas.client2.casclient2.security;

import com.cas.client2.casclient2.entity.User;
import com.cas.client2.casclient2.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 */
@Component
public class UserDetailsServiceImpl implements UserDetailsService,AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {
    @Autowired
    private UserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.findByUsername(username);
        return user;
    }

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        String name = token.getName();
        System.out.println("获得的用户名："+name);
        User user = userService.findByUsername(name);
        if (user==null){
            throw new UsernameNotFoundException(name+"不存在");
        }
        return user;
    }
}
