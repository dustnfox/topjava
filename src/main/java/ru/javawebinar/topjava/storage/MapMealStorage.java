package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.exceptions.MealNonExistException;
import ru.javawebinar.topjava.model.Meal;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MapMealStorage implements Storage {
    private final AtomicInteger id = new AtomicInteger(0);

    private final Map<Integer, Meal> map = new ConcurrentHashMap<>();

    @Override
    public Meal get(int id) {
        Meal meal = map.get(id);
        if (meal == null) {
            throw new MealNonExistException(id);
        }
        return meal;
    }

    @Override
    public void add(Meal m) {
        map.put(m.getId(), m);
    }

    @Override
    public void add(List<Meal> mealList) {
        mealList.forEach(this::add);
    }

    @Override
    public void delete(int id) {
        Meal meal = map.remove(id);
        if (meal == null) {
            throw new MealNonExistException(id);
        }
    }

    @Override
    public void clear() {
        map.clear();
    }

    @Override
    public List<Meal> getAll() {
        List<Meal> list = new ArrayList<>(map.values());
        list.sort(Comparator.comparing(Meal::getDate));
        return list;
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public int getNextId() {
        return id.getAndIncrement();
    }
}
