package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.javawebinar.topjava.InitMealsList;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalTime;
import java.util.List;

public class MealServlet extends HttpServlet {
    private static final Logger log = LoggerFactory.getLogger(MealServlet.class);

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String caloriesParam = request.getParameter("calories");
        int caloriesPerDay = Integer.MAX_VALUE;
        if(caloriesParam != null) {
            try {
                caloriesPerDay = Integer.parseInt(caloriesParam);
            } catch(NumberFormatException e) {
                log.debug("Can't parse param 'calories' into Integer. Passed value: " + caloriesParam);
            }
        }
        List<MealWithExceed> exceedList = MealsUtil.getFilteredWithExceeded(InitMealsList.getMeals(), LocalTime.MIN, LocalTime.MAX, caloriesPerDay);
        request.setAttribute("meals", exceedList);
        request.getRequestDispatcher("/meals.jsp").forward(request, response);
    }
}
