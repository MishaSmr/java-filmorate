package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import java.time.LocalDate;


class InMemoryFilmStorageTest {

    InMemoryFilmStorage filmStorage = new InMemoryFilmStorage();
    Film film = Film.builder()
            .name("Inception")
            .id(1)
            .description("The best film")
            .releaseDate(LocalDate.of(2010, 7, 8))
            .duration(148)
            .build();

    @Test
    public void shouldReturnSuccessfulValidateWhenAddFilmWithCorrectValues() throws ValidationException {
        filmStorage.getValidator().validateFilm(film);
    }

    @Test
    public void shouldThrowExceptionWhenNameIsEmpty() {
        film.setName("");
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        filmStorage.getValidator().validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldReturnSuccessfulValidateWhenDescriptionIs200() throws ValidationException {
        film.setDescription("A".repeat(200));
        filmStorage.getValidator().validateFilm(film);
    }

    @Test
    public void shouldThrowExceptionWhenDescriptionIs201() {
        film.setDescription("A".repeat(201));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        filmStorage.getValidator().validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDescriptionIsEmpty() {
        film.setDescription("");
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        filmStorage.getValidator().validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDescriptionIsMuchMoreThan200() {
        film.setDescription("A".repeat(500));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        filmStorage.getValidator().validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldReturnSuccessfulValidateWhenReleaseDateIsStartDay() throws ValidationException {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        filmStorage.getValidator().validateFilm(film);
    }

    @Test
    public void shouldThrowExceptionWhenReleaseDateIsBeforeStartDay() {
        film.setReleaseDate(LocalDate.of(1000, 10, 10));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        filmStorage.getValidator().validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDurationIsZero() {
        film.setDuration(0);
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        filmStorage.getValidator().validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDurationIsNegative() {
        film.setDuration(-1);
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        filmStorage.getValidator().validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFilmAlreadyInBase() {
        filmStorage.getFilms().put(film.getId(), film);
        Film film2 = Film.builder()
                .name("Inception")
                .description("Top")
                .releaseDate(LocalDate.of(2010, 7, 8))
                .duration(148)
                .build();
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        filmStorage.isInBase(film2));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

}