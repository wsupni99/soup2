package ru.itis.soup2.controllers;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.itis.soup2.services.core.UserService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/login")
    public String loginPage(@RequestParam(value = "error", required = false) String error,
                            @RequestParam(value = "logout", required = false) String logout,
                            @RequestParam(value = "registered", required = false) String registered,
                            Model model) {
        if (error != null) {
            log.warn("Попытка логина с ошибкой");
            model.addAttribute("error", "Неверный email или пароль");
        }
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли");
        }
        if (registered != null) {
            model.addAttribute("message", "Регистрация прошла успешно! Теперь можно войти.");
        }
        return "login";
    }

    @GetMapping("/register")
    public String registerForm() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String password) {
        log.info("Получен запрос на регистрацию: name={}, email={}", name, email);

        try {
            userService.register(email, name, password);
            log.info("Регистрация успешно завершена для {}", email);
            return "redirect:/login?registered=true";
        } catch (Exception e) {
            log.error("Ошибка при регистрации пользователя {}", email, e);
            return "redirect:/register?error=true";
        }
    }
}