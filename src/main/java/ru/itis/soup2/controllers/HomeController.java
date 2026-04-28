package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.itis.soup2.security.CustomUserDetails;
import ru.itis.soup2.services.project.ProjectService;
import ru.itis.soup2.services.project.ProjectRequestService;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProjectService projectService;
    private final ProjectRequestService projectRequestService;

    @GetMapping("/")  // ← изменили с /home на /
    public String home(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {

        if (userDetails == null) {
            return "redirect:/login";
        }

        Integer userId = userDetails.getUser().getId();
        String roleName = userDetails.getUser().getRole() != null
                ? userDetails.getUser().getRole().getRoleName()
                : "ROLE_USER";

        model.addAttribute("currentUserRole", roleName);
        model.addAttribute("currentUserId", userId);

        if ("ROLE_ADMIN".equals(roleName)) {
            return "redirect:/admin/users";
        }

        if ("ROLE_MANAGER".equals(roleName)) {
            model.addAttribute("myProjects", projectService.getProjectsByUserId(userId));
            model.addAttribute("pendingRequests", projectRequestService.getPendingRequestsForManager(userId));
            return "home/manager-home";
        }

        // Для разработчика / тестера
        model.addAttribute("myProjects", projectService.getProjectsByUserId(userId));
        model.addAttribute("myRequests", projectRequestService.getMyPendingRequests(userId));
        model.addAttribute("availableProjects", projectService.getAvailableProjectsForUser(userId));

        return "home/user-home";
    }
}