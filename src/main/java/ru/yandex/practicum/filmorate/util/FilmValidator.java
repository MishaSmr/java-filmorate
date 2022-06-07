package ru.yandex.practicum.filmorate.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

@Component
@Slf4j
public class FilmValidator {

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
        if (film.getDuration() <= 0) {
            log.warn("Продолжительность не может быть отрицательной");
            throw new ValidationException("Ошибка валидации");
        }
    }

    private static boolean checkReleaseDate(LocalDate date) {
        return date.isBefore(LocalDate.of(1895, 12, 28));
    }

}
