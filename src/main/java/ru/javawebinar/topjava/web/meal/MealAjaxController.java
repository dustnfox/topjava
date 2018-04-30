package ru.javawebinar.topjava.web.meal;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/ajax/meals")
public class MealAjaxController extends AbstractMealController {

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MealWithExceed> getAll(@RequestParam(value = "startDate", required = false) LocalDate startDate,
                                       @RequestParam(value = "startTime", required = false) LocalTime startTime,
                                       @RequestParam(value = "endDate", required = false) LocalDate endDate,
                                       @RequestParam(value = "endTime", required = false) LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime);
    }

    @Override
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") int id) {
        super.delete(id);
    }

    @PostMapping
    public void createOrUpdate(@RequestParam("id") Integer id,
                               @RequestParam("datetime") LocalDateTime dateTime,
                               @RequestParam("description") String description,
                               @RequestParam("calories") Integer calories) {

        Meal meal = new Meal(id, dateTime, description, calories);
        if (meal.isNew()) {
            super.create(meal);
        }
    }
}
