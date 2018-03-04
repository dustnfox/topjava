package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.storage.MapMealStorage;
import ru.javawebinar.topjava.storage.Storage;
import ru.javawebinar.topjava.util.InitMealsList;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.TimeUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);
    private static Storage storage;
    private static final String EDIT_URL = "WEB-INF/jsp/editMeal.jsp";
    private static final String LIST_URL = "WEB-INF/jsp/listMeals.jsp";
    private static final String MEALS_URL = "meals";

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        storage = new MapMealStorage();
        InitMealsList.addMealsData(storage);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String id = request.getParameter("id");
        String description = request.getParameter("description");
        String localDateTimeParam = request.getParameter("datetime");
        String caloriesParam = request.getParameter("calories");

        try {
            LocalDateTime localDateTime = TimeUtil.getLocalDateTimeFromHTML(localDateTimeParam);
            int calories = Integer.parseInt(caloriesParam);
            if (id.isEmpty()) {
                storage.add(new Meal(localDateTime, description, calories));
            } else {
                storage.add(new Meal(Integer.parseInt(id), localDateTime, description, calories));
            }
        } catch (DateTimeParseException e) {
            log.info("Cannot parse date string [" + localDateTimeParam + "]. Meal will not be created/updated.");
        } catch (NumberFormatException e) {
            log.info("Cannot parse int from string. Meal object will not be created/updated.");
        }
        response.sendRedirect(MEALS_URL);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");

        String forwardUrl;
        String action = request.getParameter("action");
        action = action == null ? "" : action.toLowerCase().trim();
        String id = request.getParameter("id");

        switch (action) {
            case "delete":
                try {
                    storage.delete(Integer.parseInt(id));
                } catch (NumberFormatException | NullPointerException e) {
                    log.info(String.format("Cannot parse id from given parameter %s in %s request.", id, action));
                }
                response.sendRedirect(MEALS_URL);
                return;

            case "edit":
                try {
                    Meal meal = storage.get(Integer.parseInt(id));
                    request.setAttribute("meal", meal);
                } catch (NumberFormatException | NullPointerException e) {
                    log.info(String.format("Cannot parse id from given parameter %s in %s request.", id, action));
                }

            case "create":
                forwardUrl = EDIT_URL;
                break;

            default:
                request.setAttribute("meals", getExceededList(InitMealsList.CALORIES_THRESHOLD));
                forwardUrl = LIST_URL;
        }

        request.getRequestDispatcher(forwardUrl).forward(request, response);
    }

    private List<MealWithExceed> getExceededList(int caloriesThreshold) {
        return MealsUtil.getFilteredWithExceeded(storage.getAll(), LocalTime.MIN, LocalTime.MAX, caloriesThreshold);
    }
}
