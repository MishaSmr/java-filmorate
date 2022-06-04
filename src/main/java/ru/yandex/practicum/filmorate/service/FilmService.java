package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dao.FilmLikeDao;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

@Service
@Slf4j
public class FilmService {

    @Autowired
    private final FilmLikeDao filmDao;

    public FilmService(FilmLikeDao filmDao) {
        this.filmDao = filmDao;
    }

    public void putLike(Long id, Long userId) {
        filmDao.putLike(id, userId);
    }

    public void removeLike(Long id, Long userId) {
        filmDao.removeLike(id, userId);
    }

    public List<Film> getTop(int count) {
        return filmDao.getTop(count);
    }
}
