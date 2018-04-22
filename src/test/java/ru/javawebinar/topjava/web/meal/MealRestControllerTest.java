package ru.javawebinar.topjava.web.meal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.readFromJson;
import static ru.javawebinar.topjava.UserTestData.USER_ID;
import static ru.javawebinar.topjava.web.json.JsonUtil.contentJson;

public class MealRestControllerTest extends AbstractControllerTest {
    private static final String REST_URL = MealRestController.REST_URL + '/';

    @Autowired
    protected MealService mealService;

    @Test
    public void testGet() throws Exception {
        mockMvc.perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL1));
    }

    @Test
    public void testDelete() throws Exception {
        mockMvc.perform(delete(REST_URL + MEAL1_ID))
                .andDo(print())
                .andExpect(status().isNoContent());
        assertMatches(mealService.getAll(USER_ID), Arrays.asList(MEAL6, MEAL5, MEAL4, MEAL3, MEAL2));
    }

    @Test
    public void testGetAll() throws Exception {
        mockMvc.perform(get(REST_URL))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEALS_WITH_EXCEED));
    }

    @Test
    public void testCreate() throws Exception {
        Meal expected = new Meal(LocalDateTime.of(2018, 4, 19, 13, 28), "New Description", 345);
        ResultActions action = mockMvc.perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(expected)))
                .andDo(print())
                .andExpect(status().isCreated());

        Meal returned = readFromJson(action, Meal.class);
        expected.setId(returned.getId());

        assertMatch(returned, expected);
        assertMatches(mealService.getAll(USER_ID), expected, MEAL6, MEAL5, MEAL4, MEAL3, MEAL2, MEAL1);
    }

    @Test
    public void testUpdate() throws Exception {
        Meal updatedMeal = new Meal(MEAL1);
        updatedMeal.setDescription("Updated Description");
        mockMvc.perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMeal)))
                .andExpect(status().isOk());

        assertMatch(mealService.get(MEAL1_ID, USER_ID), updatedMeal);
    }

    @Test
    public void testGetBetweenDateTime() throws Exception {
        mockMvc.perform(get(REST_URL + constructFilterString(MEAL2, MEAL2, MEAL3, MEAL2)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(new Meal[]{MEAL2}));
    }

    @Test
    public void testGetBetweenDate() throws Exception {
        mockMvc.perform(get(REST_URL + constructFilterString(MEAL2, null, MEAL3, null)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL3, MEAL2, MEAL1));
    }

    @Test
    public void testGetBetweenTime() throws Exception {

        mockMvc.perform(get(REST_URL + constructFilterString(null, MEAL3, null, MEAL3)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(contentJson(MEAL6, MEAL3));
    }

    private void appendLT(Meal meal, StringBuilder sb, String prefix) {
        if (meal != null) {
            sb.append(prefix).append('=').append(meal.getDateTime().toLocalTime().format(DateTimeFormatter.ISO_TIME)).append('&');
        }
    }

    private void appendLD(Meal meal, StringBuilder sb, String prefix) {
        if (meal != null) {
            sb.append(prefix).append('=').append(meal.getDateTime().toLocalDate().format(DateTimeFormatter.ISO_DATE)).append('&');
        }
    }

    private String constructFilterString(Meal startDate, Meal startTime, Meal endDate, Meal endTime) {
        StringBuilder sb = new StringBuilder("filter?");
        appendLD(startDate, sb, "fromDate");
        appendLT(startTime, sb, "fromTime");
        appendLD(endDate, sb, "toDate");
        appendLT(endTime, sb, "toTime");

        return sb.charAt(sb.length() - 1) == '&' ? sb.substring(0, sb.length() - 1) : sb.toString();
    }
}