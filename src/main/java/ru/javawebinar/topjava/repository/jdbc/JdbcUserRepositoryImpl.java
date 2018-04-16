package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());

        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_roles r WHERE r.user_id=?", user.getId());
        }
        batchRolesUpdate(user.getRoles(), user.getId());
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return fillUserRoles(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return fillUserRoles(users);
    }

    @Override
    public List<User> getAll() {
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);

        Map<Integer, EnumSet<Role>> rolesMap = new HashMap<>();

        jdbcTemplate.query("SELECT * FROM user_roles", (rs, rowNum) -> {
                    Integer id = rs.getInt("user_id");
                    Role role = Role.valueOf(rs.getString("role"));
                    EnumSet<Role> roleSet = rolesMap.computeIfAbsent(id, k -> EnumSet.noneOf(Role.class));
                    roleSet.add(role);
                    return 1;
                }
        );
        users.forEach(u -> u.setRoles(rolesMap.get(u.getId())));
        return users;
    }

    private User fillUserRoles(List<User> users) {
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            List<Role> roles = jdbcTemplate.query(
                    "SELECT * FROM user_roles WHERE user_id=?",
                    (rs, rowNum) -> Role.valueOf(rs.getString("role")),
                    user.getId());
            user.setRoles(roles);
        }
        return user;
    }

    private void batchRolesUpdate(Set<Role> roles, int userId) {
        final Role[] rolesArray = new Role[roles.size()];
        roles.toArray(rolesArray);

        jdbcTemplate.batchUpdate("INSERT INTO user_roles VALUES (?,?)", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, userId);
                ps.setString(2, rolesArray[i].name());
            }

            @Override
            public int getBatchSize() {
                return rolesArray.length;
            }
        });
    }
}
