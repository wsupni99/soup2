package ru.itis.soup2.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_VIEW_PREFIX = "error/";

    @ExceptionHandler(EntityNotFoundException.class)
    public Object handleNotFound(EntityNotFoundException ex, HttpServletRequest request) {
        return handleException(HttpStatus.NOT_FOUND, ex.getMessage(), request);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public Object handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        return handleException(HttpStatus.FORBIDDEN, "Доступ запрещён", request);
    }

    @ExceptionHandler(IllegalStateException.class)
    public Object handleIllegalState(IllegalStateException ex, HttpServletRequest request) {
        return handleException(HttpStatus.BAD_REQUEST, ex.getMessage(), request);
    }

    @ExceptionHandler(Exception.class)
    public Object handleGeneral(Exception ex, HttpServletRequest request) {
        return handleException(HttpStatus.INTERNAL_SERVER_ERROR, "Внутренняя ошибка сервера", request);
    }

    private Object handleException(HttpStatus status, String message, HttpServletRequest request) {
        if (isAjax(request)) {
            Map<String, Object> body = new HashMap<>();
            body.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")));
            body.put("status", status.value());
            body.put("error", status.getReasonPhrase());
            body.put("message", message);
            body.put("path", request.getRequestURI());
            return ResponseEntity.status(status).body(body);
        } else {
            ModelAndView mav = new ModelAndView();
            mav.setStatus(status);
            mav.setViewName(ERROR_VIEW_PREFIX + status.value());
            mav.addObject("_errorMessage", message);
            return mav;
        }
    }

    private boolean isAjax(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        return "XMLHttpRequest".equals(requestedWith);
    }
}