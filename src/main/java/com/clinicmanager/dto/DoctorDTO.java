package com.clinicmanager.dto;

import java.io.Serial;
import java.io.Serializable;

/** Doctor profile merged with account, specialty and department names. */
public class DoctorDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final boolean active;
    private final String matricule;
    private final String title;
    private final Long specialtyId;
    private final String specialtyName;
    private final String departmentName;

    public DoctorDTO(Long id, Long userId, String firstName, String lastName, String email,
                     String phone, boolean active, String matricule, String title,
                     Long specialtyId, String specialtyName, String departmentName) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.active = active;
        this.matricule = matricule;
        this.title = title;
        this.specialtyId = specialtyId;
        this.specialtyName = specialtyName;
        this.departmentName = departmentName;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public boolean isActive() { return active; }
    public String getMatricule() { return matricule; }
    public String getTitle() { return title; }
    public Long getSpecialtyId() { return specialtyId; }
    public String getSpecialtyName() { return specialtyName; }
    public String getDepartmentName() { return departmentName; }

    /** For the pages: "Dr. Sara Alami", or just "Sara Alami" when there is no title. */
    public String getDisplayName() {
        String prefix = (title == null || title.isBlank()) ? "" : title + " ";
        return prefix + firstName + " " + lastName;
    }
}