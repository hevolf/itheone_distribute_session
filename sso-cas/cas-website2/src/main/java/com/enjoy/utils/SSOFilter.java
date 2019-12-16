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
            if (null == userInfo){
                response.sendRedirect("http://cas.com:8080/toLogin?url="+request.getRequestURL().toString());
                return ;
            }

            /**
             * 将用户信息，加载进session中
             */
            request.getSession().setAttribute(SSOFilter.USER_INFO,userInfo);
        }

        filterChain.doFilter(request,servletResponse);
    }

    @Override
    public void destroy() {

    }

}
