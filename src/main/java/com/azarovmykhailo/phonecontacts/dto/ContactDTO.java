package com.azarovmykhailo.phonecontacts.dto;

import java.util.List;

public class ContactDTO {

    private String name;
    private List<String> emails;
    private List<String> phones;


    public ContactDTO() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getEmails() {
        return emails;
    }

    public void setEmails(List<String> emails) {
        this.emails = emails;
    }

    public List<String> getPhones() {
        return phones;
    }

    public void setPhones(List<String> phones) {
        this.phones = phones;
    }
}

