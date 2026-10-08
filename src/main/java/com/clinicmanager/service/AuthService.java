package com.clinicmanager.service;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.exception.AccountDisabledException;
import com.clinicmanager.exception.DuplicateEmailException;
import com.clinicmanager.exception.InvalidCredentialsException;
import com.clinicmanager.exception.UnauthorizedActionException;
import com.clinicmanager.exception.UserNotFoundException;
import com.clinicmanager.exception.ValidationException;
import com.clinicmanager.mapper.UserMapper;
import com.clinicmanager.model.Role;
import com.clinicmanager.model.User;
import com.clinicmanager.repository.UserRepository;
import jakarta.persistence.PersistenceException;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Locale;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Authentication and account rules: registration, login, profile, password change.
 * Business logic lives here; data access is delegated to UserRepository.
 */
public class AuthService {

    static final int MIN_PASSWORD_LENGTH = 6;
    /** BCrypt only uses the first 72 bytes; longer passwords would be silently truncated. */
    static final int MAX_PASSWORD_LENGTH = 72;
    private static final int BCRYPT_COST = 10;

    private static final Pattern EMAIL = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");

    /** Used to spend the same time on login even when the email does not exist. */
    private static final String DUMMY_HASH =
            BCrypt.hashpw("dummy-password", BCrypt.gensalt(BCRYPT_COST));

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
        user.setPasswordHash(hash(password));
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

        // Always run one BCrypt check, so an unknown email takes as long as a wrong password.
        String hashToCheck = found.map(User::getPasswordHash).orElse(DUMMY_HASH);
        boolean passwordMatches = BCrypt.checkpw(candidate, hashToCheck);

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

        if (currentPassword == null || !BCrypt.checkpw(currentPassword, user.getPasswordHash())) {
            throw new UnauthorizedActionException("Current password is incorrect.");
        }
        validatePassword(newPassword);

        user.setPasswordHash(hash(newPassword));
        userRepository.update(user);
    }

    // ------------------------------------------------------------------ helpers

    private User loadUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    private static String requireText(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new ValidationException(fieldName + " is required.");
        }
        return value.trim();
    }

    /** Trim and lowercase, so "Sara@X.com" and "sara@x.com" are the same account. */
    private static String normalizeEmail(String email) {
        String clean = requireText(email, "Email").toLowerCase(Locale.ROOT);
        if (!EMAIL.matcher(clean).matches()) {
            throw new ValidationException("Invalid email address.");
        }
        return clean;
    }

    private static void validatePassword(String password) {
        if (password == null || password.length() < MIN_PASSWORD_LENGTH) {
            throw new ValidationException(
                    "Password must be at least " + MIN_PASSWORD_LENGTH + " characters.");
        }
        if (password.length() > MAX_PASSWORD_LENGTH) {
            throw new ValidationException(
                    "Password must be at most " + MAX_PASSWORD_LENGTH + " characters.");
        }
    }

    private static String hash(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_COST));
    }

    private static String blankToNull(String value) {
        return (value == null || value.isBlank()) ? null : value.trim();
    }
}