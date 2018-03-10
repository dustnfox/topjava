package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.DateTimeFilter;
import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealRepository {
    Meal save(Meal meal);

    boolean delete(int id);

    Meal get(int id);

    List<Meal> getAll();

    List<Meal> getAll(int userId);

    List<Meal> getAllWithFilter(int userId, DateTimeFilter dateTimeFilter);
}
