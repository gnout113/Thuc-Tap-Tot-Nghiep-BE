package com.lamvinhtuong.thuctaptotnghiep;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import service.AppUserService;
import service.EmployeeService;
import service.RoleService;

@SpringBootApplication(scanBasePackages = {"controllers", "service", "security"})
@EntityScan(basePackages = {"models"})
@EnableJpaRepositories("repository")
public class ThuctaptotnghiepApplication {

    public static void main(String[] args) {
        SpringApplication.run(ThuctaptotnghiepApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner run(RoleService roleService, EmployeeService employeeService, AppUserService appUserService) {
        return args -> {
//			roleService.addNewRole("ROLE_USER");
//			roleService.addNewRole("ROLE_MANAGER");
//			roleService.addNewRole("ROLE_ADMIN");
//			roleService.addNewRole("ROLE_SUPER_ADMIN");

//			LocalDate dob = LocalDate.of(1998, 12, 21);
//			Employee employee = new Employee("Lam","Vinh","Tuong","tuonglam53@gmail.com",dob,1);
//			Role role = roleService.getRoleByName("ROLE_USER");
//			Collection<Role> roles = new ArrayList<>();
//			roles.add(role);
//			appUserService.addNewUser("tuonglv", "1234", employee, roles);
        };
    }
}
