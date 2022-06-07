package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectPathVariableException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private final UserDbStorage userStorage;
    private final UserService userService;

    public UserController(UserDbStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        if (id == null) {
            throw new IncorrectPathVariableException("id");
        }
        return userStorage.get(id);
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) throws ValidationException {
        return userStorage.create(user);
    }

    @DeleteMapping
    public void remove (@RequestBody User user) {
       userStorage.remove(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) throws ValidationException {
       return userStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (id == null) {
            throw new IncorrectPathVariableException("id");
        }
        if (friendId == null) {
            throw new IncorrectPathVariableException("friendId");
        }
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (id == null) {
            throw new IncorrectPathVariableException("id");
        }
        if (friendId == null) {
            throw new IncorrectPathVariableException("friendId");
        }
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {

        if (id == null) {
            throw new IncorrectPathVariableException("id");
        }
        return userService.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public Collection<User> getCommonFriends(@PathVariable Long id, @PathVariable Long otherId) {
        if (id == null) {
            throw new IncorrectPathVariableException("id");
        }
        if (otherId == null) {
            throw new IncorrectPathVariableException("friendId");
        }
        return userService.getCommonFriends(id, otherId);
    }
}

