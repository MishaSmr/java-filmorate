package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.time.LocalDate;

class InMemoryUserStorageTest {

    InMemoryUserStorage userStorage = new InMemoryUserStorage();
    User user = User.builder()
            .email("aaa@bbb.com")
            .login("Aa777")
            .name("Name")
            .birthday(LocalDate.of(1990, 7, 8))
            .build();

    @Test
    public void shouldReturnSuccessfulValidateWhenAddUserWithCorrectValues() throws ValidationException {
        userStorage.getValidator().validateUser(user);
    }

    @Test
    public void shouldThrowExceptionWhenEmailIsEmpty() {
        user.setEmail("");
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        userStorage.getValidator().validateUser(user));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenEmailWithoutAt() {
        user.setEmail("email.ru");
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        userStorage.getValidator().validateUser(user));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenLoginIsEmpty() {
        user.setLogin("");
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        userStorage.getValidator().validateUser(user));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenLoginWithBlank() {
        user.setLogin("my login");
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        userStorage.getValidator().validateUser(user));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }

    @Test
    public void shouldThrowExceptionWhenBirthdayIsInFuture() {
        user.setBirthday(LocalDate.of(2222, 10, 10));
        ValidationException ex = Assertions.assertThrows(
                ValidationException.class,
                () ->
                        userStorage.getValidator().validateUser(user));
        Assertions.assertEquals("Ошибка валидации",
                ex.getMessage());
    }
}