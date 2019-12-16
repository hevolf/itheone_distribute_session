package com.enjoy.session;

import com.enjoy.utils.CookieBasedSession;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Peter on 2018/8/15.
 * request包装类
 * 包装 request 和 redisTemplate
 * 1. 生成自定义sessionID
 * 2. 创建自定义session
 * 3. 返回自定义session
 * 4. 判断登录状态：自定义session中是否有用户信息
 */
public class MyRequestWrapper extends HttpServletRequestWrapper {
    private volatile boolean committed = false;
    // 每个request一个uuid
    private String uuid = UUID.randomUUID().toString();


    // 同一个域名下，共享session
    private MySession session;
    private RedisTemplate redisTemplate;

    public MyRequestWrapper(HttpServletRequest request,RedisTemplate redisTemplate) {
        super(request);
        this.redisTemplate = redisTemplate;
    }

    /**
     * 提交session内值到redis
     */
    public void commitSession() {
        // 判断当前session是否提交过，提交过则不提交
        if (committed) {
            return;
        }
        committed = true;

        MySession session = this.getSession();
        if (session != null && null != session.getAttrs()) {
            redisTemplate.opsForHash().putAll(session.getId(),session.getAttrs());
        }
    }

    /**
     * 创建新session
     * @return
     */
    public MySession createSession() {

        // 从页面传来的 --- 获取request中自定义的sessionID
        // 获取sessionID来判断是否需要新建session，
        String sessionId = CookieBasedSession.getRequestedSessionId(this);
        Map<String,Object> attr ;
        if (null != sessionId){
            attr = redisTemplate.opsForHash().entries(sessionId);
        } else {
            System.out.println("create session by rId:"+uuid);
            // ？？？？？？ uuid好像没用上？
            sessionId = UUID.randomUUID().toString();
            attr = new HashMap<>();
        }

        //session成员变量持有
        //第一次访问没有session时，给当前request创建一个自定义session，其中只有ID和空map
        session = new MySession();
        session.setId(sessionId);
        session.setAttrs(attr);

        return session;
    }

    /**
     * 取session
     * @return
     */
    @Override
    public MySession getSession() {
        return this.getSession(true);
    }

    /**
     * 取session
     * @return
     */
    @Override
    public MySession getSession(boolean create) {
        if (null != session){
            return session;
        }
        return this.createSession();
    }

    /**
     * 是否已登陆
     *
     * @return
     */
    public boolean isLogin(){
        // 判断当前session中是否有 用户对象
        Object user = getSession().getAttribute(SessionFilter.USER_INFO);
        return null != user;
    }

}
