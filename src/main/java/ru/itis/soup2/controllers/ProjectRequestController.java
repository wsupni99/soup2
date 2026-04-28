package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itis.soup2.security.CustomUserDetails;
import ru.itis.soup2.services.project.ProjectRequestService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/project-requests")
public class ProjectRequestController {

    private final ProjectRequestService projectRequestService;

    @PostMapping("/request")
    public ResponseEntity<?> createRequest(@RequestParam Integer projectId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            projectRequestService.createRequest(projectId, userDetails.getUser().getId());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{requestId}/approve")
    public ResponseEntity<?> approveRequest(@PathVariable Long requestId,
                                            @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            projectRequestService.approveRequest(requestId, userDetails.getUser().getId());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<?> rejectRequest(@PathVariable Long requestId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        try {
            projectRequestService.rejectRequest(requestId, userDetails.getUser().getId());
            return ResponseEntity.ok().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}