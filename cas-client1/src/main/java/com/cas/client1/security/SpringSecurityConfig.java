package com.cas.client1.security;

import com.cas.client1.config.CasProperties;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.validation.Cas20ServiceTicketValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Spring security配置
 * @author youyp
 * @date 2018-8-10
 */
@SuppressWarnings("ALL")
@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private CasProperties casProperties;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private SecurityMetaDataSource securityMetaDataSource;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
        auth.authenticationProvider(casAuthenticationProvider());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**","/css/**","/img/**","/*.ico","/login.html",
                "/error","/login.do");
        //web.ignoring().antMatchers("/js/**","/css/**","/img/**","/*.ico",,"/home");
        //web.ignoring().antMatchers("/**");
//        super.configure(web);

    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        System.out.println("配置Spring security");
        http.formLogin()
                //指定登录页是”/login”
                //.loginPage("/login.html").permitAll()
                //.loginProcessingUrl("/login.do").permitAll()
                //.defaultSuccessUrl("/home",true)
                //.permitAll()
                //登录成功后可使用loginSuccessHandler()存储用户信息，可选。
                //.successHandler(loginSuccessHandler()).permitAll()
                .and()
                .logout().permitAll()
                //退出登录后的默认网址是”/home”
                //.logoutSuccessUrl("/home.html")
                //.permitAll()
                .invalidateHttpSession(true)
                .and()
                //登录后记住用户，下次自动登录,数据库中必须存在名为persistent_logins的表
                .rememberMe()
                .tokenValiditySeconds(1209600)
                .and()
                .csrf().disable()
                //其他所有资源都需要认证，登陆后访问
                .authorizeRequests().anyRequest().fullyAuthenticated();
        http.exceptionHandling().authenticationEntryPoint(casAuthenticationEntryPoint())
                .and()
                .addFilterAt(casAuthenticationFilter(),CasAuthenticationFilter.class)
                .addFilterBefore(casLogoutFilter(),LogoutFilter.class)
                .addFilterBefore(singleSignOutFilter(),CasAuthenticationFilter.class);
        /**
         *  FilterSecurityInterceptor本身属于过滤器，不能在外面定义为@Bean，
         *  如果定义在外面，则这个过滤器会被独立加载到webContext中，导致请求会一直被这个过滤器拦截
         *  加入到Springsecurity的过滤器链中，才会使它完整的生效
         */
        http.addFilterBefore(filterSecurityInterceptor(),FilterSecurityInterceptor.class);
    }

    /**
     * 注意：这里不能加@Bean注解
     * @return
     * @throws Exception
     */
//    @Bean
    public FilterSecurityInterceptor filterSecurityInterceptor() throws Exception {
        FilterSecurityInterceptor filterSecurityInterceptor=new FilterSecurityInterceptor();
        filterSecurityInterceptor.setSecurityMetadataSource(securityMetaDataSource);
        filterSecurityInterceptor.setAuthenticationManager(authenticationManager());
        filterSecurityInterceptor.setAccessDecisionManager(affirmativeBased());
        return filterSecurityInterceptor;
    }

    /**
     * 认证入口
     *  <p>
     *    <b>Note:</b>浏览器访问不可直接填客户端的login请求,若如此则会返回Error页面，无法被此入口拦截
     *  </p>
     * @return
     */
    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint(){
        CasAuthenticationEntryPoint casAuthenticationEntryPoint=new CasAuthenticationEntryPoint();
        casAuthenticationEntryPoint.setLoginUrl(casProperties.getCasServerLoginUrl());
        casAuthenticationEntryPoint.setServiceProperties(serviceProperties());
        return casAuthenticationEntryPoint;
    }

    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties=new ServiceProperties();
        serviceProperties.setService(casProperties.getAppServerUrl()+casProperties.getAppLoginUrl());
        serviceProperties.setAuthenticateAllArtifacts(true);
        return serviceProperties;
    }

    //    @Bean
    public CasAuthenticationFilter casAuthenticationFilter() throws Exception {
        CasAuthenticationFilter casAuthenticationFilter=new CasAuthenticationFilter();
        casAuthenticationFilter.setAuthenticationManager(authenticationManager());
        casAuthenticationFilter.setFilterProcessesUrl(casProperties.getAppLoginUrl());
//        casAuthenticationFilter.setAuthenticationSuccessHandler(
//                new SimpleUrlAuthenticationSuccessHandler("/home.html"));
        return casAuthenticationFilter;
    }

    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(){
        CasAuthenticationProvider casAuthenticationProvider=new CasAuthenticationProvider();
        casAuthenticationProvider.setAuthenticationUserDetailsService(userDetailsService);
//        casAuthenticationProvider.setUserDetailsService(userDetailsService);
        casAuthenticationProvider.setServiceProperties(serviceProperties());
        casAuthenticationProvider.setTicketValidator(cas20ServiceTicketValidator());
        casAuthenticationProvider.setKey("casAuthenticationProviderKey");
        return casAuthenticationProvider;
    }

    @Bean
    public Cas20ServiceTicketValidator cas20ServiceTicketValidator() {
        return new Cas20ServiceTicketValidator(casProperties.getCasServerUrl());
    }

    //    @Bean
    public SingleSignOutFilter singleSignOutFilter(){
        SingleSignOutFilter singleSignOutFilter=new SingleSignOutFilter();
        singleSignOutFilter.setCasServerUrlPrefix(casProperties.getCasServerUrl());
        singleSignOutFilter.setIgnoreInitConfiguration(true);
        return singleSignOutFilter;
    }

    //    @Bean
    public LogoutFilter casLogoutFilter(){
        LogoutFilter logoutFilter = new LogoutFilter(casProperties.getCasServerLogoutUrl(), new SecurityContextLogoutHandler());
        logoutFilter.setFilterProcessesUrl(casProperties.getAppLogoutUrl());
        return logoutFilter;
    }

    /**
     * 重写AuthenticationManager获取的方法并且定义为Bean
     * @return
     * @throws Exception
     */
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        //指定密码加密所使用的加密器为passwordEncoder()
        //需要将密码加密后写入数据库
        //auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        //auth.eraseCredentials(false);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder(4);
    }


    /**
     * 定义决策管理器，这里可直接使用内置的AffirmativeBased选举器，
     * 如果需要，可自定义，继承AbstractAccessDecisionManager，实现decide方法即可
     * @return
     */
    @Bean
    public AccessDecisionManager affirmativeBased(){
        List<AccessDecisionVoter<? extends Object>> voters=new ArrayList<>();
        voters.add(roleVoter());
        System.out.println("正在创建决策管理器");
        return new AffirmativeBased(voters);
    }

    /**
     * 定义选举器
     * @return
     */
    @Bean
    public RoleVoter roleVoter(){
        //这里使用角色选举器
        RoleVoter voter=new RoleVoter();
        System.out.println("正在创建选举器");
        voter.setRolePrefix("AUTH_");
        System.out.println("已将角色选举器的前缀修改为AUTH_");
        return voter;
    }


    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }


}
