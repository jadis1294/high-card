package it.sara.demo.service.database.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
    private String guid;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public User(){};
    public User(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

}
