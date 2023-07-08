package com.azarovmykhailo.phonecontacts.controller;

import com.azarovmykhailo.phonecontacts.dto.ContactDTO;
import com.azarovmykhailo.phonecontacts.security.JwtTokenProvider;
import com.azarovmykhailo.phonecontacts.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    private final ContactService contactService;

    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public ContactController(ContactService contactService, JwtTokenProvider jwtTokenProvider) {
        this.contactService = contactService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping
    public ResponseEntity<List<ContactDTO>> getAllContacts(HttpServletRequest request) {

        String username = getUsernameByToken(request);
        if (!username.equals("")) {
            List<ContactDTO> contacts = contactService.getAllContactsByUsername(username);
            return new ResponseEntity<>(contacts, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/create-contact")
    public ResponseEntity<String> createContact(@RequestBody ContactDTO contactDTO, HttpServletRequest request) {
        String username = getUsernameByToken(request);
        if (!username.equals("")) {
            contactService.createContact(contactDTO, username);
            return new ResponseEntity<>("Contact was created successfully!", HttpStatus.OK);
        }
        return new ResponseEntity<>("The token is incorrect", HttpStatus.BAD_REQUEST);

    }

    @PutMapping("/edit-contact")
    public ResponseEntity<String> updateContact(@RequestBody ContactDTO contactDTO, HttpServletRequest request) {
        String username = getUsernameByToken(request);
        if (!username.equals("")) {
            contactService.updateContact(contactDTO, username);
            return new ResponseEntity<>("The contact was edited", HttpStatus.OK);
        }
        return new ResponseEntity<>("The token is incorrect", HttpStatus.BAD_REQUEST);

    }

    @DeleteMapping("/delete/{name}")
    public ResponseEntity<String> deleteContact(@PathVariable String name, HttpServletRequest request) {
        if (!getUsernameByToken(request).equals("")) {
            contactService.deleteContact(name);
            return new ResponseEntity<>("The contact was deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("The token is incorrect", HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteContact(HttpServletRequest request) {
        String username = getUsernameByToken(request);
        if (!username.equals("")) {
            contactService.deleteContacts(username);
            return new ResponseEntity<>("The contacts were deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("The token is incorrect", HttpStatus.BAD_REQUEST);


    }

    private String getUsernameByToken(HttpServletRequest request) {
        try {
            String token = jwtTokenProvider.getTokenFromRequest(request);
            return jwtTokenProvider.getUsernameFromToken(token);
        } catch (IllegalArgumentException e) {
            return "";
        }
    }


}

