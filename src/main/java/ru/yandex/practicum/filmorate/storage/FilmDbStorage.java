package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.util.FilmValidator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component("filmDbStorage")
@Slf4j
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final FilmValidator validator;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, FilmValidator validator) {
        this.jdbcTemplate = jdbcTemplate;
        this.validator = validator;
    }

    @Override
    public Film get(long id) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where id = ?", id);
        if (filmRows.next()) {
            return Film.builder().
                    id(filmRows.getInt("id"))
                    .name(filmRows.getString("name"))
                    .description(filmRows.getString("description"))
                    .duration(filmRows.getInt("duration"))
                    .releaseDate(filmRows.getDate("release_date").toLocalDate())
                    .mpa(makeRating(filmRows.getInt("rating_id")))
                    .rate(filmRows.getInt("rate"))
                    .build();
        } else {
            log.warn("Фильм c таким id не найден.");
            throw new FilmNotFoundException("Фильм c таким id не найден.");
        }
    }

    @Override
    public void remove(Film film) {
        String sql = "delete from film where id = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    @Override
    public Collection<Film> getAll() {
        String sql = "select * from film";

        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFilm(rs));
    }

    public Film makeFilm(ResultSet rs) throws SQLException {
        return Film.builder().
                id(rs.getInt("id"))
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .duration(rs.getInt("duration"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .mpa(makeRating(rs.getInt("rating_id")))
                .rate(rs.getInt("rate"))
                .build();
    }


    @Override
    public Film create(Film film) {
        validator.validateFilm(film);
        isInBase(film);
        String sql = "insert into film(name, release_date, description, duration, rating_id, rate ) " +
                "values (?, ?, ?, ?, ?, ?)";
            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
                stmt.setString(1, film.getName());
                stmt.setDate(2, (java.sql.Date.valueOf(film.getReleaseDate())));
                stmt.setString(3, film.getDescription());
                stmt.setInt(4, film.getDuration());
                stmt.setInt(5, film.getMpa().getId());
                stmt.setInt(6, film.getRate());
                return stmt;
            }, keyHolder);
            film.setId(keyHolder.getKey().longValue());
            return film;
    }

    @Override
    public Film update(Film film) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where id = ?", film.getId());
        if (!filmRows.next()) {
            log.warn("Фильм не найден.");
            throw new FilmNotFoundException("Фильм не найден.");
        }
        String sql = "update film set " +
                "name = ?, release_date = ?, description = ?, duration = ?, rating_id = ?, rate = ? " +
                "where id = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                java.sql.Date.valueOf(film.getReleaseDate()),
                film.getDescription(),
                film.getDuration(),
                film.getMpa().getId(),
                film.getRate(),
                film.getId());
        return film;
    }

    private Rating makeRating(int id) {
        SqlRowSet rows = jdbcTemplate.queryForRowSet("select * from rating where id = ?", id);
        if (rows.next()) {
            return new Rating(id, rows.getString("name"));
        } else {
            return new Rating(id, "NULL");
        }
    }

    public void isInBase(Film film) throws ValidationException {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from film where name = ? " +
                "and release_date = ?", film.getName(), java.sql.Date.valueOf(film.getReleaseDate()));
        if (filmRows.next()) {
            log.warn("Такой фильм уже есть в базе");
            throw new ValidationException("Ошибка валидации");
        }
    }
}
