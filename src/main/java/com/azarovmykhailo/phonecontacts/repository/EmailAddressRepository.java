package com.azarovmykhailo.phonecontacts.repository;

import com.azarovmykhailo.phonecontacts.entity.Contact;
import com.azarovmykhailo.phonecontacts.entity.EmailAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmailAddressRepository extends JpaRepository<EmailAddress, Long> {

    boolean existsByEmailIn(List<String> emails);

    void deleteAllByContact(Contact contact);

}
