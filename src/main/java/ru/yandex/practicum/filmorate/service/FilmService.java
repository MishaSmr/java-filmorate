package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmDbStorage;

import java.util.List;

@Service
@Slf4j
public class FilmService {

    @Autowired
    private final FilmDbStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;

    public FilmService(FilmDbStorage filmStorage, JdbcTemplate jdbcTemplate) {
        this.filmStorage = filmStorage;
        this.jdbcTemplate = jdbcTemplate;
    }

    public void putLike(Long id, Long userId) {
        String sql = "insert into like_user(film_id, user_id ) " +
                "values (?, ?)";
        jdbcTemplate.update(sql, id, userId);
        String sqlRate = "update film set rate = rate + 1 where id = ?";
        jdbcTemplate.update(sqlRate, id);
        log.debug("Лайк фильму c id {} от пользователя с id {}", id, userId);
    }

    public void removeLike(Long id, Long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from fr_user where id = ?", userId);
        if (!userRows.next()) {
            log.warn("Пользователь c таким id не найден.");
            throw new UserNotFoundException("Пользователь c таким id не найден.");
        }
        String sql = "delete from like_user where film_id = ? and user_id = ?";
        jdbcTemplate.update(sql, id, userId);
        String sqlRate = "update film set rate = rate - 1 where id = ?";
        jdbcTemplate.update(sqlRate, id);
        log.debug("Лайк фильму c id {} от пользователя с id {} удален", id, userId);
    }

    public List<Film> getTop(int count) {
        String sql = "select * from film order by rate desc limit ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> filmStorage.makeFilm(rs), count);
    }
}
