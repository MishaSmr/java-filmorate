package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.TreeSet;

@Data
@Builder
public class Film {
    //@NotEmpty
    //@NotNull
    private String name;
    private long id;
    //@Size(max = 200)
    private String description;
    private LocalDate releaseDate;
    private int duration;
    private Rating mpa;
    private int rate;
}
