package com.example.addressBookService.Interfaces;
import java.util.*;
import com.example.addressBookService.DTO.EmployeeDTO;
import com.example.addressBookService.DTO.ResponseDTO;
import com.example.addressBookService.Entity.EmployeeEntity;
import org.springframework.stereotype.Service;

@Service
public interface IEmployeeService {

    public EmployeeDTO get(Long id) throws Exception;

    public EmployeeDTO create(EmployeeDTO user) throws Exception;

    public String clear();

    public List<EmployeeDTO> getAll();

    public EmployeeDTO edit(EmployeeDTO user, Long id) throws Exception;

    public String delete(Long id);

    public ResponseDTO response(String message, String status);
}