package com.clinicmanager.service;

import com.clinicmanager.dto.PatientDTO;
import com.clinicmanager.exception.DuplicateCinException;
import com.clinicmanager.exception.PatientNotFoundException;
import com.clinicmanager.exception.UnauthorizedActionException;
import com.clinicmanager.exception.UserNotFoundException;
import com.clinicmanager.exception.ValidationException;
import com.clinicmanager.mapper.PatientMapper;
import com.clinicmanager.model.BloodType;
import com.clinicmanager.model.Gender;
import com.clinicmanager.model.Patient;
import com.clinicmanager.model.Role;
import com.clinicmanager.model.User;
import com.clinicmanager.repository.PatientRepository;
import com.clinicmanager.repository.UserRepository;
import jakarta.persistence.PersistenceException;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

import static com.clinicmanager.util.InputRules.blankToNull;
import static com.clinicmanager.util.InputRules.requireText;

/** Patient profile rules. The account itself (email, password) belongs to AuthService. */
public class PatientService {

    private final PatientRepository patientRepository;
    private final UserRepository userRepository;

    public PatientService() {
        this(new PatientRepository(), new UserRepository());
    }

    public PatientService(PatientRepository patientRepository, UserRepository userRepository) {
        this.patientRepository = patientRepository;
        this.userRepository = userRepository;
    }

    /** Completes the profile of a patient account. A booking requires this profile. */
    public PatientDTO createProfile(Long userId, String cin, LocalDate birthDate,
                                    Gender gender, String address, BloodType bloodType) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        if (user.getRole() != Role.PATIENT) {
            throw new UnauthorizedActionException("Only patient accounts can have a patient profile.");
        }

        String cleanCin = requireText(cin, "CIN").toUpperCase(Locale.ROOT);
        validateBirthDate(birthDate);

        if (patientRepository.findByUserId(userId).isPresent()) {
            throw new ValidationException("This account already has a patient profile.");
        }
        if (patientRepository.existsByCin(cleanCin)) {
            throw new DuplicateCinException(cleanCin);
        }

        Patient patient = new Patient();
        patient.setUser(user);
        patient.setCin(cleanCin);
        patient.setBirthDate(birthDate);
        patient.setGender(gender);
        patient.setAddress(blankToNull(address));
        patient.setBloodType(bloodType);

        try {
            patientRepository.save(patient);
        } catch (PersistenceException e) {
            if (patientRepository.existsByCin(cleanCin)) {
                throw new DuplicateCinException(cleanCin);
            }
            throw e;
        }
        return PatientMapper.toDto(patient);
    }

    public boolean hasProfile(Long userId) {
        return patientRepository.findByUserId(userId).isPresent();
    }

    public PatientDTO getProfile(Long userId) {
        return patientRepository.findByUserId(userId)
                .map(PatientMapper::toDto)
                .orElseThrow(() -> PatientNotFoundException.forUser(userId));
    }

    /** The CIN cannot be changed after creation; it identifies the person. */
    public PatientDTO updateProfile(Long userId, LocalDate birthDate, Gender gender,
                                    String address, BloodType bloodType) {
        Patient patient = patientRepository.findByUserId(userId)
                .orElseThrow(() -> PatientNotFoundException.forUser(userId));
        validateBirthDate(birthDate);

        patient.setBirthDate(birthDate);
        patient.setGender(gender);
        patient.setAddress(blankToNull(address));
        patient.setBloodType(bloodType);

        patientRepository.update(patient);
        // Map the object WE loaded (its user is fetched). The object returned by merge()
        // is a different instance whose user may be a lazy proxy.
        return PatientMapper.toDto(patient);
    }

    // ------------------------------------------------------------------ admin

    public PatientDTO getById(Long patientId) {
        return patientRepository.findByIdWithUser(patientId)
                .map(PatientMapper::toDto)
                .orElseThrow(() -> new PatientNotFoundException(patientId));
    }

    public List<PatientDTO> listAll() {
        return PatientMapper.toDtos(patientRepository.findAllWithUser());
    }

    private static void validateBirthDate(LocalDate birthDate) {
        if (birthDate != null && birthDate.isAfter(LocalDate.now())) {
            throw new ValidationException("Birth date cannot be in the future.");
        }
    }
}
