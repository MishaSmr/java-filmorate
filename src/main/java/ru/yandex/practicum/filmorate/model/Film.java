package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;

@Data
@Builder
public class Film {
   //@NotEmpty
    //@NotNull
    private String name;
    private int id;
   //@Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    private Duration duration;

}
