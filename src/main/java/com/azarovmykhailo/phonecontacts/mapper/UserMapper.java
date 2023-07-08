package com.azarovmykhailo.phonecontacts.mapper;

import com.azarovmykhailo.phonecontacts.dto.UserDTO;
import com.azarovmykhailo.phonecontacts.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    private static PasswordEncoder passwordEncoder;

    public UserMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder=passwordEncoder;
    }

    public static User convertToEntity(UserDTO userDTO) {

        User user = new User();
        user.setUsername(userDTO.getLogin());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));

        return user;
    }

    public static UserDTO convertToDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin(user.getUsername());
        userDTO.setPassword(passwordEncoder.encode(user.getPassword()));

        return userDTO;
    }
}

