package ru.javawebinar.topjava.web.meal;

import ru.javawebinar.topjava.model.Meal;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public class MealRestController extends AbstractMealController {

    public Meal create(Meal meal) {
        checkNew(meal);
        return doCreate(meal);
    }
}