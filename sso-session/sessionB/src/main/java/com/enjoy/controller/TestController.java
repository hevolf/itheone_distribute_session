package com.enjoy.controller;

import com.enjoy.session.MyRequestWrapper;
import com.enjoy.session.SessionFilter;
import com.enjoy.session.UserForm;
import com.enjoy.utils.CookieBasedSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
public class TestController{

    @GetMapping("/toLogin")
    public String toLogin(Model model, MyRequestWrapper request) {
        UserForm user = new UserForm();
        user.setUsername("James");
        user.setPassword("James");
        model.addAttribute("user", user);

        return "login";
    }

    @PostMapping("/login")
    public void login(@ModelAttribute UserForm user, MyRequestWrapper request, HttpServletResponse response) throws IOException, ServletException {
        request.getSession().setAttribute(SessionFilter.USER_INFO,user);

        //种cookie
        CookieBasedSession.onNewSession(request,response);
        //重定向
        response.sendRedirect("/index");
    }

    @GetMapping("/index")
    public ModelAndView index(MyRequestWrapper request) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("index");
        modelAndView.addObject("user", request.getSession().getAttribute(SessionFilter.USER_INFO));

        return modelAndView;
    }

}
