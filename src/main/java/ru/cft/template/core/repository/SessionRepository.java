package ru.cft.template.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cft.template.core.entity.SessionEntity;
import ru.cft.template.core.service.status.SessionStatus;

import java.util.List;
import java.util.Optional;


public interface SessionRepository extends JpaRepository<SessionEntity, Long> {
    Optional<SessionEntity> findByToken(String token);
    Optional<SessionEntity> findByUserId(Long userEntity);
    Optional<SessionEntity> findByUserIdAndStatus(Long userId, SessionStatus sessionStatus);
    List<SessionEntity> findAllByStatus(SessionStatus sessionStatus);
}
