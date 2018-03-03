package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.util.InitMealsList;
import ru.javawebinar.topjava.exceptions.MealNonExistException;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.storage.MapMealStorage;
import ru.javawebinar.topjava.storage.Storage;
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

        String idParam = request.getParameter("id");
        String description = request.getParameter("description");
        String localDateTimeParam = request.getParameter("datetime");
        String caloriesParam = request.getParameter("calories");

        try {
            LocalDateTime localDateTime = TimeUtil.getLocalDateTimeFromHTML(localDateTimeParam);
            int calories = Integer.parseInt(caloriesParam);
            if (idParam.isEmpty()) {
                storage.add(new Meal(storage.getNextId(), localDateTime, description, calories));
            } else {
                storage.add(new Meal(Integer.parseInt(idParam), localDateTime, description, calories));
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
        int id;

        switch (action) {
            case "delete":
                id = getIdFromRequest(request.getParameter("id"));
                if (id != -1) {
                    try {
                        storage.delete(id);
                    } catch (MealNonExistException e) {
                        log.info("Cannot delete meal with ID " + id + " Not found.");
                    }
                }
                request.setAttribute("meals", getExceededList(InitMealsList.CALORIES_THRESHOLD));
                forwardUrl = LIST_URL;
                break;

            case "edit":
                id = getIdFromRequest(request.getParameter("id"));
                if (id != -1) {
                    try {
                        Meal meal = storage.get(id);
                        request.setAttribute("meal", meal);
                    } catch (MealNonExistException e) {
                        log.info("Cannot find meal with UUID " + id + " Meal will be created instead of editing");
                    }
                }
                forwardUrl = EDIT_URL;
                break;

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

    private int getIdFromRequest(String idParameter) {
        int id = -1;
        try {
            id = Integer.parseInt(idParameter);
        } catch (NullPointerException e) {
            log.info("Cannot parse id parameter. [" + idParameter + "] " + e.getMessage());
        }

        return id;
    }

}
