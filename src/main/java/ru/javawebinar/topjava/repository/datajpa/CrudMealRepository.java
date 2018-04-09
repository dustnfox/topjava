package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId")
    int deleteByIdAndUser(@Param("id") Integer id, @Param("userId") Integer userId);

    Meal findByIdAndUserId(Integer id, Integer userId);

    @Query("SELECT m FROM Meal m JOIN FETCH m.user WHERE m.id=:id AND m.user.id=:userId")
    Optional<Meal> findByIdWithUser(@Param("id") Integer id, @Param("userId") Integer userId);

    List<Meal> findAllByUserIdOrderByDateTimeDesc(Integer userId);

    List<Meal> findAllByUserIdAndDateTimeBetweenOrderByDateTimeDesc(Integer userId,
                                                                    LocalDateTime startDate,
                                                                    LocalDateTime endDate);
}
