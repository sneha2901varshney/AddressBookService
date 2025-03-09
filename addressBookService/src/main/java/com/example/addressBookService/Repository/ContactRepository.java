package com.example.addressBookService.Repository;

import com.example.addressBookService.Entity.ContactEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<ContactEntity, Long> {

    public ContactEntity findByEmail(String email);

}