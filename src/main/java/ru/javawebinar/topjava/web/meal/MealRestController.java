package ru.javawebinar.topjava.web.meal;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MealRestController extends AbstractMealController {

    public Meal get(int id) {
        return doGet(id);
    }

    public void delete(int id) {
        doDelete(id);
    }

    public List<MealWithExceed> getAll() {
        return doGetAll();
    }

    public Meal create(Meal meal) {
        return doCreate(meal);
    }

    public void update(Meal meal, int id) {
        doUpdate(meal, id);
    }

    public List<MealWithExceed> getBetween(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime) {
        return doGetBetween(startDate, startTime, endDate, endTime);
    }
}