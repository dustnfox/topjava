package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private static final Comparator<Meal> DATETIME_CMP = Comparator.comparing(Meal::getDateTime);

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
    public boolean delete(int id) {
        for (Map<Integer, Meal> userMealsMap : repository.values()) {
            if (userMealsMap.remove(id) != null) {
                return true;
            }
        }
        return false;
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
    public List<Meal> getAll(int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals != null) {
            return meals.values().stream().sorted(DATETIME_CMP).collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public List<Meal> getAll() {
        return repository.values().stream()
                .flatMap(map -> map.values().stream())
                .sorted(DATETIME_CMP)
                .collect(Collectors.toList());
    }
}

