package com.azarovmykhailo.phonecontacts.repository;

import com.azarovmykhailo.phonecontacts.entity.Contact;
import com.azarovmykhailo.phonecontacts.entity.PhoneNumber;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhoneNumberRepository extends JpaRepository<PhoneNumber, Long> {
    boolean existsByNumberIn(List<String> phoneNumbers);

    void deleteAllByContact(Contact contact);
}
