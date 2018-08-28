package com.cas.client1;

import com.cas.client1.bean.AppCtx;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;

import javax.annotation.PostConstruct;
import javax.swing.*;
import java.io.IOException;

/**
 * @author Administrator
 */
@SpringBootApplication
public class CasClient1Application {


    public static void main(String[] args) {
        SpringApplication.run(CasClient1Application.class, args);
    }

    @Bean
    public OpenEntityManagerInViewFilter openEntityManagerInViewFilter(){
        return new OpenEntityManagerInViewFilter();
    }
}
