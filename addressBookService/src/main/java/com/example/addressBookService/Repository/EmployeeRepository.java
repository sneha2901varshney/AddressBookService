package com.example.addressBookService.Repository;

import com.example.addressBookService.Entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {

    public EmployeeEntity findByEmail(String email);

}