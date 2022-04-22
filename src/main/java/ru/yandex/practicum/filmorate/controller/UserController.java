package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final HashMap<Integer, User> users = new HashMap<>();
    private int Id = 1;

    @GetMapping
    public Collection<User> getAll() {
        return users.values();
    }

    @PostMapping
    public void create(@Valid @RequestBody User user) throws ValidationException {
        validateUser(user);
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(Id++);
        users.put(user.getId(), user);
        log.debug("Текущее количество пользователей: {}", users.size());
    }

    @PutMapping
    public void update(@Valid @RequestBody User user) throws ValidationException {
        validateUser(user);
        users.put(user.getId(), user);
        log.debug("Текущее количество пользователей: {}", users.size());
    }

    public void validateUser(User user) throws ValidationException {
        if (user.getEmail().isEmpty()) {
            log.warn("e-mail не может быть пустой");
            throw new ValidationException("Ошибка валидации");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("e-mail должен содержать знак @");
            throw new ValidationException("Ошибка валидации");
        }
        if (user.getLogin().isEmpty()) {
            log.warn("Логин не может быть пустой");
            throw new ValidationException("Ошибка валидации");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("Логин не может содержать пробелы");
            throw new ValidationException("Ошибка валидации");
        }
        if (checkBirthday(user.getBirthday())) {
            log.warn("Дата рождения не может быть в будущем:)");
            throw new ValidationException("Ошибка валидации");
        }

    }
    private static boolean checkBirthday(LocalDate date) {
        return date.isAfter(LocalDate.now());
    }

}

