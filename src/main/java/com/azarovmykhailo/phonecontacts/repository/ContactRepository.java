package com.azarovmykhailo.phonecontacts.repository;

import com.azarovmykhailo.phonecontacts.entity.Contact;
import com.azarovmykhailo.phonecontacts.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact, Long> {

    List<Contact> findAllByUser(User user);

    void deleteByName(String name);

    void deleteAllByUser(User user);

}
