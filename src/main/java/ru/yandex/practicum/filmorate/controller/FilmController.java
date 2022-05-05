package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.IncorrectParameterException;
import ru.yandex.practicum.filmorate.exceptions.IncorrectPathVariableException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final InMemoryFilmStorage filmStorage;
    private final FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage filmStorage, FilmService filmService) {
        this.filmStorage = filmStorage;
        this.filmService = filmService;
    }

    @GetMapping
    public Collection<Film> getAll() {
        return filmStorage.getAll();
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable Long id) {
        if (id == null || id <= 0) {
            throw new IncorrectPathVariableException("id");
        }
        return filmStorage.getFilm(id);
    }

    @PostMapping
    public void create(@Valid @RequestBody Film film) throws ValidationException {
        filmStorage.create(film);
    }

    @PutMapping
    public void update(@Valid @RequestBody Film film) throws ValidationException {
        filmStorage.update(film);
    }

    @PutMapping("/{id}/like/{userId}")
    public void putLike(@PathVariable Long id, @PathVariable Long userId) {
        if (id == null || id <= 0) {
            throw new IncorrectPathVariableException("id");
        }
        if (userId == null || userId <= 0) {
            throw new IncorrectPathVariableException("friendId");
        }
        filmService.putLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(@PathVariable Long id, @PathVariable Long userId) {
        if (id == null || id <= 0) {
            throw new IncorrectPathVariableException("id");
        }
        if (userId == null || userId <= 0) {
            throw new IncorrectPathVariableException("friendId");
        }
        filmService.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public Collection<Film> getTop(@RequestParam(defaultValue = "10") Integer count) {
        if (count <= 0) {
            throw new IncorrectParameterException("count");
        }
        return filmService.getTop(count);
    }
}
