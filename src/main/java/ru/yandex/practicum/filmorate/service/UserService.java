package ru.yandex.practicum.filmorate.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {

    @Autowired
    private final UserDbStorage userDbStorage;
    private final JdbcTemplate jdbcTemplate;

    public UserService(UserDbStorage userDbStorage, JdbcTemplate jdbcTemplate) {
        this.userDbStorage = userDbStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void addFriend(Long id, Long friendId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from fr_user where id = ?", friendId);
        if (!userRows.next()) {
            log.warn("Пользователь c таким id не найден.");
            throw new UserNotFoundException("Пользователь c таким id не найден.");
        }
        /*делаем дружбу сразу взаимной (status=1), т.к. в тестах постмана пока не предусмотрен вариант с подпиской
        при подписках сначала будем проверять нет ли уже обраной записи с подпиской второго на первого (status=0)
        и если есть, то просто обновляем status
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * from friends where user_id = ? " +
                "and friend_id = ?", friendId, id);
        if (friendRows.next()) {
            String sql = "update friends set status = 1 where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sql, friendId, id);
        }*/
        String sql = "insert into friends(user_id, friend_id, status ) " +
                "values (?, ?, ?)";
        jdbcTemplate.update(sql, id, friendId, 1);
        log.debug("Пользователь {} добавил в друзья пользователя {}", id, friendId);
    }

    public void removeFriend(Long id, Long friendId) {
        /*то же самое пока делаем под тесты
        при подписке будем проверять наличие записи со взаимной дружбой и заменять status на 0
        или создавать обратную запись со статусом 0
        если есть запись только с подпиской - удалим
        SqlRowSet friendRows = jdbcTemplate.queryForRowSet("select * from friends where user_id = ? " +
                "and friend_id = ?", id, friendId);
        if (friendRows.next()) {
            String sql = "delete from friends where where user_id = ? and friend_id = ?";
            jdbcTemplate.update(sql, id, friendId);
            if (friendRows.getInt("status") == 1) { //если была дружба, то вносим запись что теперь 2й подписан на 1го
                String sqlAdd = "insert into friends(user_id, friend_id, status ) " +
                        "values (?, ?, ?)";
                jdbcTemplate.update(sqlAdd, friendId, id, 0);
            }
        } //если запись не найдена, значит есть обратная запись с взаимной дружбой
        String sql = "update friends set status = 1 where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, friendId, id);*/
        String sql = "delete from friends where user_id = ? and friend_id = ?";
        jdbcTemplate.update(sql, id, friendId);
        log.debug("Пользователь {} удалил из друзей пользователя {}", id, friendId);
    }

    public List<User> getFriends(Long id) {
        String sql = "select friend_id as user_friend from friends where user_id = ? and status = 1";
        //пока не проверяем обратную запись
        // + "union select user_id as user_friend from friends where friend_id = ? and status = 1";
        List<Long> friendsId = jdbcTemplate.queryForList(sql, Long.class, id);
        return friendsId.stream()
                .map(userDbStorage::get)
                .collect(Collectors.toList());
    }

    public List<User> getCommonFriends(Long id, Long otherId) {
        List<User> friends = getFriends(id);
        return friends.stream()
                .filter(getFriends(otherId)::contains)
                .collect(Collectors.toList());
    }
}
