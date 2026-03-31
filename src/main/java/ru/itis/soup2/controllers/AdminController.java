package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.soup2.dto.core.AdminUserCreateDto;
import ru.itis.soup2.dto.core.AdminUserUpdateDto;
import ru.itis.soup2.dto.project.TaskLogDto;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.services.core.RoleService;
import ru.itis.soup2.services.core.UserService;
import ru.itis.soup2.services.project.TaskLogService;

@Controller
@RequestMapping("/admin")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;
    private final TaskLogService taskLogService;

    @GetMapping("/users")
    public String usersPage(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("userDto", new AdminUserCreateDto("", "", "", "", ""));
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("isEdit", false);
        return "admin/user-form";
    }

    @GetMapping("/users/{id}/edit")
    public String editUserForm(@PathVariable Integer id, Model model) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        AdminUserUpdateDto updateDto = new AdminUserUpdateDto(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getContactInfo(),
                user.getRole() != null ? user.getRole().getRoleName() : null
        );

        model.addAttribute("userDto", updateDto);
        model.addAttribute("roles", roleService.findAll());
        model.addAttribute("isEdit", true);
        return "admin/user-form";
    }

    @PostMapping("/users")
    public String createUser(@ModelAttribute("userDto") AdminUserCreateDto dto) {
        userService.createUser(dto);
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/update")
    public String updateUser(@PathVariable Integer id, @ModelAttribute("userDto") AdminUserUpdateDto dto) {
        userService.updateUser(dto);
        return "redirect:/admin/users";
    }

    @GetMapping("/tasklogs")
    public String taskLogsPage(
            @RequestParam(required = false) Integer userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "50") int size,
            Model model) {

        Pageable pageable = PageRequest.of(page, size);
        Page<TaskLogDto> logs = (userId != null && userId > 0)
                ? taskLogService.findByUserIdOptional(userId, pageable)
                : taskLogService.findAllOrdered(pageable);

        model.addAttribute("logs", logs.getContent());
        model.addAttribute("users", userService.getAllUsers());
        model.addAttribute("selectedUserId", userId);
        model.addAttribute("currentPage", logs.getNumber());
        model.addAttribute("totalPages", logs.getTotalPages());
        model.addAttribute("totalElements", logs.getTotalElements());

        return "admin/tasklogs";
    }
}