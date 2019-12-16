package com.enjoy.session;

import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Peter on 2018/8/15.
 */
public class SessionFilter implements Filter {
    // 用户对象在session中的key
    public static final String USER_INFO = "user";

    private RedisTemplate redisTemplate;

    public void setRedisTemplate(RedisTemplate redisTemplate) {
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
        //包装request对象
        MyRequestWrapper myRequestWrapper = new MyRequestWrapper(request,redisTemplate);

//        System.out.println("代码1");
        //如果未登陆，则拒绝请求，转向登陆页面
        String requestUrl = request.getServletPath();

        // 校验是否已登录
        // /toLogin :去登录页面/toLogin的controller返回的是login页面
        if (!"/toLogin".equals(requestUrl)//不是登陆页面
                && !requestUrl.startsWith("/login")//不是去登陆
                // 判断封装的session中是否有 用户对象
                && !myRequestWrapper.isLogin()) {//不是登陆状态

            // 将封装的request：myRequestWrapper和response重定向到 login登录输入页（/toLogin）
            request.getRequestDispatcher("/toLogin").forward(myRequestWrapper,response);
            return ;
        }

        System.out.println("代码1");


        // 其他业务流程
        try {
            filterChain.doFilter(myRequestWrapper,servletResponse);
        } finally {
            System.out.println("代码2");
            //提交session到redis
            myRequestWrapper.commitSession();
        }
    }

    @Override
    public void destroy() {

    }
}
