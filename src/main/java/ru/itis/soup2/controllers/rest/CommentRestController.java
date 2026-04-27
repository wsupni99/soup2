package ru.itis.soup2.controllers.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.itis.soup2.dto.project.CommentDto;
import ru.itis.soup2.models.project.Comment;
import ru.itis.soup2.security.CustomUserDetails;
import ru.itis.soup2.services.project.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/tasks/{taskId}/comments")
@RequiredArgsConstructor
public class CommentRestController {
    private final CommentService commentService;

    @GetMapping
    public List<CommentDto> getComments(@PathVariable Integer taskId) {
        return commentService.findByTaskId(taskId)
                .stream()
                .map(CommentDto::from)
                .toList();
    }

    @PostMapping
    public CommentDto addComment(@PathVariable Integer taskId,
                                 @RequestParam String text,
                                 @AuthenticationPrincipal CustomUserDetails userDetails) {
        Comment saved = commentService.createComment(
                taskId, userDetails.getUser().getId(), text.trim());
        return CommentDto.from(saved);
    }
}