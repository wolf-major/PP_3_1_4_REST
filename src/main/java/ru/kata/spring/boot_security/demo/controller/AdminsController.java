package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
public class AdminsController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminsController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    public void addAttributesToMethods(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("userTitle", user);
        model.addAttribute("newUser", new User());
        model.addAttribute("title", "Список пользователей:");
        model.addAttribute("user_list", userService.getUsers());
        model.addAttribute("isUserRole", userService.isUser(user));
        model.addAttribute("isAdminRole", userService.isAdmin(user));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public String getAdminPage(Model model, Principal principal) {
        User user = userService.getUserByEmail(principal.getName());
        model.addAttribute("user", user);
        return "admin's_pages/admin_start_page";
    }

    @GetMapping(value = "/users")
    public String getUsers(Model model, Principal principal) {
        addAttributesToMethods(model, principal);
        return "admin's_pages/user_list";
    }

    @GetMapping(value = "/add_user")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String createNewUserForm(Model model) {
        model.addAttribute("newUser", new User());
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("byUser", false);
        model.addAttribute("isAdmin", false);
        model.addAttribute("isUser", true);
        return "new_user";
    }

    @PostMapping(value = "/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveUser(@ModelAttribute("newUser") User user,
                           @RequestParam(value = "rolesIds", required = false) List<Long> rolesIds,
                           BindingResult bindingResult,
                           Model model,
                           Principal principal) {
        if (bindingResult.hasErrors()) {
            addAttributesToMethods(model, principal);
            return "/admin's_pages/user_list";
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("errorNewUser", "Пароли не совпадают!");
            addAttributesToMethods(model, principal);
            return "/admin's_pages/user_list";
        }
        userService.saveUser(user, rolesIds);
        return "redirect:/admin/users";
    }

    @GetMapping(value = "/user_page")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView getUserPage(Principal principal) {
        ModelAndView mav = new ModelAndView("/user_page");
        mav.addObject("user", userService.getUserByEmail(principal.getName()));
        mav.addObject("isUserRole", false);
        return mav;
    }

    @GetMapping(value = "/edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ModelAndView editUserForm(@RequestParam(value = "id") Long id) {
        ModelAndView mav = new ModelAndView("/admin's_pages/edit_user");
        User user = userService.getUser(id);

        mav.addObject("existingUser", user);
        mav.addObject("isAdmin", userService.isAdmin(user));
        mav.addObject("isUser", userService.isUser(user));
        mav.addObject("allRoles", roleService.getAllRoles());
        return mav;
    }

    @PostMapping(value = "/save_edit")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String saveEditUser(@ModelAttribute("user") User user,
                               @RequestParam(value = "rolesIds", required = false) List<Long> rolesIds,
                               BindingResult bindingResult, Model model, Principal principal) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Ошибки в форме!");
            return "admin's_pages/user_list";
        }
        if (!user.getPassword().equals(user.getPasswordConfirm())) {
            model.addAttribute("error", "Пароли не совпадают!");
            model.addAttribute("currentUser", user);
            addAttributesToMethods(model, principal);
            return "admin's_pages/user_list";
        }
        roleService.setRolesToUser(user, rolesIds);
        userService.updateUser(user);
        return "redirect:/admin/users";
    }

    @PostMapping(value = "/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String deleteUser(@ModelAttribute("user") User user) {
        userService.deleteUser(user.getId());
        return "redirect:/admin/users";
    }
}
