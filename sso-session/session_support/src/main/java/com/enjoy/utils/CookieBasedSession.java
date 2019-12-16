package com.enjoy.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

// 操作客户端cookie信息
// 1. 获取cookie指定字段id
// 2. 生成cookie返回给客户端（根据request，response）
public class CookieBasedSession{

    // 自定义cookiename，方便筛选，构造Cookie时传入（用来绑定sessionid）
    public static final String COOKIE_NAME_SESSION = "psession";

    public static String getRequestedSessionId(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie == null) {
                continue;
            }

            if (!COOKIE_NAME_SESSION.equalsIgnoreCase(cookie.getName())) {
                continue;
            }

            // 获取指定cookie name的 value（sessionid）
            return cookie.getValue();
        }
        return null;
    }

    // 生成cookie返回给客户端
    public static void onNewSession(HttpServletRequest request,
                             HttpServletResponse response) {
        HttpSession session = request.getSession();
        String sessionId = session.getId();
        // 绑定sessionid，同一个域名下，多个主机间共享session
        // 生成共一个psessionId 给不同主机；不同主机根据此id
        Cookie cookie = new Cookie(COOKIE_NAME_SESSION, sessionId);
        cookie.setHttpOnly(true);
        cookie.setPath(request.getContextPath() + "/");
        cookie.setDomain("dev.com");
        cookie.setMaxAge(Integer.MAX_VALUE);
        // 返回cookie
        response.addCookie(cookie);
    }

}
