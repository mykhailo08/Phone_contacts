package com.azarovmykhailo.phonecontacts.mapper;

import com.azarovmykhailo.phonecontacts.dto.ContactDTO;
import com.azarovmykhailo.phonecontacts.entity.Contact;
import com.azarovmykhailo.phonecontacts.entity.EmailAddress;
import com.azarovmykhailo.phonecontacts.entity.PhoneNumber;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContactMapperTest {

    @Test
    void testConvertToEntity() {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName("Name");
        contactDTO.setEmails(Arrays.asList("Emails", "Emails1"));
        contactDTO.setPhones(Arrays.asList("380000000000", "380000000001"));

        Contact contact = ContactMapper.convertToEntity(contactDTO);

        assertNotNull(contact);
        assertEquals(contactDTO.getName(), contact.getName());
        assertEquals(contactDTO.getEmails().size(), contact.getEmails().size());
        assertEquals(contactDTO.getPhones().size(), contact.getPhoneNumbers().size());
    }

    @Test
    void testConvertToDTO() {
        
        Contact contact = new Contact();
        contact.setName("Name");

        EmailAddress email1 = new EmailAddress();
        email1.setEmail("Email");
        email1.setContact(contact);
        EmailAddress email2 = new EmailAddress();
        email2.setEmail("Email");
        email2.setContact(contact);
        contact.setEmails(Arrays.asList(email1, email2));

        PhoneNumber phone1 = new PhoneNumber();
        phone1.setNumber("380000000000");
        phone1.setContact(contact);
        PhoneNumber phone2 = new PhoneNumber();
        phone2.setNumber("380000000001");
        phone2.setContact(contact);
        contact.setPhoneNumbers(Arrays.asList(phone1, phone2));

        ContactDTO contactDTO = ContactMapper.convertToDTO(contact);

        assertNotNull(contactDTO);
        assertEquals(contact.getName(), contactDTO.getName());
        assertEquals(contact.getEmails().size(), contactDTO.getEmails().size());
        assertEquals(contact.getPhoneNumbers().size(), contactDTO.getPhones().size());
    }
}
