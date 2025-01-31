package ru.cft.template.core.service;

import ru.cft.template.core.entity.UserEntity;

public interface UserService {
    UserEntity createUser(UserEntity userEntity);
    UserEntity getUser(Long id, Long currentUserId);
    UserEntity updateUser(Long id, UserEntity updatedUserEntity, Long currentUserId);

}
