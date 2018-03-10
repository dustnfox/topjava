package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.service.MealServiceImpl;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkForNullOrWrongUser;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private MealService service = new MealServiceImpl();

    public List<MealWithExceed> getAll() {
        log.info("getAll");
        List<Meal> meals = service.getAll(AuthorizedUser.id());
        return MealsUtil.getWithExceeded(meals, AuthorizedUser.getCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        Meal meal = service.get(id);
        checkForNullOrWrongUser(meal, AuthorizedUser.id());
        return meal;
    }

    public void create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        meal.setUserId(AuthorizedUser.id());
        service.create(meal);
    }

    public void delete(Integer id) {
        log.info("delete {}", id);
        Meal meal = service.get(id);
        checkForNullOrWrongUser(meal, AuthorizedUser.id());
        service.delete(id);
    }

    public void update(Meal meal) {
        log.info("update {}", meal);
        Meal oldMeal = service.get(meal.getId());
        checkForNullOrWrongUser(oldMeal, AuthorizedUser.id());
        meal.setUserId(AuthorizedUser.id());
        service.update(meal);
    }

}