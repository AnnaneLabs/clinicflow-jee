package com.clinicmanager.service;

import com.clinicmanager.model.*;
import com.clinicmanager.repository.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final DoctorRepository doctorRepository;
    private final PatientRepository patientRepository;
    private final AvailabilityRepository availabilityRepository;
    private final AbsenceRepository absenceRepository;

    public AppointmentService() {
        this.appointmentRepository = new AppointmentRepository();
        this.doctorRepository = new DoctorRepository();
        this.patientRepository = new PatientRepository();
        this.availabilityRepository = new AvailabilityRepository();
        this.absenceRepository = new AbsenceRepository();
    }

    public List<Appointment> getDoctorAppointments(Long doctorId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime from = now.minusMonths(6);
        LocalDateTime to = now.plusMonths(6);
        return appointmentRepository.findByDoctorAndPeriod(doctorId, from, to);
    }

    public List<Appointment> getPatientAppointments(Long patientId) {
        return appointmentRepository.findByPatientId(patientId);
    }

    public Optional<Doctor> getDoctorByUserId(Long userId) {
        return doctorRepository.findByUserId(userId);
    }

    public Optional<Patient> getPatientByUserId(Long userId) {
        return patientRepository.findByUserId(userId);
    }

    /**
     * Calculates available 30-minute time slots for a given doctor on a date.
     * Checks working availabilities, doctor absences, and existing bookings.
     */
    public List<LocalTime> getAvailableTimeSlots(Long doctorId, LocalDate date) {
        List<LocalTime> availableSlots = new ArrayList<>();

        // 1. Check if doctor is absent on date
        if (absenceRepository.isDoctorAbsentOnDate(doctorId, date)) {
            return availableSlots; // Empty: doctor is on leave
        }

        // 2. Find active availability blocks for that day of week
        List<Availability> availabilities = availabilityRepository.findActiveByDoctorAndDay(
                doctorId, date.getDayOfWeek(), date);

        if (availabilities.isEmpty()) {
            return availableSlots; // No working hours configured
        }

        // 3. Get existing booked appointments on that date
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(23, 59, 59);
        List<Appointment> bookedAppointments = appointmentRepository.findActiveByDoctorAndPeriod(
                doctorId, startOfDay, endOfDay);

        // 4. Generate 30-minute slots for each availability block
        for (Availability avail : availabilities) {
            LocalTime time = avail.getStartTime();
            LocalTime blockEnd = avail.getEndTime();

            while (time.plusMinutes(30).isBefore(blockEnd) || time.plusMinutes(30).equals(blockEnd)) {
                LocalTime slotStart = time;
                LocalTime slotEnd = time.plusMinutes(30);

                LocalDateTime startDt = date.atTime(slotStart);
                LocalDateTime endDt = date.atTime(slotEnd);

                // Check conflict with booked appointments
                boolean isBooked = bookedAppointments.stream().anyMatch(app ->
                        app.getStartTime().isBefore(endDt) && app.getEndTime().isAfter(startDt));

                if (!isBooked) {
                    availableSlots.add(slotStart);
                }

                time = slotEnd;
            }
        }

        return availableSlots;
    }

    public Appointment bookAppointment(Long patientId, Long doctorId, LocalDate date, LocalTime slotTime,
                                       AppointmentType type, String reason) {
        if (date == null || slotTime == null) {
            throw new IllegalArgumentException("Date and time slot are required.");
        }

        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Cannot book an appointment for a past date.");
        }

        LocalDateTime startTime = date.atTime(slotTime);
        LocalDateTime endTime = startTime.plusMinutes(30);

        if (absenceRepository.isDoctorAbsentOnDate(doctorId, date)) {
            throw new IllegalArgumentException("Doctor is absent on the selected date.");
        }

        if (appointmentRepository.existsOverlapping(doctorId, startTime, endTime, null)) {
            throw new IllegalArgumentException("This time slot has already been booked by another patient.");
        }

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new IllegalArgumentException("Patient profile not found."));

        Doctor doctor = doctorRepository.findById(doctorId)
                .orElseThrow(() -> new IllegalArgumentException("Doctor profile not found."));

        Appointment appointment = new Appointment();
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setStartTime(startTime);
        appointment.setEndTime(endTime);
        appointment.setType(type != null ? type : AppointmentType.CONSULTATION);
        appointment.setStatus(AppointmentStatus.PLANNED);
        appointment.setReason(reason != null ? reason.trim() : "");

        return appointmentRepository.save(appointment);
    }

    public void updateStatus(Long appointmentId, Long doctorId, AppointmentStatus newStatus) {
        Appointment appointment = appointmentRepository.findByIdWithDetails(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Appointment not found."));

        if (!appointment.getDoctor().getId().equals(doctorId)) {
            throw new IllegalArgumentException("You are not authorized to update this appointment.");
        }

        appointment.setStatus(newStatus);
        appointmentRepository.update(appointment);
    }
}
