package com.azarovmykhailo.phonecontacts.controller;

import com.azarovmykhailo.phonecontacts.dto.ContactDTO;
import com.azarovmykhailo.phonecontacts.security.JwtTokenProvider;
import com.azarovmykhailo.phonecontacts.service.ContactService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ContactControllerTest {

    @Mock
    private ContactService contactService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest request;

    private ContactController contactController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        contactController = new ContactController(contactService, jwtTokenProvider);
    }

    @Test
    void testGetAllContacts() {
        String username = "testuser";
        List<ContactDTO> contacts = new ArrayList<>();

        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("name");
        contactDTO.setEmails(Collections.singletonList("emails"));
        contactDTO.setPhones(Collections.singletonList("+380000000000"));

        ContactDTO contactDTO1 = new ContactDTO();
        contactDTO1.setName("name1");
        contactDTO1.setEmails(Collections.singletonList("emails1"));
        contactDTO1.setPhones(Collections.singletonList("+380000000001"));

        contacts.add(contactDTO);
        contacts.add(contactDTO1);

        when(jwtTokenProvider.getTokenFromRequest(request)).thenReturn("mocked_token");
        when(jwtTokenProvider.getUsernameFromToken("mocked_token")).thenReturn(username);
        when(contactService.getAllContactsByUsername(username)).thenReturn(contacts);

        ResponseEntity<List<ContactDTO>> response = contactController.getAllContacts(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(contacts, response.getBody());

        verify(jwtTokenProvider, times(1)).getTokenFromRequest(request);
        verify(jwtTokenProvider, times(1)).getUsernameFromToken("mocked_token");
        verify(contactService, times(1)).getAllContactsByUsername(username);
    }

    @Test
    void testCreateContact() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("name");
        contactDTO.setEmails(Collections.singletonList("emails"));
        contactDTO.setPhones(Collections.singletonList("+380000000000"));
        String username = "testuser";

        when(jwtTokenProvider.getTokenFromRequest(request)).thenReturn("mocked_token");
        when(jwtTokenProvider.getUsernameFromToken("mocked_token")).thenReturn(username);

        ResponseEntity<String> response = contactController.createContact(contactDTO, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Contact was created successfully!", response.getBody());

        verify(jwtTokenProvider, times(1)).getTokenFromRequest(request);
        verify(jwtTokenProvider, times(1)).getUsernameFromToken("mocked_token");
        verify(contactService, times(1)).createContact(contactDTO, username);
    }

    @Test
    void testUpdateContact() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("name");
        contactDTO.setEmails(Collections.singletonList("emails"));
        contactDTO.setPhones(Collections.singletonList("+380000000000"));
        String username = "testuser";

        when(jwtTokenProvider.getTokenFromRequest(request)).thenReturn("mocked_token");
        when(jwtTokenProvider.getUsernameFromToken("mocked_token")).thenReturn(username);

        ResponseEntity<String> response = contactController.updateContact(contactDTO, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The contact was edited", response.getBody());

        verify(jwtTokenProvider, times(1)).getTokenFromRequest(request);
        verify(jwtTokenProvider, times(1)).getUsernameFromToken("mocked_token");
        verify(contactService, times(1)).updateContact(contactDTO, username);
    }

    @Test
    void testDeleteContact() {
        String name = "John Doe";
        String username = "testuser";

        when(jwtTokenProvider.getTokenFromRequest(request)).thenReturn("mocked_token");
        when(jwtTokenProvider.getUsernameFromToken("mocked_token")).thenReturn(username);

        ResponseEntity<String> response = contactController.deleteContact(name, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The contact was deleted", response.getBody());

        verify(jwtTokenProvider, times(1)).getTokenFromRequest(request);
        verify(jwtTokenProvider, times(1)).getUsernameFromToken("mocked_token");
        verify(contactService, times(1)).deleteContact(name);
    }

    @Test
    void testDeleteAllContacts() {
        String username = "testuser";

        when(jwtTokenProvider.getTokenFromRequest(request)).thenReturn("mocked_token");
        when(jwtTokenProvider.getUsernameFromToken("mocked_token")).thenReturn(username);

        ResponseEntity<String> response = contactController.deleteContact(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("The contacts were deleted", response.getBody());

        verify(jwtTokenProvider, times(1)).getTokenFromRequest(request);
        verify(jwtTokenProvider, times(1)).getUsernameFromToken("mocked_token");
        verify(contactService, times(1)).deleteContacts(username);
    }

}