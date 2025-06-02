package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.service.AdminService;
import com.example.demo.service.dto.EmployeeDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/employees")
    public String showEmployees(Model model) {
        model.addAttribute("page", "employee");
        model.addAttribute("employees", adminService.getAllEmployees());
        model.addAttribute("employeeDto", new EmployeeDto());
        return "dashboard";
    }

    @PostMapping("/add-employee")
    public String addEmployee(@ModelAttribute("employeeDto") EmployeeDto employeeDto, 
                            RedirectAttributes redirectAttributes) {
        String result = adminService.addEmployee(employeeDto);
        if (result.equals("success")) {
            redirectAttributes.addFlashAttribute("successMessage", "Employee added successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/dashboard/admin/employee";
    }
} 