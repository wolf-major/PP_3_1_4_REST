package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.util.Map;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminsController {
    private final UserService userService;

    @Autowired
    public AdminsController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users")
    public String getUsers(Model model) {
        model.addAttribute("title", "Список пользователей:");
        model.addAttribute("user_list", userService.getUsers());
        return "user_list";
    }

    @GetMapping(value = "/add_user")
    public String createNewUserForm(Map<String, Object> model) {
        User user = new User();
        model.put("newUser", user);
        return "new_user";
    }

    @PostMapping(value = "/save")
    public String saveUser(@ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "redirect:/users";
    }

/*    @GetMapping(value = "/edit")
    public ModelAndView editUserForm(@RequestParam(value = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit_user");
        User user = userService.getUser(id);
        mav.addObject("user", user);
        return mav;
    }*/

    @PostMapping(value = "/save_edit")
    public String saveEditUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/users";
    }

/*    @GetMapping(value = "/delete")
    public String deleteUser(@RequestParam(value = "id") Long id) {
        userService.deleteUser(userService.getUser(id));
        return "redirect:/users";
    }*/
}
