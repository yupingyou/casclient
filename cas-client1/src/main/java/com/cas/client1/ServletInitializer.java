package com.cas.client1;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(CasClient1Application.class);
    }

    @Override
    public void onStartup(ServletContext servletContext)
            throws ServletException {
        System.out.println("ServletInitializer onStartup ............");
        FilterRegistration.Dynamic openEntityManagerInViewFilter = servletContext.addFilter("openEntityManagerInViewFilter", OpenEntityManagerInViewFilter.class);
        openEntityManagerInViewFilter.setInitParameter("entityManagerFactoryBeanName", "entityManagerFactory");
        openEntityManagerInViewFilter.addMappingForUrlPatterns(null, false, "/*");
        super.onStartup(servletContext);
    }

}
