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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.http.ResponseEntity;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.ResponseBody;
import java.time.LocalDate;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping("/employees")
    public String showEmployees(Model model,
                                @RequestParam(required = false) String email,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
                                @RequestParam(required = false, defaultValue = "false") boolean bench,
                                @RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "joiningDate"));

        Page<Employee> employeePage;

        if (email != null && !email.isEmpty()) {
            Employee e = adminService.getEmployeeByEmail(email);
            List<Employee> list = e != null ? List.of(e) : List.of();
            employeePage = new PageImpl<>(list, pageable, list.size());
        } else if (start != null && end != null) {
            List<Employee> list = adminService.getEmployeesByJoiningDateRange(start, end);
            employeePage = new PageImpl<>(list, pageable, list.size());
        } else if (bench) {
            List<Employee> list = adminService.getBenchEmployees();
            employeePage = new PageImpl<>(list, pageable, list.size());
        } else {
            employeePage = adminService.getEmployeePage(pageable);
        }

        model.addAttribute("page", "employee");
        model.addAttribute("employees", employeePage.getContent());
        model.addAttribute("currentPage", employeePage.getNumber());
        model.addAttribute("totalPages", employeePage.getTotalPages());
        model.addAttribute("employeeDto", new EmployeeDto());
        model.addAttribute("projects", adminService.getAllProjects());

        // Preserve filter params for pagination links
        model.addAttribute("filterEmail", email);
        model.addAttribute("filterStart", start);
        model.addAttribute("filterEnd", end);
        model.addAttribute("filterBench", bench);
        model.addAttribute("size", size);

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

    // Update employee
    @PostMapping("/update-employee")
    public String updateEmployee(@ModelAttribute("employeeDto") EmployeeDto dto,
                               @RequestParam String empId,
                               RedirectAttributes redirectAttributes) {
        String result = adminService.updateEmployee(empId, dto);
        if ("success".equals(result)) {
            redirectAttributes.addFlashAttribute("successMessage", "Employee updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/dashboard/admin/employee";
    }

    // Delete employee
    @PostMapping("/delete-employee")
    public String deleteEmployee(@RequestParam String empId,
                               RedirectAttributes redirectAttributes) {
        adminService.deleteEmployee(empId);
        redirectAttributes.addFlashAttribute("successMessage", "Employee deleted successfully!");
        return "redirect:/dashboard/admin/employee";
    }

    // Assign employee to project
    @PostMapping("/assign-employee")
    public String assignEmployee(@RequestParam String empId,
                               @RequestParam String projectId,
                               RedirectAttributes redirectAttributes) {
        String result = adminService.assignEmployeeToProject(empId, projectId);
        if ("success".equals(result)) {
            redirectAttributes.addFlashAttribute("successMessage", "Employee assigned successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/dashboard/admin/employee";
    }

    // Release employee
    @PostMapping("/release-employee")
    public String releaseEmployee(@RequestParam String empId,
                                RedirectAttributes redirectAttributes) {
        String result = adminService.releaseEmployeeFromProject(empId);
        if ("success".equals(result)) {
            redirectAttributes.addFlashAttribute("successMessage", "Employee released successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/dashboard/admin/employee";
    }

    /* ===================== JSON REST ENDPOINTS (Employee) ===================== */

    @GetMapping("/api/employees")
    @ResponseBody
    public Page<Employee> apiEmployees(@RequestParam(defaultValue = "0") int page,
                                       @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "joiningDate"));
        return adminService.getEmployeePage(pageable);
    }

    @GetMapping("/api/employees/{empId}")
    @ResponseBody
    public ResponseEntity<Employee> apiEmployeeById(@PathVariable String empId) {
        Employee emp = adminService.getEmployeeByEmpId(empId);
        return emp != null ? ResponseEntity.ok(emp) : ResponseEntity.notFound().build();
    }

    @GetMapping("/api/employees/search")
    @ResponseBody
    public ResponseEntity<Employee> apiEmployeeByEmail(@RequestParam String email) {
        Employee emp = adminService.getEmployeeByEmail(email);
        return emp != null ? ResponseEntity.ok(emp) : ResponseEntity.notFound().build();
    }

    @GetMapping("/api/employees/bench")
    @ResponseBody
    public List<Employee> apiBenchEmployees() {
        return adminService.getBenchEmployees();
    }

    @GetMapping("/api/employees/joining-range")
    @ResponseBody
    public List<Employee> apiEmployeesByDateRange(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate start,
                                                 @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) java.time.LocalDate end) {
        return adminService.getEmployeesByJoiningDateRange(start, end);
    }

    // Update client
    @PostMapping("/update-client")
    public String updateClient(@ModelAttribute("clientDto") ClientDto dto,
                             @RequestParam String clientId,
                             RedirectAttributes redirectAttributes) {
        String result = adminService.updateClient(clientId, dto);
        if ("success".equals(result)) {
            redirectAttributes.addFlashAttribute("successMessage", "Client updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/dashboard/admin/client";
    }

    // Delete client
    @PostMapping("/delete-client")
    public String deleteClient(@RequestParam String clientId, RedirectAttributes redirectAttributes) {
        adminService.deleteClient(clientId);
        redirectAttributes.addFlashAttribute("successMessage", "Client deleted successfully!");
        return "redirect:/dashboard/admin/client";
    }

    /* REST endpoints for client and project */
    @GetMapping("/api/clients")
    @ResponseBody
    public List<Client> apiClients() { return adminService.getAllClients(); }

    @GetMapping("/api/clients/{clientId}")
    @ResponseBody
    public ResponseEntity<Client> apiClientById(@PathVariable String clientId) {
        Client c = adminService.getClientByClientId(clientId);
        return c != null ? ResponseEntity.ok(c) : ResponseEntity.notFound().build();
    }

    @GetMapping("/api/projects")
    @ResponseBody
    public List<Project> apiProjects() { return adminService.getAllProjects(); }

    @GetMapping("/api/projects/{projectId}")
    @ResponseBody
    public ResponseEntity<Project> apiProjectById(@PathVariable String projectId) {
        Project p = adminService.getProjectByProjectId(projectId);
        return p != null ? ResponseEntity.ok(p) : ResponseEntity.notFound().build();
    }

    @GetMapping("/api/projects/{projectId}/employees")
    @ResponseBody
    public List<Employee> apiProjectEmployees(@PathVariable String projectId) {
        return adminService.getEmployeesByProjectId(projectId);
    }

    @GetMapping("/api/projects/{projectId}/client")
    @ResponseBody
    public ResponseEntity<Client> apiProjectClient(@PathVariable String projectId) {
        Client c = adminService.getClientOfProject(projectId);
        return c != null ? ResponseEntity.ok(c) : ResponseEntity.notFound().build();
    }

    @PostMapping("/update-project")
    public String updateProject(@ModelAttribute("projectDto") ProjectDto dto,
                              @RequestParam String projectId,
                              RedirectAttributes redirectAttributes) {
        String result = adminService.updateProject(projectId, dto);
        if ("success".equals(result)) {
            redirectAttributes.addFlashAttribute("successMessage", "Project updated successfully!");
        } else {
            redirectAttributes.addFlashAttribute("error", result);
        }
        return "redirect:/dashboard/admin/project";
    }

    @PostMapping("/delete-project")
    public String deleteProject(@RequestParam String projectId, RedirectAttributes redirectAttributes) {
        adminService.deleteProject(projectId);
        redirectAttributes.addFlashAttribute("successMessage", "Project deleted successfully!");
        return "redirect:/dashboard/admin/project";
    }
} 