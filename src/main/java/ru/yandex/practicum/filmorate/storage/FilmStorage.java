package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;

public interface FilmStorage {

    public Film get(long id);

    public void remove(Film film);

    public Collection<Film> getAll();

    public void create(Film film);

    public void update(Film film);

    public HashMap<Long, Film> getFilms();
}
