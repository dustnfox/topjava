package ru.javawebinar.topjava.repository.mock;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.MEALS.forEach(this::save);
    }

    @Override
    public Meal save(Meal meal) {
        Map<Integer, Meal> userMealsMap = repository.get(meal.getUserId());
        if (userMealsMap == null) {
            userMealsMap = new HashMap<>();
            repository.putIfAbsent(meal.getUserId(), userMealsMap);
        }

        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            userMealsMap.put(meal.getId(), meal);
            return meal;
        }
        // treat case: update, but absent in storage
        return userMealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public void delete(int id) {
        for (Map<Integer, Meal> userMealsMap : repository.values()) {
            if (userMealsMap.remove(id) != null) {
                return;
            }
        }
    }

    @Override
    public Meal get(int id) {
        for (Map<Integer, Meal> userMealsMap : repository.values()) {
            Meal meal = userMealsMap.get(id);
            if (meal != null) {
                return meal;
            }
        }
        return null;
    }

    @Override
    public Collection<Meal> getAll(Integer userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null ? new ArrayList<>(meals.values()) : new ArrayList<>();
    }
}

