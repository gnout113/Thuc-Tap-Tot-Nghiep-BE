package controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.AppUser;
import models.Employee;
import models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import service.AppUserService;
import service.EmployeeService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@RestController
@RequestMapping(path = "/api")
public class EmployeeController {
    @Autowired
    private final EmployeeService employeeService;

    @Autowired
    private final AppUserService appUserService;

    public EmployeeController(EmployeeService employeeService, AppUserService appUserService) {
        this.employeeService = employeeService;
        this.appUserService = appUserService;
    }

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

    @GetMapping(path = "/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodeJWT = verifier.verify(refresh_token);
                String username = decodeJWT.getSubject();
                AppUser appUser = this.appUserService.getUserByUsername(username);
                String access_token = JWT.create()
                        .withSubject(appUser.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURL().toString())
                        .withClaim("roles", appUser.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                response.setHeader("access_token", access_token);
                response.setHeader("refresh_token", refresh_token);
                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", refresh_token);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            } catch (Exception exception) {
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String> error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }

    @GetMapping(path = "/user")
    public List<String> user() {
        List<String> user = new ArrayList<>();
        user.add("Hello user");
        return user;
    }

    @GetMapping(path = "/admin")
    public List<String> admin() {
        List<String> admin = new ArrayList<>();
        admin.add("Hello admin");
        return admin;
    }
}
