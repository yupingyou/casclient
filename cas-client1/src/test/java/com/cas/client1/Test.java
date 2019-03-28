package com.cas.client1;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {
    public static void main(String[] args) {
        String encode = new BCryptPasswordEncoder().encode("123");
        System.out.println(encode);
    }

    @org.junit.Test
    public void test(){
        try {
            TestA testA=new TestA();
            testA.doSomething(null);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
