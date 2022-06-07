package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserValidator;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

@Component("inMemoryUserStorage")
@Slf4j
public class InMemoryUserStorage implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();
    private long id = 1;
    private final UserValidator validator = new UserValidator();

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User get(Long id) {
        if (!users.containsKey(id)) {
            log.warn("Пользователь c таким id не найден.");
            throw new UserNotFoundException("Пользователь c таким id не найден.");
        }
        return users.get(id);
    }

    @Override
    public User create(User user) {
        validator.validateUser(user);
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        user.setId(id++);
       //user.setFriends(new HashSet<>());
        users.put(user.getId(), user);
        log.debug("Текущее количество пользователей: {}", users.size());
        return user;
    }

    @Override
    public void remove(User user) {
        if (!users.containsKey(user.getId())) {
            log.warn("Пользователь не найден.");
            throw new UserNotFoundException("Пользователь не найден.");
        }
        users.remove(user.getId());
    }

    @Override
    public User update(User user) {
        validator.validateUser(user);
        User oldUser = users.get(user.getId());
       // user.setFriends(oldUser.getFriends());
        users.put(user.getId(), user);
        log.debug("Текущее количество пользователей: {}", users.size());
        return user;
    }

    public HashMap<Long, User> getUsers() {
        return users;
    }

    public UserValidator getValidator() {
        return validator;
    }
}
