package com.whp.config.shrio;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
//import org.crazycake.shiro.RedisCacheManager;
//import org.crazycake.shiro.RedisManager;
//import org.crazycake.shiro.RedisSessionDAO;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.whp.config.jwt.JwtFilter;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : 张吉伟
 * @data : 2018/5/15 14:30
 * @descrpition :
 */
@Configuration
public class ShiroConfiguration {
    @Value("${shiro.state}")
    private boolean ShiroState;

    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean shiroFilter(DefaultWebSecurityManager securityManager) {
        System.out.println("11---------11");
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        if (ShiroState) {
            Map<String, Filter> map = new HashMap<String, Filter>();
            map.put("jwt", new JwtFilter());
            shiroFilterFactoryBean.setFilters(map);
            shiroFilterFactoryBean.setUnauthorizedUrl("/subuser/login");
            Map<String, String> filterMap = new HashMap<String, String>();
            filterMap.put("/FbCurrency/FbCurrencySelect", "anon");
            filterMap.put("/LFbSell/LFbSellSelect", "anon");
            filterMap.put("/FbSocket/*", "anon");
            filterMap.put("/**", "jwt");
            shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        } else {
            Map<String, String> filterMap = new HashMap<String, String>();
            filterMap.put("/**", "anon");
            shiroFilterFactoryBean.setFilterChainDefinitionMap(filterMap);
        }
        return shiroFilterFactoryBean;
    }

    @Bean(name = "securityManager")
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager manager = new DefaultWebSecurityManager();
        manager.setRealm(myRealm1());
//        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
//        DefaultSessionStorageEvaluator defaultSessionStorageEvaluator = new DefaultSessionStorageEvaluator();
//        defaultSessionStorageEvaluator.setSessionStorageEnabled(false);
//        subjectDAO.setSessionStorageEvaluator(defaultSessionStorageEvaluator);
//        manager.setSubjectDAO(subjectDAO);
        // 自定义缓存实现 使用redis
        manager.setCacheManager(cacheManager());
        // 自定义session管理 使用redis
        manager.setSessionManager(SessionManager());

        return manager;
    }

    @Bean
    public MyRealm myRealm1() {
        return new MyRealm();
    }


    /**
     * 配置shiro redisManager
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost("47.75.124.131:6379");
        redisManager.setDatabase(3);
        redisManager.setTimeout(1800);// 配置过期时间
        // redisManager.setTimeout(timeout);
        redisManager.setPassword("1234567a?");
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     *
     * @return
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setRedisManager(redisManager());
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     */
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setRedisManager(redisManager());
        return redisSessionDAO;
    }

    /**
     * shiro session的管理
     */
    public DefaultWebSessionManager SessionManager() {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        sessionManager.setSessionDAO(redisSessionDAO());
        return sessionManager;
    }

}
