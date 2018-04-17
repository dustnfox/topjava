package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping(value = "/meals")
public class MealJspController extends AbstractMealController {

    @GetMapping(value = {"", "/"})
    public String doGetAll(Model model) {
        model.addAttribute("meals", doGetAll());
        return "meals";
    }

    @GetMapping(value = "/delete")
    public String doDelete(HttpServletRequest request) {
        doDelete(getId(request));
        return "redirect:/meals/";
    }

    @GetMapping(value = "/update")
    public String update(Model model, HttpServletRequest request) {
        final Meal meal = service.get(getId(request), AuthorizedUser.id());
        model.addAttribute("meal", meal);
        model.addAttribute("isNew", false);
        return "mealForm";
    }

    @GetMapping(value = "/create")
    public String create(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        model.addAttribute("isNew", true);
        return "mealForm";
    }

    @PostMapping(value = "/filter")
    public String filter(Model model, HttpServletRequest request) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));

        model.addAttribute("meals",
                doGetBetween(startDate, startTime, endDate, endTime));
        return "meals";
    }

    @PostMapping(value = "/save")
    public String save(HttpServletRequest request) throws UnsupportedEncodingException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));
        if (request.getParameter("id").isEmpty()) {
            doCreate(meal);
        } else {
            doUpdate(meal, getId(request));
        }
        return "redirect:/meals/";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}
