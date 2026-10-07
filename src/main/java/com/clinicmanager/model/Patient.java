package com.clinicmanager.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "patients")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false, unique = true, length = 20)
    private String cin;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 10)
    private Gender gender;

    @Column(length = 255)
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "blood_type", length = 15)
    private BloodType bloodType;

    public Patient() { }

    // + getters and setters (IntelliJ: Alt+Insert > Getter and Setter)
    // no setId: the database owns the id


    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getCin() {
        return cin;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Gender getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }
}