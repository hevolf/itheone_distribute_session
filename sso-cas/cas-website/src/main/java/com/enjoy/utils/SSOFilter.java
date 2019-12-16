package com.enjoy.utils;

import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SSOFilter implements Filter {
    private RedisTemplate redisTemplate;

    public static final String USER_INFO = "user";

    public SSOFilter(RedisTemplate redisTemplate){
        this.redisTemplate = redisTemplate;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;

        // 从本地session中
        Object userInfo = request.getSession().getAttribute(USER_INFO);;

        //如果未登陆，则拒绝请求，转向登陆页面
        String requestUrl = request.getServletPath();
        if (!"/toLogin".equals(requestUrl)//不是登陆页面
                && !requestUrl.startsWith("/login")//不是去登陆
                && null == userInfo) {//不是登陆状态

            String ticket = request.getParameter("ticket");
            //有票据,则使用票据去尝试拿取用户信息
            if (null != ticket){
                userInfo = redisTemplate.opsForValue().get(ticket);
            }
            //无法得到用户信息，则去登陆页面
            // request.getRequestURL().toString() 登录中心回调地址（即用户访问的当前地址）
            if (null == userInfo){
                response.sendRedirect("http://cas.com:8080/toLogin?url="+request.getRequestURL().toString());
                // 下次其他回调url时重新进filter逻辑，此处直接返回
                return ;
            }

            /**
             * 将用户信息，加载进session中
             */
            request.getSession().setAttribute(SSOFilter.USER_INFO,userInfo);
            // 使ticket单次有效
            redisTemplate.delete(ticket);
        }
        System.out.println("===========path=======================");
        System.out.println(request.getContextPath()); // 上下文环境context
        System.out.println(request.getServletPath()); // servlet：匹配的controller路径
        System.out.println(request.getRequestURL());  // 访问的完整路径包含host和ip
        System.out.println(request.getRequestURI());  // =context+servlet???
        System.out.println("===========path=======================");

        filterChain.doFilter(request,servletResponse);
    }

    @Override
    public void destroy() {

    }

}
