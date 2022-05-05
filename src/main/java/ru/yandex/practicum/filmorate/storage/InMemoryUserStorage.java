package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

@Component
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();
    private long id = 1;


    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User getUser(Long id) {
        if (!users.containsKey(id)) {
            log.warn("Пользователь c таким id не найден.");
            throw new UserNotFoundException("Пользователь c таким id не найден.");
        }
        return users.get(id);
    }

    @Override
    public User create(User user) {
        validateUser(user);
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(id++);
        user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.debug("Текущее количество пользователей: {}", users.size());
        return user;
    }

    @Override
    public void update(User user) {
        validateUser(user);
        User oldUser = users.get(user.getId());
        user.setFriends(oldUser.getFriends());
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

    public HashMap<Long, User> getUsers() {
        return users;
    }
}
