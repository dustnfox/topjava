package ru.javawebinar.topjava.web.meal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;
import static ru.javawebinar.topjava.util.Util.orElse;
import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;

@Controller
public class JspMealController {

    @Autowired
    MealService service;

    @GetMapping(value = "meals")
    public String getAll(Model model) {
        int userId = AuthorizedUser.id();
        List<MealWithExceed> mealWithExceedList =
                MealsUtil.getWithExceeded(service.getAll(userId), AuthorizedUser.getCaloriesPerDay());
        model.addAttribute("meals", mealWithExceedList);
        return "meals";
    }

    @GetMapping(value = "/meals/delete")
    public String delete(HttpServletRequest request) {
        service.delete(Integer.parseInt(request.getParameter("id")), AuthorizedUser.id());
        return "redirect:/meals/";
    }

    @GetMapping(value = "/meals/update")
    public String update(Model model, HttpServletRequest request) {
        String idStr = request.getParameter("id");
        final Meal meal = service.get(Integer.parseInt(idStr), AuthorizedUser.id());
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping(value = "/meals/create")
    public String create(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @PostMapping(value = "/meals/filter")
    public String filter(Model model, HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        List<Meal> mealsDateFiltered = service.getBetweenDates(
                orElse(startDate, DateTimeUtil.MIN_DATE),
                orElse(endDate, DateTimeUtil.MAX_DATE),
                AuthorizedUser.id());
        List<MealWithExceed> mealsWithExceeds = MealsUtil.getFilteredWithExceeded(mealsDateFiltered,
                AuthorizedUser.getCaloriesPerDay(),
                orElse(startTime, LocalTime.MIN),
                orElse(endTime, LocalTime.MAX));
        model.addAttribute("meals", mealsWithExceeds);

        return "meals";
    }

    @PostMapping(value = "/meals/save")
    public String save(HttpServletRequest request) {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        int userId = AuthorizedUser.id();
        if (request.getParameter("id").isEmpty()) {
            service.create(meal, userId);
        } else {
            assureIdConsistent(meal, Integer.parseInt(request.getParameter("id")));
            service.update(meal, userId);
        }
        return "redirect:/meals/";
    }
}
