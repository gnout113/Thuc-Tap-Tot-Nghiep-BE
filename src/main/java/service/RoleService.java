package service;

import models.AppUser;
import models.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.RoleRepository;

import java.util.Optional;

@Service
@Transactional
public class RoleService {
    @Autowired
    private final RoleRepository roleRepository;

    @Autowired
    private final AppUserService appUserService;

    public RoleService(RoleRepository roleRepository, AppUserService appUserService) {
        this.roleRepository = roleRepository;
        this.appUserService = appUserService;
    }

    public void addNewRole(String name) {
        Optional<Role> roleOptional = this.roleRepository.findRoleByName(name);
        if (roleOptional.isPresent()) {
            throw new IllegalStateException("Role is existed");
        }
        this.roleRepository.save(new Role(name));
    }

    public void deleteRole(String name) {
        Optional<Role> roleOptional = this.roleRepository.findRoleByName(name);
        if (!roleOptional.isPresent()) {
            throw new IllegalStateException("Role is not existed");
        }
        this.roleRepository.delete(new Role(name));
    }

    public void addRoleToUser(String username, String roleName) {
        Optional<Role> roleOptional = this.roleRepository.findRoleByName(roleName);
        if (!roleOptional.isPresent()) {
            throw new IllegalStateException("Role is not existed");
        }
        AppUser appUser = this.appUserService.getUserByUsername(username);
        appUser.getRoles().add(roleOptional.get());
    }

    public Role getRoleByName(String roleName) {
        Optional<Role> roleOptional = this.roleRepository.findRoleByName(roleName);
        if (!roleOptional.isPresent()) {
            throw new IllegalStateException("Role is not existed");
        }
        return roleOptional.get();
    }
}
