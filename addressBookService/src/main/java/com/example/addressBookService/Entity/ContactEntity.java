package com.example.addressBookService.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "contacts")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ContactEntity {

    String name;
    String email;
    Long phoneNumber;
    String address;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    public ContactEntity(String name, String email, Long phoneNumber, String address) {
        this.name = name;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }
}