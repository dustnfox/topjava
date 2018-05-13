package ru.javawebinar.topjava.web.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.AuthorizedUser;
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
        return UserTo.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        String newEmail = ((UserTo) target).getEmail();
        String oldEmail = getLoggedUserEmail();
        if (oldEmail == null || !newEmail.equals(oldEmail)) {
            if (userRepository.getByEmail(newEmail) != null) {
                errors.rejectValue("email", "user.email.duplicate");
            }
        }
    }

    private String getLoggedUserEmail() {
        AuthorizedUser authorizedUser = AuthorizedUser.safeGet();
        if (authorizedUser != null) {
            UserTo userTo = authorizedUser.getUserTo();
            if (userTo != null) {
                return userTo.getEmail();
            }
        }
        return null;
    }
}
