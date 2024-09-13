package io.mountblue.blogapplication.controller;

import io.mountblue.blogapplication.entity.User;
import io.mountblue.blogapplication.exception.EmailAlreadyExistsException;
import io.mountblue.blogapplication.exception.ResourceNotFoundException;
import io.mountblue.blogapplication.exception.UsernameAlreadyExistsException;
import io.mountblue.blogapplication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {
    @Autowired
    UserService userService;

    @GetMapping("signin")
    public String showLoginPage(){
        return "login_page";
    }

    @GetMapping("register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "register_page";
    }

    @PostMapping("register")
    public String registerUser(User user, Model model){
        try {
            userService.saveUser(user);
        } catch (EmailAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register_page";
        } catch (UsernameAlreadyExistsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register_page";
        } catch (ResourceNotFoundException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register_page";
        }
        model.addAttribute("successMessage", "Your account has been created successfully! Please log in to get started.");

        return "login_page";
    }}
