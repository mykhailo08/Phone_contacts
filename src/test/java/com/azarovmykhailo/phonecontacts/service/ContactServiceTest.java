package com.azarovmykhailo.phonecontacts.service;

import com.azarovmykhailo.phonecontacts.dto.ContactDTO;
import com.azarovmykhailo.phonecontacts.entity.Contact;
import com.azarovmykhailo.phonecontacts.entity.EmailAddress;
import com.azarovmykhailo.phonecontacts.entity.PhoneNumber;
import com.azarovmykhailo.phonecontacts.entity.User;
import com.azarovmykhailo.phonecontacts.repository.ContactRepository;
import com.azarovmykhailo.phonecontacts.repository.EmailAddressRepository;
import com.azarovmykhailo.phonecontacts.repository.PhoneNumberRepository;
import com.azarovmykhailo.phonecontacts.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @Mock
    private EmailAddressRepository emailAddressRepository;

    @Mock
    private PhoneNumberRepository phoneNumberRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ContactService contactService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetAllContactsByUsername() {
        
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        List<Contact> contacts = new ArrayList<>();
        Contact  contact = new Contact();
        contact.setEmails(Collections.singletonList(new EmailAddress()));
        contact.setPhoneNumbers(Collections.singletonList(new PhoneNumber()));
        contacts.add(contact);
        when(userService.getUserByUsername(username)).thenReturn(user);
        when(contactRepository.findAllByUser(user)).thenReturn(contacts);
        
        List<ContactDTO> result = contactService.getAllContactsByUsername(username);

        assertEquals(contacts.size(), result.size());
        verify(userService).getUserByUsername(username);
        verify(contactRepository).findAllByUser(user);
    }

    @Test
    void testCreateContact() {
        
        String username = "testUser";
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setPhones(new ArrayList<>());
        contactDTO.setEmails(new ArrayList<>());
        User user = new User();
        user.setUsername(username);
        when(userService.getUserByUsername(username)).thenReturn(user);
        when(contactRepository.save(any(Contact.class))).thenReturn(new Contact());

        assertDoesNotThrow(() -> contactService.createContact(contactDTO, username));

        verify(userService).getUserByUsername(username);
        verify(contactRepository).save(any(Contact.class));
        verify(emailAddressRepository, times(0)).save(any(EmailAddress.class));
        verify(phoneNumberRepository, times(0)).save(any(PhoneNumber.class));
    }

    @Test
    void testUpdateContact() {
        
        String username = "testUser";
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("Test Contact");
        contactDTO.setEmails(new ArrayList<>());
        contactDTO.setPhones(new ArrayList<>());
        User user = new User();
        user.setUsername(username);
        Contact existingContact = new Contact();
        existingContact.setName(contactDTO.getName());
        existingContact.setEmails(Collections.singletonList(new EmailAddress()));
        existingContact.setPhoneNumbers(Collections.singletonList(new PhoneNumber()));
        user.setContacts(Collections.singletonList(existingContact));
        when(userRepository.findByUsername(username)).thenReturn(user);
        when(contactRepository.save(existingContact)).thenReturn(existingContact);

        assertDoesNotThrow(() -> contactService.updateContact(contactDTO, username));

        verify(userRepository).findByUsername(username);
        verify(emailAddressRepository).deleteAllByContact(existingContact);
        verify(phoneNumberRepository).deleteAllByContact(existingContact);
        verify(contactRepository).save(existingContact);
    }

    @Test
    void testDeleteContact() {
        
        String contactName = "Test Contact";

        assertDoesNotThrow(() -> contactService.deleteContact(contactName));

        verify(contactRepository).deleteByName(contactName);
    }

    @Test
    void testDeleteContacts() {
        
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        when(userService.getUserByUsername(username)).thenReturn(user);

        assertDoesNotThrow(() -> contactService.deleteContacts(username));

        verify(userService).getUserByUsername(username);
        verify(contactRepository).deleteAllByUser(user);
    }
}
