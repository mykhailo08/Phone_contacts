package com.azarovmykhailo.phonecontacts.mapper;

import com.azarovmykhailo.phonecontacts.dto.UserDTO;
import com.azarovmykhailo.phonecontacts.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserMapperTest {

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testConvertToEntity() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("testUser");
        userDTO.setPassword("testPassword");
        when(passwordEncoder.encode(userDTO.getPassword())).thenReturn("encodedPassword");

        User result = UserMapper.convertToEntity(userDTO);

        assertEquals(userDTO.getLogin(), result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder).encode(userDTO.getPassword());
    }

    @Test
    void testConvertToDTO() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("testPassword");
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");

        UserDTO result = UserMapper.convertToDTO(user);

        assertEquals(user.getUsername(), result.getLogin());
        assertEquals("encodedPassword", result.getPassword());
        verify(passwordEncoder).encode(user.getPassword());
    }
}