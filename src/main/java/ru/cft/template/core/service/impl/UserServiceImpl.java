package ru.cft.template.core.service.impl;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.cft.template.core.entity.UserEntity;
import ru.cft.template.core.repository.UserRepozitory;
import ru.cft.template.core.service.UserService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepozitory userRepozitory;

    public UserServiceImpl(UserRepozitory userRepozitory){
        this.userRepozitory=userRepozitory;
    }

    @Override
    @Transactional
    public UserEntity createUser(UserEntity userEntity){
        userEntity.setCreatedAt(Instant.from(LocalDateTime.now()));
        userEntity.setUpdatedAt(Instant.from(LocalDateTime.now()));
        return userRepozitory.save(userEntity);
    }

    // получение информации о пользователе
    @Override
    public UserEntity getUser(Long id, Long currentUserId) {
        Optional<UserEntity> userOptional = userRepozitory.findById(id);

        if (userOptional.isPresent()) {
            UserEntity userEntity = userOptional.get();

            if (userEntity.getId().equals(currentUserId)) {
                return userEntity;
            } else {
                // Возвращаем только безопасные данные для других пользователей без пароля
                UserEntity safeUserEntity = new UserEntity();
                safeUserEntity.setFirstName(userEntity.getFirstName());
                safeUserEntity.setLastName(userEntity.getLastName());
                safeUserEntity.setMidName(userEntity.getMidName());
                safeUserEntity.setPhoneNumber(userEntity.getPhoneNumber());
                safeUserEntity.setEmail(userEntity.getEmail());
                safeUserEntity.setBirthDate(userEntity.getBirthDate());
                return safeUserEntity;
            }
        } else {
            throw new NoSuchElementException("Пользователь не найден");
        }
    }

   // обновление данных пользователя
    @Override
    @Transactional
    public UserEntity updateUser(Long id, UserEntity updatedUserEntity, Long currentUserId) {
      Optional<UserEntity> userOptional = userRepozitory.findById(id);
      //проверка на совпадение ключей найденного польз с тем, кто сделал запрос
      if (userOptional.isPresent() & userOptional.get().getId().equals(currentUserId)){
          UserEntity userEntity = userOptional.get();
          userEntity.setFirstName(updatedUserEntity.getFirstName());
          userEntity.setLastName(updatedUserEntity.getLastName());
          userEntity.setMidName(updatedUserEntity.getMidName());
          userEntity.setBirthDate(updatedUserEntity.getBirthDate());
          userEntity.setUpdatedAt(Instant.from(LocalDateTime.now()));
          return userRepozitory.save(userEntity);
      }
        throw new NoSuchElementException("Пользователь с заданными ID не найден");
    }


}
