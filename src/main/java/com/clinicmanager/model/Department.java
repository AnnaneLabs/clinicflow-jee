package com.clinicmanager.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "departments")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(mappedBy = "department")
    private List<Specialty> specialties = new ArrayList<>();

    public Department() { }

    public Department(String name) { this.name = name; }

    public void addSpecialty(Specialty specialty) {
        specialties.add(specialty);
        specialty.setDepartment(this);
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public List<Specialty> getSpecialties() { return specialties; }
}