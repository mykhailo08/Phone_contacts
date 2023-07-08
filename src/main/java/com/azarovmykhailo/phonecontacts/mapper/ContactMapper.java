package com.azarovmykhailo.phonecontacts.mapper;

import com.azarovmykhailo.phonecontacts.dto.ContactDTO;
import com.azarovmykhailo.phonecontacts.entity.Contact;
import com.azarovmykhailo.phonecontacts.entity.EmailAddress;
import com.azarovmykhailo.phonecontacts.entity.PhoneNumber;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ContactMapper {

    public static Contact convertToEntity(ContactDTO contactDTO) {

        Contact contact = new Contact();
        contact.setName(contactDTO.getName());

        List<EmailAddress> emailAddresses = new ArrayList<>();
        for (String email : contactDTO.getEmails()) {
            EmailAddress emailAddress = new EmailAddress();
            emailAddress.setEmail(email);
            emailAddress.setContact(contact);
            emailAddresses.add(emailAddress);
        }
        contact.setEmails(emailAddresses);

        List<PhoneNumber> phoneNumbers = new ArrayList<>();
        for (String phoneNumber : contactDTO.getPhones()) {
            PhoneNumber phone = new PhoneNumber();
            phone.setNumber(phoneNumber);
            phone.setContact(contact);
            phoneNumbers.add(phone);
        }
        contact.setPhoneNumbers(phoneNumbers);

        return contact;
    }

    public static ContactDTO convertToDTO(Contact contact) {
        ContactDTO contactDTO = new ContactDTO();
        contactDTO.setName(contact.getName());

        List<String> emails = new ArrayList<>();
        for (EmailAddress emailAddress : contact.getEmails()) {
            emails.add(emailAddress.getEmail());
        }
        contactDTO.setEmails(emails);

        List<String> phones = new ArrayList<>();
        for (PhoneNumber phoneNumber : contact.getPhoneNumbers()) {
            phones.add(phoneNumber.getNumber());
        }
        contactDTO.setPhones(phones);

        return contactDTO;
    }
}

