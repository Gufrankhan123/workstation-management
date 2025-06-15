package com.example.demo.controller;

import com.example.demo.model.Employee;
import com.example.demo.model.Client;
import com.example.demo.model.Project;
import com.example.demo.service.AdminService;
import com.example.demo.service.dto.EmployeeDto;
import com.example.demo.service.dto.ClientDto;
import com.example.demo.service.dto.ProjectDto;
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

    @GetMapping("/clients")
    public String showClients(Model model) {
        model.addAttribute("page", "client");
        model.addAttribute("clients", adminService.getAllClients());
        model.addAttribute("clientDto", new ClientDto());
        return "dashboard";
    }

    @GetMapping("/projects")
    public String showProjects(Model model) {
        model.addAttribute("page", "project");
        model.addAttribute("projects", adminService.getAllProjects());
        model.addAttribute("clients", adminService.getAllClients());
        model.addAttribute("employees", adminService.getAllEmployees());
        model.addAttribute("projectDto", new ProjectDto());
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

    @PostMapping("/add-client")
    public String addClient(@ModelAttribute("clientDto") ClientDto clientDto,
                          RedirectAttributes redirectAttributes) {
        String result = adminService.addClient(clientDto);
        if (result.equals("success")) {
            redirectAttributes.addFlashAttribute("successMessage", "Client added successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/dashboard/admin/client";
    }

    @PostMapping("/add-project")
    public String addProject(@ModelAttribute("projectDto") ProjectDto projectDto,
                           RedirectAttributes redirectAttributes) {
        String result = adminService.addProject(projectDto);
        if (result.equals("success")) {
            redirectAttributes.addFlashAttribute("successMessage", "Project added successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/dashboard/admin/project";
    }
} 