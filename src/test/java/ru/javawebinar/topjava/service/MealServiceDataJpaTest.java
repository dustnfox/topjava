package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.UserTestData.USER_ID;

@ActiveProfiles(Profiles.DATAJPA)
public class MealServiceDataJpaTest extends MealServiceTest {

    @Test
    public void getWithUser() {
        Meal actual = service.getWithUser(MEAL1_ID, USER_ID);
        assertMatchWithUser(actual, MEAL1);
    }

    @Test
    public void getWithUserNotFound() {
        thrown.expect(NotFoundException.class);
        service.getWithUser(1, USER_ID);
    }
}