package com.cas.client1;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    public static void main(String[] args) {
        String encode = new BCryptPasswordEncoder().encode("123");
        System.out.println(encode);
    }
}
