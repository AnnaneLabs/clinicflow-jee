package com.clinicmanager.mapper;

import com.clinicmanager.dto.DoctorDTO;
import com.clinicmanager.model.Doctor;
import com.clinicmanager.model.Specialty;
import com.clinicmanager.model.User;

import java.util.List;

/**
 * Converts a Doctor entity to a DoctorDTO.
 * The doctor's user, specialty and the specialty's department must already be loaded
 * (DoctorRepository.findByIdWithDetails / findAllWithDetails / findBySpecialtyId do that).
 */
public final class DoctorMapper {

    private DoctorMapper() { }

    public static DoctorDTO toDto(Doctor doctor) {
        if (doctor == null) {
            return null;
        }
        User user = doctor.getUser();
        Specialty specialty = doctor.getSpecialty();
        return new DoctorDTO(
                doctor.getId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.isActive(),
                doctor.getMatricule(),
                doctor.getTitle(),
                specialty.getId(),
                specialty.getName(),
                specialty.getDepartment().getName());
    }

    public static List<DoctorDTO> toDtos(List<Doctor> doctors) {
        return doctors.stream().map(DoctorMapper::toDto).toList();
    }
}