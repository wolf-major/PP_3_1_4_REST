package ru.kata.spring.boot_security.demo.controller;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.model.UserDTO;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminsRESTController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminsRESTController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String getUsersHTML() {
        return "admin's_pages/user_list";
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<List<UserDTO>> getUsers() {
        List<User> users = userService.getUsers();
        List<UserDTO> usersDTO = users.stream()
                .map(userService::setDataToUser)
                .toList();
        return ResponseEntity.ok(usersDTO);
    }

    @GetMapping(value = "/info", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserDTO> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        System.out.println(userDetails.getUsername());
        User user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(userService.setDataToUser(user));
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<UserDTO> getUserById(@PathVariable Long id) {
        User user = userService.getUser(id);
        return ResponseEntity.ok(userService.setDataToUser(user));
    }

    @PostMapping(value = "/save_edit")
    @ResponseBody
    public ResponseEntity<Map<String, String>> editUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.convertDataFromUserDTO(userDTO);
            userService.updateUser(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Данные пользователя успешно обновлены!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Ошибка при сохранении данных пользователя: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping(value = "/save")
    @ResponseBody
    public ResponseEntity<Map<String, String>> saveUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.convertDataFromUserDTO(userDTO);
            userService.saveUser(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Пользователь успешно добавлен!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Ошибка при сохранении данных пользователя: " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }

    @PostMapping(value = "/delete")
    @ResponseBody
    public ResponseEntity<Map<String, String>> deleteUser(@RequestBody UserDTO userDTO) {
        try {
            User user = userService.convertDataFromUserDTO(userDTO);
            userService.deleteUser(user);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Пользователь успешно удален!");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Ошибка при удалении пользователя " + e.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
