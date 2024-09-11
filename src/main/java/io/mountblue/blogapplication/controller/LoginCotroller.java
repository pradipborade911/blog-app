package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.entity.User;
import io.mountblue.blogapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class LoginCotroller {
    @Autowired
    UserService userService;

    @GetMapping("signin")
    public String showLoginPage(){
        return "loginpage";
    }

    @GetMapping("register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("register")
    @ResponseBody
    public String registerUser(User user, Model model){
        userService.saveUser(user);
        return "registered";
    }}
