package ru.yandex.practicum.filmorate.service;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FriendDao;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private final FriendDao friendDao;

    public UserService(FriendDao friendDao) {
        this.friendDao = friendDao;
    }


    public void addFriend(Long id, Long friendId) {
        friendDao.addFriend(id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        friendDao.removeFriend(id, friendId);
    }

    public List<User> getFriends(Long id) {
        return friendDao.getFriends(id);
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> friends = friendDao.getFriends(id);
        return friends.stream()
                .filter(friendDao.getFriends(otherId)::contains)
                .collect(Collectors.toList());
    }
}
