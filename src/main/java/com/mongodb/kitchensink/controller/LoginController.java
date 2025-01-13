package com.mongodb.kitchensink.controller;

import com.mongodb.kitchensink.model.User;
import com.mongodb.kitchensink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;


    @GetMapping("/login")
    public String login(Model model) {
        ModelAndView modelAndView = new ModelAndView("login");
        return "login"; // Point to a Thymeleaf template named `login.html`
    }

    @PostMapping("/registerUser")
    public String registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return "User registered successfully!";
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    public class PageController {
        @GetMapping("/successPage")
        public String successPage() {
            return "success"; // Name of your success page template (e.g., success.html)
        }
    }
}
