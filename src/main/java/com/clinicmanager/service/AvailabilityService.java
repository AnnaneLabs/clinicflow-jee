package com.clinicmanager.service;

import com.clinicmanager.model.Availability;
import com.clinicmanager.model.AvailabilityStatus;
import com.clinicmanager.model.Doctor;
import com.clinicmanager.repository.AvailabilityRepository;
import com.clinicmanager.repository.DoctorRepository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final DoctorRepository doctorRepository;

    public AvailabilityService() {
        this.availabilityRepository = new AvailabilityRepository();
        this.doctorRepository = new DoctorRepository();
    }

    public List<Availability> getDoctorAvailabilities(Long doctorId) {
        return availabilityRepository.findByDoctorId(doctorId);
    }

    public Optional<Doctor> getDoctorByUserId(Long userId) {
        return doctorRepository.findByUserId(userId);
    }

    public Availability createAvailability(Long doctorId, DayOfWeek dayOfWeek,
                                            LocalTime startTime, LocalTime endTime,
                                            LocalDate validFrom, LocalDate validTo) {
        if (startTime.isAfter(endTime) || startTime.equals(endTime)) {
            throw new IllegalArgumentException("Start time must be before end time.");
        }

        if (validFrom == null) {
            validFrom = LocalDate.now();
        }

        if (validTo != null && validTo.isBefore(validFrom)) {
            throw new IllegalArgumentException("Valid To date cannot be before Valid From date.");
        }

        if (availabilityRepository.existsOverlapping(doctorId, dayOfWeek, startTime, endTime, null)) {
            throw new IllegalArgumentException("This availability time slot overlaps with an existing schedule block for " + dayOfWeek + ".");
        }

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor not found with ID: " + doctorId));

        Availability availability = new Availability();
        availability.setDoctor(doctor);
        availability.setDayOfWeek(dayOfWeek);
        availability.setStartTime(startTime);
        availability.setEndTime(endTime);
        availability.setStatus(AvailabilityStatus.ACTIVE);
        availability.setValidFrom(validFrom);
        availability.setValidTo(validTo);

        return availabilityRepository.save(availability);
    }

    public void deleteAvailability(Long availabilityId, Long doctorId) {
        Availability availability = availabilityRepository.findById(availabilityId)
                .orElseThrow(() -> new IllegalArgumentException("Availability slot not found."));

        if (!availability.getDoctor().getId().equals(doctorId)) {
            throw new IllegalArgumentException("You are not authorized to delete this availability slot.");
        }

        availabilityRepository.deleteById(availabilityId);
    }
}
