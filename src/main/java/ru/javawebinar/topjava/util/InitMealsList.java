package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.storage.Storage;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class InitMealsList {
    public static final int CALORIES_THRESHOLD = 2000;
    private static final List<Meal> meals = Arrays.asList(
            new Meal(LocalDateTime.of(2017, Month.MAY, 30, 10, 0), "Завтрак", 500),
            new Meal(LocalDateTime.of(2017, Month.MAY, 30, 13, 0), "Обед", 1000),
            new Meal(LocalDateTime.of(2017, Month.MAY, 30, 20, 0), "Ужин", 500),
            new Meal(LocalDateTime.of(2017, Month.MAY, 31, 10, 0), "Завтрак", 1000),
            new Meal(LocalDateTime.of(2017, Month.MAY, 31, 13, 0), "Обед", 500),
            new Meal(LocalDateTime.of(2017, Month.MAY, 31, 20, 0), "Ужин", 510));

    public static void addMealsData(Storage storage) {
        meals.forEach(storage::add);
    }

    public static List<Meal> getMealsList() {
        return meals;
    }
}
