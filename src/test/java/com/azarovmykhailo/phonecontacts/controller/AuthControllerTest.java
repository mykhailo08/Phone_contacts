package com.azarovmykhailo.phonecontacts.controller;

import com.azarovmykhailo.phonecontacts.dto.UserDTO;
import com.azarovmykhailo.phonecontacts.security.JwtTokenProvider;
import com.azarovmykhailo.phonecontacts.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthControllerTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserService userService;

    private AuthController authController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        authController = new AuthController(authenticationManager, jwtTokenProvider, userService);
    }

    @Test
    void testLoginWithValidCredentials() {

        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("testuser");
        userDTO.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(null);
        when(jwtTokenProvider.generateToken(userDTO)).thenReturn("mocked_token");

        ResponseEntity<String> response = authController.login(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mocked_token", response.getBody());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(1)).generateToken(userDTO);
    }

    @Test
    void testLoginWithDisabledUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("testuser");
        userDTO.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new DisabledException("User account is disabled"));

        ResponseEntity<String> response = authController.login(userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("User account is disabled", response.getBody());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testLoginWithInvalidCredentials() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("testuser");
        userDTO.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new BadCredentialsException("Invalid credentials"));

        ResponseEntity<String> response = authController.login(userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid credentials", response.getBody());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testLoginWithAuthenticationFailure() {
        UserDTO userDTO = new UserDTO();
        userDTO.setLogin("testuser");
        userDTO.setPassword("password");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenThrow(new AuthenticationException("Authentication failed") {});

        ResponseEntity<String> response = authController.login(userDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Authentication failed", response.getBody());

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void testRegisterUser() {
        UserDTO userDTO = new UserDTO();

        ResponseEntity<String> response = authController.registerUser(userDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration completed successfully!", response.getBody());

        verify(userService, times(1)).registerUser(userDTO);
    }
}