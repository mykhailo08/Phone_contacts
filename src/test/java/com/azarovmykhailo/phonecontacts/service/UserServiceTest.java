package com.azarovmykhailo.phonecontacts.service;

import com.azarovmykhailo.phonecontacts.dto.UserDTO;
import com.azarovmykhailo.phonecontacts.entity.User;
import com.azarovmykhailo.phonecontacts.mapper.UserMapper;
import com.azarovmykhailo.phonecontacts.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserMapper userMapper;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userMapper = new UserMapper(passwordEncoder);
        userService = new UserService(userRepository);
    }

    @Test
    void testRegisterUserValidUserData() {
        
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("testUser");
        userDTO.setPassword("testPassword");
        User user = userMapper.convertToEntity(userDTO);
        when(userRepository.findByUsername(userDTO.getLogin())).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        
        assertDoesNotThrow(() -> userService.registerUser(userDTO));

        
        verify(userRepository).findByUsername(userDTO.getLogin());
    }

    @Test
    void testGetUserByUsernameUserExists() {
        
        String username = "testUser";
        User user = new User();
        when(userRepository.findByUsername(username)).thenReturn(user);

        
        User result = userService.getUserByUsername(username);

        
        assertEquals(user, result);
        verify(userRepository).findByUsername(username);
    }

    @Test
    void testGetUserByUsernameUserNotFound() {
        
        String username = "nonexistentUser";
        when(userRepository.findByUsername(username)).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.getUserByUsername(username));
        assertEquals("User not found", exception.getMessage());

        verify(userRepository).findByUsername(username);
    }
}