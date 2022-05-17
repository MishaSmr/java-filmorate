package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public void addFriend(Long id, Long friendId) {
        User user = userStorage.get(id);
        User friend = userStorage.get(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
        log.debug("Текущее количество друзей пользователя: {} — {}", user.getName(), user.getFriends().size());
    }

    public void removeFriend(Long id, Long friendId) {
        User user = userStorage.get(id);
        User friend = userStorage.get(friendId);
        user.getFriends().remove(friendId);
        friend.getFriends().remove(id);
        log.debug("Текущее количество друзей пользователя: {} — {}", user.getName(), user.getFriends().size());
    }

    public List<User> getFriends(Long id) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.get(id);
        for (long friendId : user.getFriends()) {
            friends.add(userStorage.get(friendId));
        }
        return friends;
    }

    public List<User> getMutualFriends(Long id, Long otherId) {
        List<User> friends = new ArrayList<>();
        User user = userStorage.get(id);
        User otherUser = userStorage.get(otherId);
        Set<Long> mutualFriendsId = user.getFriends().stream()
                .filter(otherUser.getFriends()::contains)
                .collect(Collectors.toSet());
        for (long friendId : mutualFriendsId) {
            friends.add(userStorage.get(friendId));
        }
        return friends;
    }
}
