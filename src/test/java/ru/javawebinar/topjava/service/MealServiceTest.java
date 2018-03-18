package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.ADMIN_ID;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService service;

    @Test
    public void get() {
        Meal actual = service.get(MEAL_ID_1, USER_ID);
        assertMatch(actual, MEAL_1);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundGet() {
        service.get(MEAL_NOT_EXIST_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void notYoursGet() {
        service.get(MEAL_ID_1, ADMIN_ID);
    }

    @Test
    public void delete() {
        service.delete(MEAL_ID_1, USER_ID);
        List<Meal> expectedMeals = Arrays.asList(MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2);
        assertMatch(service.getAll(USER_ID), expectedMeals);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundDelete() {
        service.delete(MEAL_NOT_EXIST_ID, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void notYoursDelete() {
        service.delete(MEAL_ID_1, ADMIN_ID);
    }

    @Test
    public void getBetweenDates() {
        List<Meal> actual = service.getBetweenDates(FIRST_DAY, FIRST_DAY, USER_ID);
        List<Meal> expected = Arrays.asList(MEAL_3, MEAL_2, MEAL_1);
        assertMatch(actual, expected);
    }

    @Test
    public void getBetweenDateTimes() {
        List<Meal> actual = service.getBetweenDateTimes(FIRST_DAY_TEN_OCLOCK, FIRST_DAY_FIVE_OCLOCK, USER_ID);
        List<Meal> expected = Collections.singletonList(MEAL_2);
        assertMatch(actual, expected);
    }

    @Test
    public void getAll() {
        List<Meal> actual = service.getAll(USER_ID);
        List<Meal> expected = Arrays.asList(MEAL_6, MEAL_5, MEAL_4, MEAL_3, MEAL_2, MEAL_1);
        assertMatch(actual, expected);
    }

    @Test
    public void update() {
        Meal expected = new Meal(MEAL_1);
        expected.setDateTime(LocalDateTime.of(1703, 11, 12, 23, 24));
        expected.setDescription("Brand new description");
        expected.setCalories(12345);
        service.update(expected, USER_ID);
        assertMatch(service.get(MEAL_ID_1, USER_ID), expected);
    }

    @Test(expected = NotFoundException.class)
    public void notFoundUpdate() {
        Meal notYoursMeal = new Meal(MEAL_NOT_EXIST_ID, LocalDateTime.now(), "", 0);
        service.update(notYoursMeal, USER_ID);
    }

    @Test(expected = NotFoundException.class)
    public void notYoursUpdate() {
        Meal notYoursMeal = new Meal(MEAL_ADMIN_ID, LocalDateTime.now(), "", 0);
        service.update(notYoursMeal, USER_ID);
    }

    @Test
    public void create() {
        Meal expected = new Meal(LocalDateTime.now(), "new Description", 0);
        int actualId = service.create(expected, USER_ID).getId();
        assertMatch(service.get(actualId, USER_ID), expected);
    }

    @Test(expected = DuplicateKeyException.class)
    public void sameDateTimeCreate() {
        Meal newMeal = new Meal(MEAL_1.getDateTime(), "Same timestamp meal", 0);
        service.create(newMeal, USER_ID);
    }
}