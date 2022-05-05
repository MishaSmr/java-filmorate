package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

@Component
@Slf4j
public class InMemoryFilmStorage implements FilmStorage {

    private final HashMap<Long, Film> films = new HashMap<>();
    private long id = 1;

    @Override
    public Collection<Film> getAll() {
        return films.values();
    }

    @Override
    public Film getFilm (long id) {
        if (!films.containsKey(id)) {
            log.warn("Фильм c таким id не найден.");
            throw new FilmNotFoundException("Фильм c таким id не найден.");
        }
        return films.get(id);
    }

    @Override
    public void create(Film film) {
        validateFilm(film);
        isInBase(film);
        film.setId(id++);
        film.setLikes(new HashSet<>());
        films.put(film.getId(), film);
        log.debug("Текущее количество фильмов: {}", films.size());
    }

    @Override
    public void update(Film film) {
        validateFilm(film);
        Film oldFilm = films.get(film.getId());
        film.setLikes(oldFilm.getLikes());
        film.setLikesCount(oldFilm.getLikesCount());
        films.put(film.getId(), film);
        log.debug("Текущее количество фильмов: {}", films.size());
    }

    public void validateFilm(Film film) throws ValidationException {
        if (film.getName().isEmpty()) {
            log.warn("Название не может быть пустым");
            throw new ValidationException("Ошибка валидации");
        }
        if (film.getDescription().length() > 200) {
            log.warn("Длина описания больше 200 символова");
            throw new ValidationException("Ошибка валидации");
        }
        if (film.getDescription().isEmpty()) {
            log.warn("Описание не может быть пустым");
            throw new ValidationException("Ошибка валидации");
        }
        if (checkReleaseDate(film.getReleaseDate())) {
            log.warn("Дата релиза не может быть раньше 28 декабря 1895 года");
            throw new ValidationException("Ошибка валидации");
        }
        if (film.getDuration().isNegative() || film.getDuration().isZero()) {
            log.warn("Продолжительность не может быть отрицательной");
            throw new ValidationException("Ошибка валидации");
        }
    }

    private static boolean checkReleaseDate(LocalDate date) {
        return date.isBefore(LocalDate.of(1895, 12, 28));
    }

    public void isInBase(Film film) throws ValidationException {
        for (Film f : films.values()) {
            if (f.getName().equals(film.getName()) && f.getReleaseDate().equals(film.getReleaseDate())) {
                log.warn("Такой фильм уже есть в базе");
                throw new ValidationException("Ошибка валидации");
            }
        }
    }

    public HashMap<Long, Film> getFilms() {
        return films;
    }
}
