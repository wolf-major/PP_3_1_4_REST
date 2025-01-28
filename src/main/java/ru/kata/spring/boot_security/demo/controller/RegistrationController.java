package ru.kata.spring.boot_security.demo.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Map;

@Controller
@Valid
public class RegistrationController {
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String getWelcomePage() {
        return "welcome_page";
    }

    @GetMapping(value = "/registration")
    public String getRegistrationPage(Map<String, Object> model) {
        User user = new User();
        model.put("newUser", user);
        return "new_user";
    }

    @PostMapping("/registration")
    public String addNewUser(@ModelAttribute("newUser") @Valid User newUser,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            return "new_user";
        }
        if (!newUser.getPassword().equals(newUser.getPasswordConfirm())) {
            bindingResult.rejectValue("passwordConfirm", "error.newUser", "Пароли не совпадают");
            return "new_user";
        }
        userService.saveUser(newUser);
        return "redirect:/login";
    }
}
