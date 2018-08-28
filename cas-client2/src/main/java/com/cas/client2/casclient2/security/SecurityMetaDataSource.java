package com.cas.client2.casclient2.security;

import com.cas.client2.casclient2.entity.Resource;
import com.cas.client2.casclient2.entity.User;
import com.cas.client2.casclient2.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@SuppressWarnings("ALL")
@Component
public class SecurityMetaDataSource implements FilterInvocationSecurityMetadataSource {

    @Autowired
    private ResourceService resourceService;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private LinkedHashMap<String,Collection<ConfigAttribute>> metaData;
    @PostConstruct
    private void loadSecurityMetaData(){
        System.out.println("正在初始化元数据");
        List<Resource> list = resourceService.getAll();
        metaData=new LinkedHashMap<>();
        for (Resource resource:list){
            List<ConfigAttribute> attributes=new ArrayList<>();
            attributes.add(new SecurityConfig(resource.getResCode()));
            metaData.put(resource.getUrl(),attributes);
        }
        List<ConfigAttribute> base=new ArrayList<>();
        base.add(new SecurityConfig("AUTH_0"));
        metaData.put("/**",base);
    }

    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {


        FilterInvocation invocation= (FilterInvocation) object;
        HttpServletRequest httpRequest = invocation.getHttpRequest();
        HttpSession session = httpRequest.getSession();
        if (metaData==null){
            return new ArrayList<>(0);
        }
        String requestUrl = invocation.getRequestUrl();
        System.out.println("请求Url："+requestUrl);
        Iterator<Map.Entry<String, Collection<ConfigAttribute>>> iterator = metaData.entrySet().iterator();
        Collection<ConfigAttribute> rs=new ArrayList<>();
        while (iterator.hasNext()){
            Map.Entry<String, Collection<ConfigAttribute>> next = iterator.next();
            String url = next.getKey();
            Collection<ConfigAttribute> value = next.getValue();
            RequestMatcher requestMatcher=new AntPathRequestMatcher(url);
            if (requestMatcher.matches(invocation.getRequest())){
                rs = value;
                break;
            }
        }
        System.out.println("拦截认证权限为："+rs);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println(principal);
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getAuthorities());
        return rs;
    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        System.out.println("invoke getAllConfigAttributes ");
        loadSecurityMetaData();
        System.out.println("初始化元数据");
        Collection<Collection<ConfigAttribute>> values = metaData.values();
        Collection<ConfigAttribute> all=new ArrayList<>();
        for (Collection<ConfigAttribute> each:values){
            each.forEach(configAttribute -> {
                all.add(configAttribute);
            });
        }
        return all;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }
}
