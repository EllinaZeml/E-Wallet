package ru.cft.template.api.controller;
//Контроллер принимает HTTP-запросы, вызывает методы сервиса и отправляет результаты клиенту.
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import ru.cft.template.api.dto.UserDTO;
import ru.cft.template.core.entity.UserEntity;
import ru.cft.template.core.service.UserService;


@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService=userService;
    }

    @PostMapping
    public UserEntity createUser(@RequestBody UserEntity userEntity){
        return userService.createUser(userEntity);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(
            @PathVariable Long id,
            @RequestHeader Long currentId){
        UserEntity user = userService.getUser(id, currentId);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Пользователь не найден с ID: " + id);
        }
        return new UserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getMidName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBirthDate()
        );
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(
            @PathVariable Long id,
            @RequestBody UserEntity userEntity,
            @RequestHeader Long currentId){
        //return userService.updateUser(id, userEntity,currentId);
        UserEntity user = userService.updateUser(id,userEntity,currentId);
        return new UserDTO(
                user.getFirstName(),
                user.getLastName(),
                user.getMidName(),
                user.getEmail(),
                user.getPhoneNumber(),
                user.getBirthDate()
        );
    }

}
