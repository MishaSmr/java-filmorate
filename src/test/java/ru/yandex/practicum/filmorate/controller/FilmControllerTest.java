package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.LocalDate;


class FilmControllerTest {

    FilmController controller = new FilmController();
    Film film = Film.builder()
            .name("Inception")
            .id(1)
            .description("The best film")
            .releaseDate(LocalDate.of(2010, 7, 8))
            .duration(Duration.ofMinutes(148))
            .build();

    @Test
    public void shouldReturnSuccessfulValidateWhenAddFilmWithCorrectValues() throws ValidationException {
        controller.validateFilm(film);
    }

    @Test
    public void shouldThrowExceptionWhenNameIsEmpty() {
        film.setName("");
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        controller.validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldReturnSuccessfulValidateWhenDescriptionIs200() throws ValidationException {
        film.setDescription("A".repeat(200));
        controller.validateFilm(film);
    }

    @Test
    public void shouldThrowExceptionWhenDescriptionIs201() {
        film.setDescription("A".repeat(201));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        controller.validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDescriptionIsMuchMoreThan200() {
        film.setDescription("A".repeat(500));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        controller.validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldReturnSuccessfulValidateWhenReleaseDateIsStartDay() throws ValidationException {
        film.setReleaseDate(LocalDate.of(1895, 12, 28));
        controller.validateFilm(film);
    }

    @Test
    public void shouldThrowExceptionWhenReleaseDateIsBeforeStartDay() {
        film.setReleaseDate(LocalDate.of(1000, 10, 10));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        controller.validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDurationIsZero() {
        film.setDuration(Duration.ofMinutes(0));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        controller.validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenDurationIsNegative() {
        film.setDuration(Duration.ofMinutes(-1));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        controller.validateFilm(film));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenFilmAlreadyInBase() {
        controller.getFilms().put(film.getId(), film);
        Film film2 = Film.builder()
                .name("Inception")
                .description("Top")
                .releaseDate(LocalDate.of(2010, 7, 8))
                .duration(Duration.ofMinutes(148))
                .build();
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        controller.isInBase(film2));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

}