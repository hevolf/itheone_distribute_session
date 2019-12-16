package com.enjoy.controller;

import com.enjoy.utils.SSOFilter;
import com.enjoy.utils.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


@Controller
public class IndexController {
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();
        Object userInfo = request.getSession().getAttribute(SSOFilter.USER_INFO);

        modelAndView.setViewName("index");
        modelAndView.addObject("user", userInfo);

        request.getSession().setAttribute("test","123");
        return modelAndView;
    }
}
