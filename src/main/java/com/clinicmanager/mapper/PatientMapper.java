package com.clinicmanager.mapper;

import com.clinicmanager.dto.PatientDTO;
import com.clinicmanager.model.Patient;
import com.clinicmanager.model.User;

import java.util.List;

/**
 * Converts a Patient entity to a PatientDTO.
 * The patient's user must already be loaded (use the repository methods with JOIN FETCH),
 * otherwise reading it here throws LazyInitializationException.
 */
public final class PatientMapper {

    private PatientMapper() { }

    public static PatientDTO toDto(Patient patient) {
        if (patient == null) {
            return null;
        }
        User user = patient.getUser();
        return new PatientDTO(
                patient.getId(),
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                patient.getCin(),
                patient.getBirthDate(),
                patient.getGender(),
                patient.getAddress(),
                patient.getBloodType());
    }

    public static List<PatientDTO> toDtos(List<Patient> patients) {
        return patients.stream().map(PatientMapper::toDto).toList();
    }
}