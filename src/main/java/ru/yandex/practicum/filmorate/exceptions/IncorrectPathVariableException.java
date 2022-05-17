package ru.yandex.practicum.filmorate.exceptions;

public class IncorrectPathVariableException extends RuntimeException {

    private final String pathVariable;

    public IncorrectPathVariableException(String parameter) {
        this.pathVariable = parameter;
    }

    public String getPathVariable() {
        return pathVariable;
    }
}
