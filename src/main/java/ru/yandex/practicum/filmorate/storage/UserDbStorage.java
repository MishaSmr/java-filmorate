package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.util.UserValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Objects;

@Component("userDbStorage")
@Slf4j
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;
    private final UserValidator validator;

    public UserDbStorage(JdbcTemplate jdbcTemplate, UserValidator validator) {
        this.jdbcTemplate = jdbcTemplate;
        this.validator = validator;
    }

    @Override
    public User get(Long id) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from fr_user where id = ?", id);
        if (userRows.next()) {
            return User.builder().
                    id(userRows.getInt("id"))
                    .email(userRows.getString("email"))
                    .login(userRows.getString("login"))
                    .name(userRows.getString("name"))
                    .birthday(Objects.requireNonNull(userRows.getDate("birthday")).toLocalDate())
                    .build();
        } else {
            log.warn("Пользователь c таким id не найден.");
            throw new UserNotFoundException("Пользователь c таким id не найден.");
        }
    }

    @Override
    public Collection<User> getAll() {
        String sql = "select * from fr_user";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeUser(rs));
    }

    public User makeUser(ResultSet rs) throws SQLException {
        return User.builder().
                id(rs.getInt("id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                .build();
    }

    @Override
    public User create(User user) {
        validator.validateUser(user);
        if (user.getName().isEmpty()) {
            user.setName(user.getLogin());
        }
        String sql = "insert into fr_user (login, name, email, birthday ) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, user.getLogin());
            stmt.setString(2, user.getName());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, (java.sql.Date.valueOf(user.getBirthday())));
            return stmt;
        }, keyHolder);
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
        return user;
    }

    @Override
    public void remove(User user) {
        String sql = "delete from fr_user where id = ?";
        jdbcTemplate.update(sql, user.getId());
    }

    @Override
    public User update(User user) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from fr_user where id = ?", user.getId());
        if (!userRows.next()) {
            log.warn("Пользователь не найден.");
            throw new UserNotFoundException("Пользователь не найден.");
        }
        String sql = "update fr_user set " +
                "login = ?, name = ?, email = ?, birthday = ? " +
                "where id = ?";
        jdbcTemplate.update(sql,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                java.sql.Date.valueOf(user.getBirthday()),
                user.getId());
        return user;
    }
}
