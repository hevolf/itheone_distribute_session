package com.enjoy.config;

import com.enjoy.utils.SSOFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Created by Peter on 2018/8/15.
 */
@Configuration
public class SSOConfig {

    //配置filter生效
    @Bean
    public FilterRegistrationBean ssoFilterRegistration(RedisTemplate redisTemplate) {
        FilterRegistrationBean registration = new FilterRegistrationBean();
        registration.setFilter(new SSOFilter(redisTemplate));
        registration.addUrlPatterns("/*");
        registration.addInitParameter("paramName", "paramValue");
        registration.setName("sessionFilter");
        registration.setOrder(1);
        return registration;
    }

}
