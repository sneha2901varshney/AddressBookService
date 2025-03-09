package com.example.addressBookService.Interfaces;
import java.util.*;
import com.example.addressBookService.DTO.ContactDTO;
import com.example.addressBookService.DTO.ResponseDTO;
import org.springframework.stereotype.Service;

@Service
public interface IContactService {



    public ContactDTO get(Long id);

    public ContactDTO create(ContactDTO user);

    public String clear();

    public List<ContactDTO> getAll();

    public ContactDTO edit(ContactDTO user, Long id);

    public String delete(Long id);

    public ResponseDTO response(String message, String status);
}