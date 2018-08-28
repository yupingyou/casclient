package com.cas.client2.casclient2.entity;

import org.springframework.security.core.GrantedAuthority;

public class AuthorityInfo implements GrantedAuthority {

    private String authCode;

    public AuthorityInfo(String authCode) {
        this.authCode = authCode;
    }

    @Override
    public String getAuthority() {
        return authCode;
    }
}
