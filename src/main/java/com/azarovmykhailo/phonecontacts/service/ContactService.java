package com.azarovmykhailo.phonecontacts.service;

import com.azarovmykhailo.phonecontacts.dto.ContactDTO;
import com.azarovmykhailo.phonecontacts.entity.Contact;
import com.azarovmykhailo.phonecontacts.entity.EmailAddress;
import com.azarovmykhailo.phonecontacts.entity.PhoneNumber;
import com.azarovmykhailo.phonecontacts.entity.User;
import com.azarovmykhailo.phonecontacts.exception.CustomException;
import com.azarovmykhailo.phonecontacts.mapper.ContactMapper;
import com.azarovmykhailo.phonecontacts.repository.ContactRepository;
import com.azarovmykhailo.phonecontacts.repository.EmailAddressRepository;
import com.azarovmykhailo.phonecontacts.repository.PhoneNumberRepository;
import com.azarovmykhailo.phonecontacts.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContactService {

    private final ContactRepository contactRepository;
    private final EmailAddressRepository emailAddressRepository;

    private final PhoneNumberRepository phoneNumberRepository;
    private final UserRepository userRepository;

    private final UserService userService;

    public ContactService(ContactRepository contactRepository, UserService userService,
                          EmailAddressRepository emailAddressRepository, PhoneNumberRepository phoneNumberRepository, UserRepository userRepository) {
        this.contactRepository = contactRepository;
        this.userService = userService;
        this.emailAddressRepository = emailAddressRepository;
        this.phoneNumberRepository = phoneNumberRepository;
        this.userRepository = userRepository;
    }

    public List<ContactDTO> getAllContactsByUsername(String username) {
        User user = userService.getUserByUsername(username);
        List<Contact> contacts = contactRepository.findAllByUser(user);

        List<ContactDTO> contactDTOList = new ArrayList<>();

        for (Contact cont : contacts) {
            contactDTOList.add(ContactMapper.convertToDTO(cont));
        }
        return contactDTOList;
    }

    public void createContact(ContactDTO contactDTO, String username) {
        validateContactData(contactDTO);
        User user = userService.getUserByUsername(username);

        Contact contact = new Contact();
        contact.setName(contactDTO.getName());
        contact.setUser(user);

        Contact savedContact = contactRepository.save(contact);

        for (String email : contactDTO.getEmails()) {
            EmailAddress emailAddress = new EmailAddress();
            emailAddress.setEmail(email);
            emailAddress.setContact(savedContact);
            emailAddressRepository.save(emailAddress);
        }

        for (String phoneNumber : contactDTO.getPhones()) {
            PhoneNumber phoneNumberEntity = new PhoneNumber();
            phoneNumberEntity.setNumber(phoneNumber);
            phoneNumberEntity.setContact(savedContact);
            phoneNumberRepository.save(phoneNumberEntity);
        }

    }

    @Transactional
    public void updateContact(ContactDTO contactDTO, String username) {
        User user = userRepository.findByUsername(username);

        String contactName = contactDTO.getName();
        Contact contact = null;

        for (Contact cont : user.getContacts()) {
            if (cont.getName().equals(contactName)) {
                contact = cont;
            }
        }
        if (contact == null) {
            throw new EntityNotFoundException("There is no contact with this name: " + contactName);
        }

        emailAddressRepository.deleteAllByContact(contact);
        phoneNumberRepository.deleteAllByContact(contact);

        contact.setEmails(new ArrayList<>());
        contact.setPhoneNumbers(new ArrayList<>());
        contact.setName(contactDTO.getName());

        for (String email : contactDTO.getEmails()) {
            EmailAddress emailAddress = new EmailAddress();
            emailAddress.setEmail(email);
            emailAddress.setContact(contact);
            contact.getEmails().add(emailAddress);
            emailAddressRepository.save(emailAddress);
        }

        for (String phoneNumber : contactDTO.getPhones()) {
            PhoneNumber phoneNumberEntity = new PhoneNumber();
            phoneNumberEntity.setNumber(phoneNumber);
            phoneNumberEntity.setContact(contact);
            contact.getPhoneNumbers().add(phoneNumberEntity);
            phoneNumberRepository.save(phoneNumberEntity);
        }

        contactRepository.save(contact);

    }

    @Transactional
    public void deleteContact(String name) {
        contactRepository.deleteByName(name);
    }

    @Transactional
    public void deleteContacts(String username) {
        User user = userService.getUserByUsername(username);
        contactRepository.deleteAllByUser(user);
    }

    private void validateContactData(ContactDTO contactDTO) {
        List<String> emails = contactDTO.getEmails();
        List<String> phoneNumbers = contactDTO.getPhones();

        if (emails != null && !emails.isEmpty()) {
            boolean emailExists = emailAddressRepository.existsByEmailIn(emails);
            if (emailExists) {
                throw new CustomException(HttpStatus.NOT_ACCEPTABLE, "Email already exists for another contact");
            }

            String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
            for (String email : emails) {
                if (!email.matches(emailRegex)) {
                    throw new CustomException(HttpStatus.NOT_ACCEPTABLE, "Invalid email format");
                }
            }
        }

        if (phoneNumbers != null && !phoneNumbers.isEmpty()) {

            boolean phoneNumberExists = phoneNumberRepository.existsByNumberIn(phoneNumbers);
            if (phoneNumberExists) {
                throw new CustomException(HttpStatus.NOT_ACCEPTABLE, "Phone number already exists for another contact");
            }

            String phoneNumberRegex = "^\\+380\\d{9}$";
            for (String phoneNumber : phoneNumbers) {
                if (!phoneNumber.matches(phoneNumberRegex)) {
                    throw new CustomException(HttpStatus.NOT_ACCEPTABLE, "Invalid phone number format");
                }
            }
        }
    }

}