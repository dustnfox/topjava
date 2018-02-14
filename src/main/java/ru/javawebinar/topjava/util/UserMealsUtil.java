package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GKislin
 * 31.05.2015.
 */
public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        List<UserMealWithExceed> exceededMealList = getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
/*
        Here's an old implementation of calories a day calculation:

        Map<LocalDate, Integer> caloriesSumsByDays = new HashMap<>();
        mealList.forEach(m -> {
            LocalDate date = m.getDateTime().toLocalDate();
            caloriesSumsByDays.put(date, caloriesSumsByDays.getOrDefault(date, 0) + m.getCalories());
        });
*/

        final Map<LocalDate, Integer> caloriesSumsByDays = mealList.stream().collect(Collectors.groupingBy(
                m -> m.getDateTime().toLocalDate(),
                Collectors.summingInt(UserMeal::getCalories)
        ));

        return mealList.stream()
                .filter(m -> TimeUtil.isBetween(m.getDateTime().toLocalTime(), startTime, endTime))
                .map(m -> new UserMealWithExceed(
                        m.getDateTime(),
                        m.getDescription(),
                        m.getCalories(),
                        caloriesSumsByDays.get(m.getDateTime().toLocalDate()) > caloriesPerDay))
                .collect(Collectors.toList());
    }

    public static List<UserMealWithExceed> getFilteredWithExceededIterative(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<LocalDate, Integer> caloriesSumsByDays = new HashMap<>();

        for (UserMeal meal : mealList) {
            LocalDate date = meal.getDateTime().toLocalDate();
            caloriesSumsByDays.put(date, caloriesSumsByDays.getOrDefault(date, 0) + meal.getCalories());
        }

        List<UserMealWithExceed> exceedList = new ArrayList<>(mealList.size());
        for (UserMeal meal : mealList) {
            LocalTime mealTime = meal.getDateTime().toLocalTime();
            if (TimeUtil.isBetween(mealTime, startTime, endTime)) {
                LocalDate mealDate = meal.getDateTime().toLocalDate();
                UserMealWithExceed exceedMeal = new UserMealWithExceed(
                        meal.getDateTime(),
                        meal.getDescription(),
                        meal.getCalories(),
                        caloriesSumsByDays.get(mealDate) > caloriesPerDay);
                exceedList.add(exceedMeal);
            }
        }

        return exceedList;
    }
}
