package com.clinicmanager.dto;

import com.clinicmanager.model.Role;

import java.io.Serial;
import java.io.Serializable;

/**
 * Safe view of a user account: what the session and the JSPs may see.
 * Deliberately has NO password hash. Serializable because it is stored in the HttpSession.
 */
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final Role role;
    private final boolean active;

    public UserDTO(Long id, String firstName, String lastName, String email,
                   String phone, Role role, boolean active) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.active = active;
    }

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public Role getRole() { return role; }
    public boolean isActive() { return active; }

    /** Convenience for the pages: ${user.fullName} */
    public String getFullName() { return firstName + " " + lastName; }
}