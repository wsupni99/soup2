package ru.itis.soup2.controllers;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import ru.itis.soup2.security.CustomUserDetails;

@ControllerAdvice
public class GlobalControllerAdvice {

    @ModelAttribute("currentUser")
    public CustomUserDetails getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return userDetails;
    }
}