package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;
import java.util.stream.Collectors;

public interface FriendDao {

    public void addFriend(Long id, Long friendId);

    public void removeFriend(Long id, Long friendId);

    public List<User> getFriends(Long id);

}
