package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes) {
        try {
            userService.registerUser(user, confirmPassword);
            redirectAttributes.addFlashAttribute("successMessage", "Registration successful! Please login.");
            return "redirect:/login?registered";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/register";
        }
    }

    @PostMapping("/login")
    public String loginUser(@RequestParam String username,
            @RequestParam String password,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        try {
            User user = userService.loginUser(username, password);
            if (user != null) {
                // Create a session-safe user object without password
                User sessionUser = new User();
                sessionUser.setId(user.getId());
                sessionUser.setUsername(user.getUsername());
                sessionUser.setRole(user.getRole());

                session.setAttribute("user", sessionUser);
                session.setAttribute("isLoggedIn", true);
                return "redirect:/dashboard";
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password");
                return "redirect:/login?error";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/login?error";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    @GetMapping("/error")
    public String showErrorPage(Model model,
            @RequestParam(required = false) String errorMessage,
            @RequestParam(required = false) String errorDetails) {
        model.addAttribute("errorMessage", errorMessage);
        model.addAttribute("errorDetails", errorDetails);
        return "error";
    }
}