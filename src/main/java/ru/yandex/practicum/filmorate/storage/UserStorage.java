package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {

    public User get(Long id);

    public Collection<User> getAll();

    public User create(User user);

    public void remove (User user);

    public User update(User user);

}
