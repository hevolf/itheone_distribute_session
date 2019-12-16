package com.enjoy.config;

import com.enjoy.session.SessionFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by Peter on 2018/8/15.
 * sessionFilter：配置过滤器
 * sessionFilterRegistration：配置过滤规则
 */
@Configuration
public class SessionConfig {

    //配置filter生效
    @Bean
    public FilterRegistrationBean sessionFilterRegistration(SessionFilter sessionFilter) {

        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(sessionFilter);
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("sessionFilter");
        registration.setOrder(1);
        return registration;
    }

    //定义过滤器组件sessionFilter
    @Bean
    public SessionFilter sessionFilter(RedisTemplate redisTemplate){
        SessionFilter sessionFilter = new SessionFilter();
        sessionFilter.setRedisTemplate(redisTemplate);
        return sessionFilter;
    }

}
