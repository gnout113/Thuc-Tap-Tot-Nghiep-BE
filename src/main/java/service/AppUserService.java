package service;

import models.AppUser;
import models.Employee;
import models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.AppUserRepository;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AppUserService implements UserDetailsService {
    @Autowired
    private final AppUserRepository appUserRepository;

    @Autowired
    private final EmployeeService employeeService;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository appUserRepository, EmployeeService employeeService, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.employeeService = employeeService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = this.getUserByUsername(username);
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        appUser.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new org.springframework.security.core.userdetails.User(appUser.getUsername(), appUser.getPassword(), authorities);
    }

    public List<AppUser> getAllUsers() {
        return this.appUserRepository.findAll();
    }

    public AppUser getUserByUsername(String username) {
        Optional<AppUser> appUserOptional = this.appUserRepository.findByUsername(username);
        if (!appUserOptional.isPresent()) {
            throw new UsernameNotFoundException("Username is not existed");
        }
        return this.appUserRepository.findByUsername(username).get();
    }

    public void addNewUser(String username, String password, Employee employee, Collection<Role> roles) {
        Optional<AppUser> appUserOptional = this.appUserRepository.findByUsername(username);
        if (appUserOptional.isPresent()) {
            throw new IllegalStateException("Username is existed");
        }
        AppUser appUser = new AppUser(username, password, roles, employee);
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        this.employeeService.addNewEmployee(employee);
        this.appUserRepository.save(appUser);
    }

    public void deleteUser(String username) {
        Optional<AppUser> appUserOptional = this.appUserRepository.findByUsername(username);
        if (!appUserOptional.isPresent()) {
            throw new IllegalStateException("Username is not existed");
        }
        this.appUserRepository.deleteById(appUserOptional.get().getId());
    }

    public void changePassword(String username, String password) {
        Optional<AppUser> appUserOptional = this.appUserRepository.findByUsername(username);
        if (!appUserOptional.isPresent()) {
            throw new IllegalStateException("Username is not existed");
        }
        appUserOptional.get().setPassword(password);
        this.appUserRepository.save(appUserOptional.get());
    }
}
