package ru.kata.spring.boot_security.demo.test_users;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Collections;
import java.util.HashSet;

@Component
public class Init {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public Init(UserService userService, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    private void init() {
        if (roleService.findRoleByName("USER") == null) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleService.saveRole(userRole);
            System.out.println("Роль User создана");
        } else {
            System.out.println("РОЛЬ USER НЕ СОЗДАНА!");
        }

        if (roleService.findRoleByName("ADMIN") == null) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleService.saveRole(adminRole);
            System.out.println("Роль ADMIN создана");
        }
        else {
            System.out.println("РОЛЬ ADMIN НЕ СОЗДАНА!");
        }

        if (userService.getUserByEmail("user@example.com") == null) {
            User user = new User();
            user.setFirstName("Elijah");
            user.setLastName("Jackson");
            user.setEmail("user@example.com");
            user.setPassword("00000000");
            user.setPhoneNumber("+1(404) 678-9012");
            user.setAddress("123 Peachtree St, Atlanta, GA 30301, USA");
            user.setRoles(new HashSet<>(Collections.singleton(roleService.findRoleByName("USER"))));

            userService.saveUser(user, Collections.singletonList(1L));
            System.out.println(user.getPassword());
        }

        if (userService.getUserByEmail("admin@example.com") == null) {
            User admin = new User();
            admin.setFirstName("Aiden");
            admin.setLastName("Lewis");
            admin.setEmail("admin@example.com");
            admin.setPassword(("00000000"));
            admin.setPhoneNumber("+1(404) 901-2345");
            admin.setAddress("789 Peachtree St, Atlanta, GA 30301, USA");
            admin.setRoles(new HashSet<>(Collections.singleton(roleService.findRoleByName("ADMIN"))));

            userService.saveUser(admin, Collections.singletonList(2L));
            System.out.println(admin.getPassword());
        }
    }
}
