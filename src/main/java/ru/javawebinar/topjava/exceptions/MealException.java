package ru.javawebinar.topjava.exceptions;

public class MealException extends RuntimeException {
    private final int id;


    public MealException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
