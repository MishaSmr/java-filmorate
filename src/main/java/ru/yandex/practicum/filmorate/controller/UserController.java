package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectPathVariableException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;

@RestController
@RequestMapping("/users")
public class UserController {

    private final InMemoryUserStorage userStorage;
    private final UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage userStorage, UserService userService) {
        this.userStorage = userStorage;
        this.userService = userService;
    }

    @GetMapping
    public Collection<User> getAll() {
        return userStorage.getAll();
    }

    @GetMapping("/{id}")
    public User geUser(@PathVariable Long id) {
        if (id == null || id <= 0) {
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
    public void update(@Valid @RequestBody User user) throws ValidationException {
        userStorage.update(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (id == null || id <= 0) {
            throw new IncorrectPathVariableException("id");
        }
        if (friendId == null || friendId <= 0) {
            throw new IncorrectPathVariableException("friendId");
        }
        userService.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable Long id, @PathVariable Long friendId) {
        if (id == null || id <= 0) {
            throw new IncorrectPathVariableException("id");
        }
        if (friendId == null || friendId <= 0) {
            throw new IncorrectPathVariableException("friendId");
        }
        userService.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public Collection<User> getFriends(@PathVariable Long id) {

        if (id == null || id <= 0) {
            throw new IncorrectPathVariableException("id");
        }
        return userService.getFriends(id);
    }
}

