package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepositoryImpl implements UserRepository {
    private final Map<Integer, User> map = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(0);

    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);
    private static final Comparator<User> USER_COMPARATOR =
            Comparator.comparing(User::getName).thenComparing(User::getId);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return map.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if(user.isNew()) {
            user.setId(nextId.incrementAndGet());
            map.put(user.getId(), user);
            return user;
        }
        return map.computeIfPresent(user.getId(), (key, value) -> user);
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return map.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
/*
        List<User> usersList = new ArrayList<>(map.values());
        usersList.sort(Comparator.comparing(User::getName));
        return usersList;
*/

        return map.values().stream()
                .sorted(USER_COMPARATOR)
                .collect(Collectors.toList());
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
/*
        for(User u : map.values()) {
            if(u.getEmail() != null && email.equals(u.getEmail())) {
                return u;
            }
        }
        return null;
*/

        return map.values().stream()
                .filter(u -> u.getEmail() != null && u.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }
}
