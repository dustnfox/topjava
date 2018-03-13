package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    @Autowired
    public MealRestController(MealService service) {
        this.service = service;
    }

    public List<MealWithExceed> getAll() {
        log.info("getAll");

        List<Meal> meals = service.getAll(AuthorizedUser.id());
        return MealsUtil.getWithExceeded(meals, AuthorizedUser.getCaloriesPerDay());
    }

    public List<MealWithExceed> getAll(String startDate, String startTime, String endDate, String endTime) {
        log.info("getAll");

        LocalDate sd = DateTimeUtil.dateOf(startDate, LocalDate.MIN);
        LocalDate ed = DateTimeUtil.dateOf(endDate, LocalDate.MAX);
        LocalTime st = DateTimeUtil.timeOf(startTime, LocalTime.MIN);
        LocalTime et = DateTimeUtil.timeOf(endTime, LocalTime.MAX);

        List<Meal> meals = service.getAll(AuthorizedUser.id(), sd, ed);
        return MealsUtil.getFilteredWithExceeded(meals, AuthorizedUser.getCaloriesPerDay(), st, et);
    }

    public Meal get(int id) {
        log.info("get {}", id);
        return service.get(id, AuthorizedUser.id());
    }

    public void delete(int id) {
        log.info("delete {}", id);
        service.delete(id, AuthorizedUser.id());
    }

    public void save(Meal meal) {
        log.info("save {}", meal);
        checkNew(meal);
        service.save(meal, AuthorizedUser.id());
    }

    public void update(Meal meal, int id) {
        log.info("update {}", meal);
        assureIdConsistent(meal, id);
        service.update(meal, AuthorizedUser.id());
    }

}