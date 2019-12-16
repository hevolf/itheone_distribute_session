package com.enjoy.controller;

import com.enjoy.utils.LoginFilter;
import com.enjoy.utils.UserForm;
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


@Controller
public class IndexController {

    @GetMapping("/toLogin")
    public String toLogin(Model model,HttpServletRequest request) {

        System.out.println("步骤3");
        request.getSession();
        UserForm user = new UserForm();
        user.setUsername("Peter");
        user.setPassword("Peter");
        model.addAttribute("user", user);

        return "login";
    }

    @PostMapping("/login")
    public void login(@ModelAttribute UserForm user,HttpServletRequest request,HttpServletResponse response) throws IOException, ServletException {
        request.getSession().setAttribute(LoginFilter.USER_INFO,user);
        //重定向，回原url
        response.sendRedirect("/index");
    }

    @GetMapping("/index")
    public ModelAndView index(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView();

        modelAndView.setViewName("index");
        modelAndView.addObject("user", request.getSession().getAttribute(LoginFilter.USER_INFO));

        request.getSession().setAttribute("test","123");
        return modelAndView;
    }
}
