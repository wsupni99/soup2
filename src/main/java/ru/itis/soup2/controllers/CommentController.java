package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.itis.soup2.dto.project.CommentDto;
import ru.itis.soup2.models.project.Comment;
import ru.itis.soup2.security.CustomUserDetails;
import ru.itis.soup2.services.project.CommentService;

@Controller
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/tasks/{taskId}/comments")
    @ResponseBody
    public CommentDto addComment(@PathVariable Integer taskId,
                                 @RequestParam String text,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Comment saved = commentService.createComment(
                taskId,
                userDetails.getUser().getId(),
                text.trim()
        );
        return CommentDto.from(saved);
    }
}