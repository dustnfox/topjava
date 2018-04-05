package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import static ru.javawebinar.topjava.UserTestData.*;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTest {

    @Test
    public void getWithMeals() {
        User user = service.getWithMeals(USER_ID);
        assertMatchWithMeals(user, USER);
    }

    @Test
    public void getWithMealsNotFound() {
        thrown.expect(NotFoundException.class);
        service.getWithMeals(1);
    }
}