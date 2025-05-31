package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.example.demo.model.User;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(HttpSession session) {
        return "index";
    }

    @GetMapping("/login")
    public String login(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model, HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/dashboard";
        }
        model.addAttribute("user", new User());
        return "register";
    }

    @GetMapping("/dashboard")
    public String dashboard(HttpSession session) {
        System.out.println("Accessing /dashboard");
        System.out.println("Session ID: " + session.getId());
        System.out.println("User in session: " + session.getAttribute("user"));
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        return "dashboard";
    }
} 