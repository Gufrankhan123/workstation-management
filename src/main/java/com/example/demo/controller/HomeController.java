package com.example.demo.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import com.example.demo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import com.example.demo.service.UserService;
import java.util.List;
import com.example.demo.service.AdminService;
import com.example.demo.service.dto.EmployeeDto;
import com.example.demo.repository.UserRepository;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private UserRepository userRepository;

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
    public String dashboard(HttpSession session, Model model) {
        if (session.getAttribute("user") == null) {
            return "redirect:/login";
        }
        
        User user = (User) session.getAttribute("user");
        String role = user.getRole().toUpperCase();
        
        switch (role) {
            case "ADMIN":
                return "redirect:/dashboard/admin/overview";
            case "EMPLOYEE":
                return "redirect:/dashboard/employee/overview";
            case "CLIENT":
                return "redirect:/dashboard/client/overview";
            default:
                return "redirect:/login";
        }
    }

    @GetMapping("/dashboard/users")
    public String dashboardUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"admin".equals(user.getRole())) {
            return "redirect:/dashboard";
        }
        List<User> users = userService.findAllUsers();
        model.addAttribute("users", users);
        return "dashboard";
    }

    @GetMapping("/dashboard/admin/overview")
    public String adminOverview(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "overview");
        model.addAttribute("employeeCount", adminService.getAllEmployees().size());
        model.addAttribute("clientCount", adminService.getAllClients().size());
        model.addAttribute("projectCount", adminService.getAllProjects().size());
        model.addAttribute("benchCount", adminService.getBenchEmployees().size());
        model.addAttribute("bannedCount", userRepository.findByAccountBannedTrueAndRoleNot("ADMIN").size());
        return "dashboard";
    }

    @GetMapping("/dashboard/admin/employee")
    public String adminEmployee(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "employee");
        model.addAttribute("employees", adminService.getAllEmployees());
        model.addAttribute("projects", adminService.getAllProjects());
        return "dashboard";
    }

    @GetMapping("/dashboard/admin/client")
    public String adminClient(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "client");
        model.addAttribute("clients", adminService.getAllClients());
        model.addAttribute("projects", adminService.getAllProjects());
        model.addAttribute("employees", adminService.getAllEmployees());
        return "dashboard";
    }

    @GetMapping("/dashboard/admin/project")
    public String adminProject(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "project");
        model.addAttribute("projects", adminService.getAllProjects());
        model.addAttribute("clients", adminService.getAllClients());
        model.addAttribute("employees", adminService.getAllEmployees());
        return "dashboard";
    }

    @GetMapping("/dashboard/admin/banned-users")
    public String adminBannedUsers(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"ADMIN".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "banned-users");
        model.addAttribute("bannedUsers", userRepository.findByAccountBannedTrueAndRoleNot("ADMIN"));
        model.addAttribute("activeUsers", userRepository.findActiveNonAdmin());
        return "dashboard";
    }

    @GetMapping("/dashboard/employee/overview")
    public String employeeOverview(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"EMPLOYEE".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "employee-overview");
        return "dashboard";
    }

    @GetMapping("/dashboard/client/overview")
    public String clientOverview(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"CLIENT".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "client-overview");
        return "dashboard";
    }

    @GetMapping("/dashboard/employee/profile")
    public String employeeProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"EMPLOYEE".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "employee-profile");
        com.example.demo.model.Employee emp = adminService.getEmployeeByEmail(user.getUsername());
        model.addAttribute("employee", emp);
        return "dashboard";
    }

    @GetMapping("/dashboard/employee/project")
    public String employeeProject(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"EMPLOYEE".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "employee-project");
        com.example.demo.model.Employee emp = adminService.getEmployeeByEmail(user.getUsername());
        model.addAttribute("employee", emp);
        com.example.demo.model.Project project = null;
        com.example.demo.model.Client client = null;
        if (emp != null && emp.getProjects() != null && !emp.getProjects().isEmpty()) {
            project = emp.getProjects().iterator().next();
            if (project.getClients() != null && !project.getClients().isEmpty()) {
                client = project.getClients().iterator().next();
            }
        }
        model.addAttribute("project", project);
        model.addAttribute("client", client);
        return "dashboard";
    }

    @GetMapping("/dashboard/client/profile")
    public String clientProfile(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"CLIENT".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "client-profile");
        com.example.demo.model.Client client = adminService.getClientByEmail(user.getUsername());
        model.addAttribute("client", client);
        return "dashboard";
    }

    @GetMapping("/dashboard/client/projects")
    public String clientProjects(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"CLIENT".equalsIgnoreCase(user.getRole())) {
            return "redirect:/dashboard";
        }
        model.addAttribute("page", "client-projects");
        com.example.demo.model.Client client = adminService.getClientByEmail(user.getUsername());
        model.addAttribute("client", client);
        return "dashboard";
    }
} 