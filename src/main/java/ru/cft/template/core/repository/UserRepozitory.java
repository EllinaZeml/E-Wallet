package ru.cft.template.core.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.cft.template.core.entity.UserEntity;

import java.util.Optional;
import java.util.UUID;

public interface UserRepozitory extends JpaRepository<UserEntity, UUID> {
    Optional<UserEntity> findByPhoneNumber(String phoneNumber);
    Optional<UserEntity> findById(Long id);
}
