package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.DateTimeFilter;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealWithExceed> getAll(DateTimeFilter dtf) {
        log.info("getAll");
        List<Meal> meals = service.getAll(AuthorizedUser.id(), dtf);
        return MealsUtil.getWithExceeded(meals, AuthorizedUser.getCaloriesPerDay());
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, AuthorizedUser.id());
    }

    public void create(Meal meal) {
        log.info("create {}", meal);
        checkNew(meal);
        service.create(meal, AuthorizedUser.id());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, AuthorizedUser.id());
    }

    public void update(Meal meal) {
        log.info("update {}", meal);
        service.update(meal, AuthorizedUser.id());
    }

}