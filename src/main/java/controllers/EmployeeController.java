package controllers;

import models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import service.EmployeeService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "/api/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping
    public List<Employee> HelloWorld() {
        return this.employeeService.getAllEmployees();
    }

    @PostMapping
    public void AddNewEmployee(@RequestBody Employee employee) {
        this.employeeService.addNewEmployee(employee);
    }

    @DeleteMapping(path = "{studentId}")
    public void DeleteEmployee(@PathVariable("studentId") Long id) {
        this.employeeService.deleteEmployee(id);
    }

    @PutMapping(path = "{studentId}")
    public void updateEmployee(@PathVariable("studentId") Long id,
                               @RequestParam(required = false) String firstName,
                               @RequestParam(required = false) String middleName,
                               @RequestParam(required = false) String lastName,
                               @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dob,
                               @RequestParam(required = false) String email,
                               @RequestParam(required = false) Integer gender) {
        this.employeeService.updateEmployee(id, firstName, middleName, lastName, email, dob, gender);
    }
}
