package com.clinicmanager.service;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.exception.AccountDisabledException;
import com.clinicmanager.exception.DuplicateEmailException;
import com.clinicmanager.exception.InvalidCredentialsException;
import com.clinicmanager.exception.UnauthorizedActionException;
import com.clinicmanager.exception.UserNotFoundException;
import com.clinicmanager.mapper.UserMapper;
import com.clinicmanager.model.Role;
import com.clinicmanager.model.User;
import com.clinicmanager.repository.UserRepository;
import com.clinicmanager.util.PasswordHasher;
import jakarta.persistence.PersistenceException;

import java.util.Locale;
import java.util.Optional;

import static com.clinicmanager.util.InputRules.blankToNull;
import static com.clinicmanager.util.InputRules.normalizeEmail;
import static com.clinicmanager.util.InputRules.requireText;
import static com.clinicmanager.util.InputRules.validatePassword;

/**
 * Authentication and account rules: registration, login, profile, password change.
 * Business logic lives here; data access is delegated to UserRepository.
 */
public class AuthService {

    private final UserRepository userRepository;

    public AuthService() {
        this(new UserRepository());
    }

    /** Constructor injection: tests can pass a mocked repository. */
    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // ------------------------------------------------------------------ register

    public UserDTO register(String firstName, String lastName, String email,
                            String phone, String password) {
        String cleanFirstName = requireText(firstName, "First name");
        String cleanLastName = requireText(lastName, "Last name");
        String cleanEmail = normalizeEmail(email);
        validatePassword(password);

        if (userRepository.existsByEmail(cleanEmail)) {
            throw new DuplicateEmailException(cleanEmail);
        }

        User user = new User();
        user.setFirstName(cleanFirstName);
        user.setLastName(cleanLastName);
        user.setEmail(cleanEmail);
        user.setPhone(blankToNull(phone));
        user.setPasswordHash(PasswordHasher.hash(password));
        user.setRole(Role.PATIENT);
        user.setActive(true);

        try {
            userRepository.save(user);
        } catch (PersistenceException e) {
            // Two requests can pass the check above at the same time; the database
            // unique constraint then rejects the second one. Report it as a duplicate.
            if (userRepository.existsByEmail(cleanEmail)) {
                throw new DuplicateEmailException(cleanEmail);
            }
            throw e;
        }
        return UserMapper.toDto(user);
    }

    // ------------------------------------------------------------------ login

    public UserDTO login(String email, String password) {
        String cleanEmail = email == null ? "" : email.trim().toLowerCase(Locale.ROOT);
        String candidate = password == null ? "" : password;

        Optional<User> found = userRepository.findByEmail(cleanEmail);

        // With no user, matches() still spends one BCrypt check and returns false.
        boolean passwordMatches = PasswordHasher.matches(
                candidate, found.map(User::getPasswordHash).orElse(null));

        // Same error for "no such email" and "wrong password": no user enumeration.
        if (found.isEmpty() || !passwordMatches) {
            throw new InvalidCredentialsException();
        }

        User user = found.get();
        // Checked only AFTER the password is proven, so strangers cannot probe account status.
        if (!user.isActive()) {
            throw new AccountDisabledException();
        }
        return UserMapper.toDto(user);
    }

    // ------------------------------------------------------------------ profile

    public UserDTO getProfile(Long userId) {
        return UserMapper.toDto(loadUser(userId));
    }

    public UserDTO updateProfile(Long userId, String firstName, String lastName, String phone) {
        User user = loadUser(userId);
        user.setFirstName(requireText(firstName, "First name"));
        user.setLastName(requireText(lastName, "Last name"));
        user.setPhone(blankToNull(phone));
        return UserMapper.toDto(userRepository.update(user));
    }

    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = loadUser(userId);

        if (currentPassword == null
                || !PasswordHasher.matches(currentPassword, user.getPasswordHash())) {
            throw new UnauthorizedActionException("Current password is incorrect.");
        }
        validatePassword(newPassword);

        user.setPasswordHash(PasswordHasher.hash(newPassword));
        userRepository.update(user);
    }

    private User loadUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }
}
