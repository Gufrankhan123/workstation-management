# Workstation Management System

A lightweight HR-management & client-project portal built with Spring Boot and Thymeleaf.  It lets three user roles—**Admin**, **Employee** and **Client**—log in and manage everyday workflows such as onboarding employees/clients, assigning projects, and tracking bench strength.  A single monolithic service powers HTML pages rendered server-side, while Bootstrap 5 gives the UI a clean, responsive look.  The application ships with sensible defaults (two admin accounts) and demonstrates best-practice layering (controller → service → repository) together with entity-centric data modelling.

---
## Key Features
* Role-based dashboards (Admin / Employee / Client)
* CRUD for Employees, Clients and Projects
* Assignment / release of employees to projects
* Pagination & filtering on employee tables
* Password-visibility toggle, encrypted storage (BCrypt)
* Automatic bootstrap of default admin users
* Clean responsive UI with Bootstrap & FontAwesome

## Getting Started
1. **Prerequisites** – JDK 17+, Maven 3, MySQL/Postgres (or H2).
2. **Clone & build**
   ```bash
   git clone <repo>
   cd demo
   mvn spring-boot:run
   ```
3. **Login** – Navigate to `http://localhost:8080/login` and use:
   * admin / Password@123  → role ADMIN
   * superadmin / Password@123 → role SUPERADMIN

---
## Architecture
```
Spring Boot 3.x (MVC)
└─ Controllers (Web layer)
   └─ Services (Business logic)
      └─ Repositories (Spring Data JPA)
         └─ MySQL (or any JDBC DB)
```
Entities: `User`, `Employee`, `Client`, `Project` with proper relations (`@OneToOne`, `@ManyToMany`).  Thymeleaf fragments compose the UI, and Bootstrap Off-canvas makes the sidebar responsive.

---
## Q & A

### Q1. Explain your project in 100 words
A web-based Workstation Management System that centralises employee, client and project information.  Admins onboard employees/clients, create projects and assign employees.  Employees log in to view their profile, current project and tasks; clients can monitor their projects and associated employees.  The system offers filtering, pagination and default credential seeding, all wrapped in a clean Bootstrap UI and secured passwords.  Built fully with Spring Boot and Thymeleaf, it's designed for quick deployment in SMEs where heavyweight ERP suites are overkill.

### Q2. What is the tech stack of your project?
* Java 17
* Spring Boot 3 (MVC, Spring Data JPA)
* Thymeleaf (server-side templating)
* Bootstrap 5 & FontAwesome (UI)
* MySQL / H2 (relational database)
* Maven (build)

### Q3. What is your role and responsibility or dependency in your project?
I acted as **full-stack developer** responsible for:
* Designing the data model and JPA mappings
* Implementing controllers, services and repositories
* Building Thymeleaf templates & responsive layouts
* Integrating password encryption & default-user seeding
* Writing utility components and deployment documentation

### Q4. Have you implemented the microservice in your project?
No—this is a **monolithic** Spring Boot application.  Given the project size and tight coupling of features, a single deployable JAR offered simpler maintenance and faster delivery.  The code structure, however, is modular enough to be extracted into micro-services in the future.

### Q5. Have you implemented the spring security in your project?
Basic security is covered via **BCrypt-encrypted passwords** and role checks in controllers/session; full Spring Security integration is a planned enhancement.

### Q6. What is the big challenge which you faced in your project?
Balancing a clean, responsive UI with server-side rendering—especially dynamic modals and password toggles—was challenging.  Ensuring modal IDs and accordion targets remained unique while driven by Thymeleaf loops required careful parameterisation.

### Q7. What is the team size?
Solo development (1), with periodic stakeholder feedback from a product owner.

### Q8. What is the design pattern which you have used into your project?
* **Layered Architecture** (Controller-Service-Repository)
* **Builder / Factory helpers** for ID generation
* **MVC pattern** via Spring Boot & Thymeleaf
* **Singleton** beans for services and util components by virtue of Spring's default scope. 