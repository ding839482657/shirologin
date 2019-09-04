package com.gnjf.shirologin.shiro;

import com.gnjf.shirologin.shiro.jwt.JwtFilter;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Shiro配置
 */
//表示这是配置
@Configuration
public class ShiroConfig {

    /**
     * 配置使用自定义Realm，关闭Shiro自带的session
     *
     * UserRealm进行校验
     */
    @Bean("securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager(UserRealm userRealm) {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        // 使用自定义Realm
        manager.setRealm(userRealm);
        // 关闭Shiro自带的session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator evaluator = new DefaultSessionStorageEvaluator();
        evaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(evaluator);
        manager.setSubjectDAO(subjectDAO);
        // 设置自定义Cache缓存
//        defaultWebSecurityManager.setCacheManager(new CustomCacheManager());
        return manager;
    }
    /**
     * 注册shiro的Filter，拦截请求
     */
    @Bean("shiroFilter")
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean factoryBean = new ShiroFilterFactoryBean();
        // 添加自己的过滤器取名为jwt
        //map集合存储filter
        Map<String, Filter> filterMap = new HashMap<>(16);
        filterMap.put("jwt", new JwtFilter());
        factoryBean.setFilters(filterMap);
        factoryBean.setSecurityManager(securityManager);
        // 自定义url规则使用LinkedHashMap有序Map
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>(16);
        // Swagger接口文档
        // filterChainDefinitionMap.put("/v2/api-docs", "anon");
        // filterChainDefinitionMap.put("/webjars/**", "anon");
        // filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        // filterChainDefinitionMap.put("/swagger-ui.html", "anon");
        // filterChainDefinitionMap.put("/doc.html", "anon");

        // 公开接口
        // filterChainDefinitionMap.put("/api/**", "anon");
        // 所有请求通过我们自己的JWTFilter
        //拦截所有的
        map.put("/**", "jwt");
        factoryBean.setFilterChainDefinitionMap(map);
        return factoryBean;
    }

    /**
     * 下面的代码是添加注解支持
     */
    @Bean
    @DependsOn("lifecycleBeanPostProcessor")
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator creator = new DefaultAdvisorAutoProxyCreator();
        // 强制使用cglib，防止重复代理和可能引起代理出错的问题，https://zhuanlan.zhihu.com/p/29161098
        creator.setProxyTargetClass(true);
        return creator;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(DefaultWebSecurityManager manager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(manager);
        return advisor;
    }
}
