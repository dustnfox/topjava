package ru.javawebinar.topjava.exceptions;

public class MealNonExistException extends MealException {
    public MealNonExistException(int id) {
        super(id);
    }
}
