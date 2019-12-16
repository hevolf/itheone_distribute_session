package com.enjoy.utils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginFilter implements Filter {
    public static final String USER_INFO = "user";
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

            request.getRequestDispatcher("/toLogin").forward(request,response);
            return ;
        }

        System.out.println("步骤1");
        filterChain.doFilter(request,servletResponse);

        System.out.println("步骤2");
    }

    @Override
    public void destroy() {

    }

}
