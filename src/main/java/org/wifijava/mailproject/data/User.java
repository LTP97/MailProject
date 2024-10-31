package org.wifijava.mailproject.data;


import lombok.Getter;
import lombok.Setter;

@Getter
public class User {
    private final String emailAdress;
    @Setter
    private String password;

    public User(String emailAdress, String password) {
        this.emailAdress = emailAdress;
        this.password = password;
    }

}
