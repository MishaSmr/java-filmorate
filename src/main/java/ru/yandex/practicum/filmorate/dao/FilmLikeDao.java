package ru.yandex.practicum.filmorate.dao;

import org.springframework.jdbc.support.rowset.SqlRowSet;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmLikeDao {

    public void putLike(Long id, Long userId);

    public void removeLike(Long id, Long userId);

    public List<Film> getTop(int count);
}
