package ru.javawebinar.topjava.web;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.javawebinar.topjava.MealTestData.MEALS_WITH_EXCEED;
import static ru.javawebinar.topjava.MealTestData.assertMatches;
import static ru.javawebinar.topjava.UserTestData.USER;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class RootControllerTest extends AbstractControllerTest {

    @Test
    public void testUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("users"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/users.jsp"))
                .andExpect(model().attribute("users", hasSize(2)))
                .andExpect(model().attribute("users", hasItem(
                        allOf(
                                hasProperty("id", is(START_SEQ)),
                                hasProperty("name", is(USER.getName()))
                        )
                )));
    }

    @Test
    public void testMeals() throws Exception {
        final class MealWithExceedMatcher extends BaseMatcher<List<MealWithExceed>> {
            private final List<MealWithExceed> EXPECTED;

            private MealWithExceedMatcher(List<MealWithExceed> expected) {
                EXPECTED = expected;
            }

            @Override
            public void describeTo(Description description) {
            }

            @Override
            public boolean matches(Object item) {
                assertMatches((List<MealWithExceed>) item, EXPECTED);
                return true;
            }
        }

        mockMvc.perform(get("/meals"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("meals"))
                .andExpect(forwardedUrl("/WEB-INF/jsp/meals.jsp"))
                .andExpect(model().attribute("meals", hasSize(6)))
                .andExpect(model().attribute("meals", new MealWithExceedMatcher(MEALS_WITH_EXCEED)));
    }
}