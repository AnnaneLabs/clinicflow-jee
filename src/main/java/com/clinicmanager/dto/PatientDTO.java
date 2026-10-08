package com.clinicmanager.dto;

import com.clinicmanager.model.BloodType;
import com.clinicmanager.model.Gender;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

/** Patient profile merged with the account data (name, email, phone). */
public class PatientDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long id;
    private final Long userId;
    private final String firstName;
    private final String lastName;
    private final String email;
    private final String phone;
    private final String cin;
    private final LocalDate birthDate;
    private final Gender gender;
    private final String address;
    private final BloodType bloodType;

    public PatientDTO(Long id, Long userId, String firstName, String lastName, String email,
                      String phone, String cin, LocalDate birthDate, Gender gender,
                      String address, BloodType bloodType) {
        this.id = id;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.cin = cin;
        this.birthDate = birthDate;
        this.gender = gender;
        this.address = address;
        this.bloodType = bloodType;
    }

    public Long getId() { return id; }
    public Long getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getCin() { return cin; }
    public LocalDate getBirthDate() { return birthDate; }
    public Gender getGender() { return gender; }
    public String getAddress() { return address; }
    public BloodType getBloodType() { return bloodType; }

    public String getFullName() { return firstName + " " + lastName; }
}