package service;

import models.Employee;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.EmployeeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.apache.coyote.http11.Constants.a;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllEmployees() {
        //return List.of(new Employee("lam","vinh","tuong", "tuonglam53@gmail.com", LocalDate.of(1998, Month.DECEMBER,21)));
        return employeeRepository.findAll();
    }

    public void addNewEmployee(Employee employee) {
        Optional<Employee> employeeOptional = employeeRepository.findEmployeeByEmail(employee.getEmail());
        if (employeeOptional.isPresent()) {
            throw new IllegalStateException("Email is existed");
        }
        employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        boolean isExist = employeeRepository.existsById(id);
        if (isExist) {
            employeeRepository.deleteById(id);
        } else {
            throw new IllegalStateException("Employee is not exist");
        }
    }
    @Transactional
    public void updateEmployee(Long id, String firstName, String middleName, String lastName, String email, LocalDate dob, Integer gender) {
        Pattern p = Pattern.compile("^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$", Pattern.CASE_INSENSITIVE);
        boolean validEmail;
        boolean isExist = employeeRepository.existsById(id);
        Optional<Employee> emailOptional = employeeRepository.findEmployeeByEmail(email);

        if (!isExist) {
            throw new IllegalStateException("Employee is not exist");
        }
        if (emailOptional.isPresent()) {
            throw new IllegalStateException("Email is existed");
        }

        Employee employee = employeeRepository.getById(id);

        if (firstName != null && firstName.length() > 0 && !Objects.equals(employee.getFirstName(), firstName)) {
            employee.setFirstName(firstName);
        }
        if (middleName != null && middleName.length() > 0 && !Objects.equals(employee.getMiddleName(), middleName)) {
            employee.setMiddleName(middleName);
        }
        if (lastName != null && lastName.length() > 0 && !Objects.equals(employee.getLastName(), lastName)) {
            employee.setLastName(lastName);
        }
        if (email != null && email.length() > 0 && !Objects.equals(employee.getEmail(), email)) {
            Matcher m = p.matcher(email);
            validEmail = m.matches();
            if (!validEmail) {
                throw new IllegalStateException("Email is not valid");
            }
            employee.setEmail(email);
        }
        if (dob != null && !employee.getDob().equals(dob)) {
            employee.setDob(dob);
        }
        if (gender != null && !gender.equals(employee.getGender())) {
            employee.setGender(gender);
        }
        employeeRepository.save(employee);
    }
}
