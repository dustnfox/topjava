package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int MEAL_ID_1 = START_SEQ + 2;
    public static final int MEAL_ID_2 = START_SEQ + 3;
    public static final int MEAL_ID_3 = START_SEQ + 4;
    public static final int MEAL_ID_4 = START_SEQ + 5;
    public static final int MEAL_ID_5 = START_SEQ + 6;
    public static final int MEAL_ID_6 = START_SEQ + 7;
    public static final int MEAL_ADMIN_ID = START_SEQ + 8;
    public static final int MEAL_NOT_EXIST_ID = 1;
    public static final LocalDate FIRST_DAY = LocalDate.of(2018, 3, 16);
    public static final LocalDateTime FIRST_DAY_TEN_OCLOCK =
            LocalDateTime.of(FIRST_DAY, LocalTime.of(10, 0));
    public static final LocalDateTime FIRST_DAY_FIVE_OCLOCK =
            LocalDateTime.of(FIRST_DAY, LocalTime.of(17, 0));

    public static final Meal MEAL_1 = new Meal(MEAL_ID_1,
            LocalDateTime.of(2018, 3, 16, 9, 5),
            "Breakfast", 500);
    public static final Meal MEAL_2 = new Meal(MEAL_ID_2,
            LocalDateTime.of(2018, 3, 16, 12, 11),
            "Lunch", 1000);
    public static final Meal MEAL_3 = new Meal(MEAL_ID_3,
            LocalDateTime.of(2018, 3, 16, 18, 28),
            "Dinner", 300);
    public static final Meal MEAL_4 = new Meal(MEAL_ID_4,
            LocalDateTime.of(2018, 3, 17, 8, 37),
            "Breakfast", 500);
    public static final Meal MEAL_5 = new Meal(MEAL_ID_5,
            LocalDateTime.of(2018, 3, 17, 11, 48),
            "Lunch", 1000);
    public static final Meal MEAL_6 = new Meal(MEAL_ID_6,
            LocalDateTime.of(2018, 3, 17, 19, 0),
            "Dinner", 700);
    public static final Meal MEAL_ADMIN = new Meal(MEAL_ADMIN_ID,
            LocalDateTime.of(1900, 11, 12, 23, 24),
            "Stranger user meal", 123);

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields().isEqualTo(expected);
    }
}
