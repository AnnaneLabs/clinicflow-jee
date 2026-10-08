package com.clinicmanager.mapper;

import com.clinicmanager.dto.UserDTO;
import com.clinicmanager.model.User;

import java.util.List;

/** Converts a User entity to a UserDTO. The password hash is never copied. */
public final class UserMapper {

    private UserMapper() { }

    public static UserDTO toDto(User user) {
        if (user == null) {
            return null;
        }
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.isActive());
    }

    public static List<UserDTO> toDtos(List<User> users) {
        return users.stream().map(UserMapper::toDto).toList();
    }
}