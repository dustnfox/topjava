package ru.javawebinar.topjava.repository.mock;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.DateTimeUtil.isBetween;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private static final Comparator<Meal> DATETIME_CMP =
            Comparator.comparing(Meal::getDateTime).reversed().thenComparing(Meal::getId);

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

        return userMealsMap.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> userMealsMap = repository.get(userId);
        return userMealsMap != null && userMealsMap.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> userMealsMap = repository.get(userId);
        if (userMealsMap != null) {
            return userMealsMap.get(id);
        }
        return null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllWithPredicate(userId, m -> true);
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate) {
        return getAllWithPredicate(userId, m ->
                isBetween(m.getDate(), startDate, endDate));
    }

    private List<Meal> getAllWithPredicate(int userId, Predicate<Meal> predicate) {
        Map<Integer, Meal> meals = repository.get(userId);
        if (meals != null) {
            return meals.values().stream()
                    .filter(predicate)
                    .sorted(DATETIME_CMP)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}

