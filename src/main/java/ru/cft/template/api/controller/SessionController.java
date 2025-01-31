package ru.cft.template.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.cft.template.api.dto.SessionDTO;
import ru.cft.template.core.entity.SessionEntity;
import ru.cft.template.core.service.SessionService;

@RestController
@RequestMapping("/sessions")
public class SessionController {
    @Autowired
    private final SessionService sessionService;

    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/{userId}")
    public SessionDTO createSession(@PathVariable Long userId){
        SessionEntity session = sessionService.createSession(userId);
        return new SessionDTO(session.getUser().getId(), session.getToken());
    }
    //тут не знаю какой путь дать
    @PostMapping("/token")
    public SessionDTO logoutByToken(@RequestParam String token) {
        SessionEntity session = sessionService.logoutByToken(token);
        return new SessionDTO(session.getUser().getId(), session.getToken());
    }
    @PostMapping("/{userId}")
    public SessionDTO logoutByUser(@PathVariable Long userId) {
        SessionEntity session =sessionService.logoutByUser(userId);
        return new SessionDTO(session.getUser().getId(), session.getToken());
    }

    @GetMapping("/{sessionId}")
    public SessionDTO getSessionInfo(@PathVariable Long sessionId) {
        SessionEntity session = sessionService.getSessionInfo(sessionId);
        if (session == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Сессия не найдена");
        }
        return new SessionDTO(session.getUser().getId(), session.getToken());
    }

    @GetMapping("/token")
    public SessionDTO getSessionInfoByToken(@RequestParam String token) {
        SessionEntity session = sessionService.getSessionInfoByToken(token);
        if (session != null) {
            return new SessionDTO(session.getUser().getId(), session.getToken());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Сессия не найдена");
        }
    }

    @PostMapping
    public String checkSessionExpiry() {
        sessionService.checkSessionExpiry();
        return "Проверка на истечение сессий завершена";
    }
}
