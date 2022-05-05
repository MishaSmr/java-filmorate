package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

public interface UserStorage {

    public User getUser(Long id);

    public Collection<User> getAll();

    public User create(User user);

    public void update(User user);

    public HashMap<Long, User> getUsers();
}
