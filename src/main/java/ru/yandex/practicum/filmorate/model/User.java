package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.HashSet;

@Data
@Builder
public class User {
    private long id;
    //@NotEmpty //Здесь и далее закомментировано, чтобы формировать исключение как в пройденной теории
    //@Email
    private String email;
    //@NotNull
    //@NotEmpty
    //@NotBlank
    private String login;
    private String name;
    private LocalDate birthday;
}
