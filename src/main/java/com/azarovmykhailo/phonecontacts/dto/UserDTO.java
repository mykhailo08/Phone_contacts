package com.azarovmykhailo.phonecontacts.dto;

public class UserDTO {

    private String login;
    private String password;

    public UserDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public UserDTO() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

