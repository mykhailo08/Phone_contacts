package com.azarovmykhailo.phonecontacts.service;

import com.azarovmykhailo.phonecontacts.dto.UserDTO;
import com.azarovmykhailo.phonecontacts.entity.User;
import com.azarovmykhailo.phonecontacts.exception.CustomException;
import com.azarovmykhailo.phonecontacts.mapper.UserMapper;
import com.azarovmykhailo.phonecontacts.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(UserDTO userDTO) {
        validateUserData(userDTO);
        User existingUser = userRepository.findByUsername(userDTO.getLogin());
        if (existingUser != null) {
            throw new CustomException(HttpStatus.NOT_ACCEPTABLE,"Username already exists");
        }
        User user = UserMapper.convertToEntity(userDTO);
        userRepository.save(user);
    }

    public User getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    private void validateUserData(UserDTO userDTO) {
        String password = userDTO.getPassword();
        if (password.length() < 6 || password.length() > 20) {
            throw new CustomException(HttpStatus.BAD_REQUEST,"Password must be between 6 and 20 characters");
        }
    }
}
