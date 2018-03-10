package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.DateTimeFilter;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkForNullOrWrongUser;

@Service
public class MealServiceImpl implements MealService {

    @Autowired
    private MealRepository repository;

    @Override
    public Meal create(Meal meal, int userId) {
        meal.setUserId(userId);
        return repository.save(meal);
    }

    @Override
    public void delete(int id, int userId) throws NotFoundException {
        Meal meal = repository.get(id);
        checkForNullOrWrongUser(meal, userId);
        repository.delete(id);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException {
        return checkForNullOrWrongUser(repository.get(id), userId);
    }

    @Override
    public List<Meal> getAll(int userId, DateTimeFilter dateTimeFilter) {
        return dateTimeFilter.isEmpty() ?
                repository.getAll(userId) :
                repository.getAllWithFilter(userId, dateTimeFilter);
    }

    @Override
    public void update(Meal meal, int userId) {
        meal.setUserId(userId);
        Meal oldMeal = repository.get(meal.getId());
        checkForNullOrWrongUser(oldMeal, userId);
        repository.save(meal);
    }
}