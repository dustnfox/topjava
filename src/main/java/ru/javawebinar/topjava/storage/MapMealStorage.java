package ru.javawebinar.topjava.storage;

import ru.javawebinar.topjava.model.Meal;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MapMealStorage implements Storage {
    private final AtomicInteger id = new AtomicInteger(0);

    private final Map<Integer, Meal> map = new ConcurrentHashMap<>();

    @Override
    public Meal get(int id) {
        return map.get(id);
    }

    @Override
    public void add(Meal m) {
        if (m.getId() == -1) {
            m = new Meal(id.getAndIncrement(), m.getDateTime(), m.getDescription(), m.getCalories());
        }
        map.put(m.getId(), m);
    }

    @Override
    public void delete(int id) {
        map.remove(id);
    }

    @Override
    public List<Meal> getAll() {
        return new ArrayList<>(map.values());
    }

}
