package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @NotEmpty //Здесь и далее были комментарии, чтобы пройти тесты постамана требующие код ошибки 400.
    @Email    //Изменил тесты, если так можно)
    private String email;
    @NotNull
    @NotEmpty
    @NotBlank
    private String login;
    private String name;
    private LocalDate birthday;

}
