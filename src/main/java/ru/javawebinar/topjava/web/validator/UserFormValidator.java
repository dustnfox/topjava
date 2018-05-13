package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.HasId;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.datajpa.DataJpaUserRepositoryImpl;
import ru.javawebinar.topjava.to.UserTo;

@Component
public class UserFormValidator implements Validator {
    private final DataJpaUserRepositoryImpl userRepository;

    @Autowired
    public UserFormValidator(DataJpaUserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserTo.class.equals(clazz) || User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (isDuplicateEmail((HasId) target)) {
            errors.rejectValue("email", "user.email.duplicate", "user with the email already exists");
        }
    }

    private boolean isDuplicateEmail(HasId obj) {
        String email = obj instanceof UserTo ? ((UserTo) obj).getEmail() : ((User) obj).getEmail();
        User oldUser = userRepository.getByEmail(email);
        return oldUser != null && !oldUser.getId().equals(obj.getId());
    }
}
