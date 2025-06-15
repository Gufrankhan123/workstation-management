package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.OtpService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class OtpController {

    @Autowired
    private OtpService otpService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/verify-otp")
    public String showOtpPage(HttpSession session, Model model) {
        Long userId = (Long) session.getAttribute("pendingOtpUserId");
        if (userId == null) {
            return "redirect:/login";
        }
        model.addAttribute("email", session.getAttribute("pendingOtpUsername"));
        return "verify-otp";
    }

    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam String code,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("pendingOtpUserId");
        if (userId == null) {
            return "redirect:/login";
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        try {
            boolean valid = otpService.validateOtp(user, code);
            if (valid) {
                // Create session user object
                User sessionUser = new User();
                sessionUser.setId(user.getId());
                sessionUser.setUsername(user.getUsername());
                sessionUser.setRole(user.getRole());
                session.setAttribute("user", sessionUser);
                session.setAttribute("isLoggedIn", true);
                // cleanup
                session.removeAttribute("pendingOtpUserId");
                session.removeAttribute("pendingOtpUsername");
                session.removeAttribute("pendingOtpRole");
                return "redirect:/dashboard";
            }
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid OTP");
            return "redirect:/verify-otp";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            if (user.isAccountBanned()) {
                session.invalidate();
                return "redirect:/login?banned";
            }
            return "redirect:/verify-otp";
        }
    }

    @PostMapping("/resend-otp")
    public String resendOtp(HttpSession session, RedirectAttributes redirectAttributes) {
        Long userId = (Long) session.getAttribute("pendingOtpUserId");
        if (userId == null) {
            return "redirect:/login";
        }
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            return "redirect:/login";
        }
        if (user.isAccountBanned()) {
            session.invalidate();
            return "redirect:/login?banned";
        }
        otpService.resendOtp(user);
        redirectAttributes.addFlashAttribute("infoMessage", "A new OTP has been sent to your email.");
        return "redirect:/verify-otp";
    }
} 