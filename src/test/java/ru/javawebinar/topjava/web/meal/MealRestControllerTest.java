package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.exception.ErrorInfo;
import ru.javawebinar.topjava.util.exception.ErrorType;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.MealTestData.assertMatch;
import static ru.javawebinar.topjava.TestUtil.contentJson;
import static ru.javawebinar.topjava.TestUtil.*;
import static ru.javawebinar.topjava.UserTestData.*;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealRestControllerTest extends AbstractControllerTest {

    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    private MealService service;

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + ADMIN_MEAL_ID)
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(ADMIN_MEAL1));
    }

    @Test
    public void testGetUnauth() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testGetNotFound() throws Exception {
        mockMvc.perform(get(REST_URL + ADMIN_MEAL_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID)
                .with(userHttpBasic(USER)))
                .andExpect(status().isNoContent());
        assertMatch(service.getAll(START_SEQ), MEAL6, MEAL5, MEAL4, MEAL3, MEAL2);
    }

    @Test
    public void testDeleteNotFound() throws Exception {
        mockMvc.perform(delete(REST_URL + ADMIN_MEAL_ID)
                .with(userHttpBasic(USER)))
                .andDo(print())
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updated = getUpdated();

        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk());

        assertMatch(service.get(MEAL1_ID, START_SEQ), updated);
    }

    @Test
    public void testInvalidDataUpdate() throws Exception {
        Meal updated = getUpdated();
        updated.setDescription("");
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updated))
                .with(userHttpBasic(USER)))
                .andExpect(status().isUnprocessableEntity());

        assertMatch(service.getAll(USER_ID), MEALS);
    }

    @Test
    public void testCreate() throws Exception {
        Meal created = getCreated();
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)));

        Meal returned = readFromJson(action, Meal.class);
        created.setId(returned.getId());

        assertMatch(returned, created);
        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEAL2, created, ADMIN_MEAL1);
    }

    @Test
    public void testInvalidDataCreate() throws Exception {
        Meal created = getCreated();
        created.setCalories(1);
        mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(created))
                .with(userHttpBasic(ADMIN)))
                .andExpect(status().isUnprocessableEntity());

        assertMatch(service.getAll(ADMIN_ID), ADMIN_MEAL2, ADMIN_MEAL1);
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL)
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MealsUtil.getWithExceeded(MEALS, USER.getCaloriesPerDay())));
    }

    @Test
    public void testFilter() throws Exception {
        mockMvc.perform(get(REST_URL + "filter")
                .param("startDate", "2015-05-30").param("startTime", "07:00")
                .param("endDate", "2015-05-31").param("endTime", "11:00")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(contentJsonArray(
                        MealsUtil.createWithExceed(MEAL4, true),
                        MealsUtil.createWithExceed(MEAL1, false)));
    }

    @Test
    public void testFilterAll() throws Exception {
        mockMvc.perform(get(REST_URL + "filter?startDate=&endTime=")
                .with(userHttpBasic(USER)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(contentJson(MealsUtil.getWithExceeded(Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1), USER.getCaloriesPerDay())));
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testForDuplicateDatetimeCreate() throws Exception {
        Meal expectedMeal = new Meal(MEAL1.getDateTime(), MEAL1.getDescription(), MEAL1.getCalories());
        ErrorInfo expectedError = new ErrorInfo("http://localhost" + REST_URL, ErrorType.DATA_ERROR,
                "org.springframework.dao.DataIntegrityViolationException: Meal with this date and time already exists");
        MvcResult actual = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(expectedMeal)))
                .andExpect(status().isConflict())
                .andReturn();
        JSONAssert.assertEquals(JsonUtil.writeValue(expectedError), actual.getResponse().getContentAsString(), true);
    }

    @Test
    @Transactional(propagation = Propagation.NEVER)
    public void testForDuplicateDatetimeUpdate() throws Exception {
        Meal expectedMeal = new Meal(MEAL1_ID, MEAL2.getDateTime(), MEAL1.getDescription(), MEAL1.getCalories());
        ErrorInfo expectedError = new ErrorInfo("http://localhost" + REST_URL + MEAL1_ID, ErrorType.DATA_ERROR,
                "org.springframework.dao.DataIntegrityViolationException: Meal with this date and time already exists");
        MvcResult actual = mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .with(userHttpBasic(USER))
                .content(JsonUtil.writeValue(expectedMeal)))
                .andExpect(status().isConflict())
                .andReturn();
        JSONAssert.assertEquals(JsonUtil.writeValue(expectedError), actual.getResponse().getContentAsString(), true);
    }
}