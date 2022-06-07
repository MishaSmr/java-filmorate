package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

@Component
@Slf4j
public class UserValidator {

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
