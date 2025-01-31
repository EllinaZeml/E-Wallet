package ru.cft.template.core.service;

import ru.cft.template.core.entity.SessionEntity;

import java.util.Optional;

public interface SessionService {
    SessionEntity getSessionInfo(Long sessionId);
    SessionEntity getSessionInfoByToken(String token);
    SessionEntity createSession(Long userId);
    SessionEntity logoutByToken(String token);
    SessionEntity logoutByUser(Long userId);
    void checkSessionExpiry();
    Optional<SessionEntity> getSessionsByUserId(Long userId);
}
