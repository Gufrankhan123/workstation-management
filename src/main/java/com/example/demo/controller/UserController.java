package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.service.UserService;
import com.example.demo.service.OtpService;
import com.example.demo.repository.UserRepository;
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

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

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
            if (user == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "Invalid username or password");
                return "redirect:/login?error";
            }
            if (user.isAccountBanned()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Your account was banned due to suspicious activity. Please contact admin.");
                return "redirect:/login?banned";
            }

            String role = user.getRole() == null ? "" : user.getRole().toUpperCase();
            if (role.equals("CLIENT") || role.equals("EMPLOYEE")) {
                // Generate OTP and redirect to verification page
                otpService.generateOtp(user);
                session.setAttribute("pendingOtpUserId", user.getId());
                session.setAttribute("pendingOtpUsername", user.getUsername());
                session.setAttribute("pendingOtpRole", user.getRole());
                return "redirect:/verify-otp";
            } else {
                // Direct login for ADMIN/SUPERADMIN or other roles
                User sessionUser = new User();
                sessionUser.setId(user.getId());
                sessionUser.setUsername(user.getUsername());
                sessionUser.setRole(user.getRole());
                session.setAttribute("user", sessionUser);
                session.setAttribute("isLoggedIn", true);
                return "redirect:/dashboard";
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