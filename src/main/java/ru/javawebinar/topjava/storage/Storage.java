package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface Storage {
    Meal get(int id);

    void add(Meal m);

    void add(List<Meal> mealList);

    void delete(int id);

    void clear();

    List<Meal> getAll();

    int size();

    int getNextId();
}
