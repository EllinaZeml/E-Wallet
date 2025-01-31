package ru.cft.template.core.service.impl;

import jakarta.transaction.Transactional;
import org.hibernate.SessionException;
import org.springframework.beans.factory.annotation.Autowired;
import ru.cft.template.core.entity.SessionEntity;
import ru.cft.template.core.entity.UserEntity;
import ru.cft.template.core.repository.SessionRepository;
import ru.cft.template.core.repository.UserRepozitory;
import ru.cft.template.core.service.SessionService;
import ru.cft.template.core.service.status.SessionStatus;

import java.time.LocalDateTime;
import java.util.Optional;


public class SessionServiceImpl implements SessionService {
    private static final int SESSION_TIMEOUT_MINUTES = 30;

    @Autowired
    private SessionRepository sessionRepository;
    @Autowired
    private UserRepozitory userRepository;

    //получение информации по id
    @Override
    public SessionEntity getSessionInfo(Long sessionId) {
        Optional<SessionEntity> sessionOpt = sessionRepository.findById(sessionId);
        return sessionOpt.orElse(null);
    }

    //получение информации по токену
    @Override
    public SessionEntity getSessionInfoByToken(String token) {
        Optional<SessionEntity> sessionOpt = sessionRepository.findByToken(token);
        return sessionOpt.orElse(null);
    }


    //создание сессии (вход в ак)
    @Override
    @Transactional
    public SessionEntity createSession(Long userId) {
        Optional<UserEntity> userOpt = userRepository.findById(userId);
        if (!userOpt.isPresent()) {
            throw new SessionException("Пользователь не найден");
        }
        //если сессия у пользователя активна
        UserEntity user = userOpt.get();
        Optional<SessionEntity> sessionOpt = sessionRepository.findByUserIdAndStatus(userId, SessionStatus.ACTIVE);
        if (sessionOpt.isPresent()) {
            throw new SessionException("Пользователь уже в системе");
        }
        SessionEntity session = new SessionEntity();
        session.setUser(user);
        //session.setToken(sessionToken); токен пока не знаю как реализовать
        session.setStatus(SessionStatus.ACTIVE);
        session.setCreatedAt(LocalDateTime.now());
        session.setUpdatedAt(LocalDateTime.now());
        session.setSessionTime(LocalDateTime.now().plusMinutes(SESSION_TIMEOUT_MINUTES));

        sessionRepository.save(session);
        return session;
    }

    //выход из сессии по токену
    @Override
    @Transactional
    public SessionEntity logoutByToken(String token) {
        Optional<SessionEntity> sessionOpt = sessionRepository.findByToken(token);
        if (sessionOpt.isEmpty()) {
            throw new SessionException("Сессия не найдена");
        }
        SessionEntity session = sessionOpt.get();

        if (session.getStatus() != SessionStatus.ACTIVE) {
            throw new SessionException ("Сессия уже завершена или истекла");
        }
        session.setStatus(SessionStatus.INACTIVE);
        session.setUpdatedAt(LocalDateTime.now());
        sessionRepository.save(session);
        return session;

    }

    //выход по юзеру
    @Override
    @Transactional
    public SessionEntity logoutByUser(Long userId){
        Optional<SessionEntity> sessionOpt = sessionRepository.findByUserId(userId);
        if(!sessionOpt.isPresent()){
            throw new SessionException("Пользователь не найден");
        }
        SessionEntity session = sessionOpt.get();

        if(session.getStatus() != SessionStatus.ACTIVE){
            throw new SessionException("Сессия уже завершена или истекла");
        }
        session.setStatus(SessionStatus.INACTIVE);
        session.setUpdatedAt(LocalDateTime.now()); //обновляем время последнего изменения
        sessionRepository.save(session);

        return session;
    }

    //проверка на истечение сессии
    @Override
    public void checkSessionExpiry(){
        LocalDateTime now = LocalDateTime.now();
        sessionRepository.findAllByStatus(SessionStatus.ACTIVE).forEach( sessionEntity ->{
            //время, кгда сессия должна быть завершена
            LocalDateTime sessionExpiryTime = sessionEntity.getCreatedAt().plusMinutes(SESSION_TIMEOUT_MINUTES);
            if(sessionExpiryTime.isBefore(now)){
                sessionEntity.setStatus(SessionStatus.EXPIRED);
                sessionEntity.setUpdatedAt(now);//обнов послед изменение
                sessionRepository.save(sessionEntity);
            }
        } );

    }
    // Получаем все сессии пользователя
    @Override
    public Optional<SessionEntity> getSessionsByUserId(Long userId) {
        return sessionRepository.findByUserId(userId);
    }

}
