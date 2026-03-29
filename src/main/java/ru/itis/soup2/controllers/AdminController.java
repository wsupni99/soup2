package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.itis.soup2.dto.AdminUserCreateDto;
import ru.itis.soup2.dto.AdminUserUpdateDto;
import ru.itis.soup2.dto.TaskLogDto;
import ru.itis.soup2.models.core.User;
import ru.itis.soup2.services.core.RoleService;
import ru.itis.soup2.services.core.UserService;
import ru.itis.soup2.services.project.TaskLogService;

import java.util.List;

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

    @GetMapping("/users/create")
    public String createUserForm(Model model) {
        model.addAttribute("createDto", new AdminUserCreateDto("", "", "", "", ""));
        model.addAttribute("roles", roleService.findAll());
        return "admin/user-create";
    }

    @PostMapping("/users/create")
    public String createUser(@ModelAttribute("createDto") AdminUserCreateDto dto) {
        userService.createUser(dto);
        return "redirect:/admin/users";
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

        model.addAttribute("updateDto", updateDto);
        model.addAttribute("roles", roleService.findAll());
        return "admin/user-edit";
    }

    @PostMapping("/users/update")
    public String updateUser(@ModelAttribute("updateDto") AdminUserUpdateDto dto) {
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