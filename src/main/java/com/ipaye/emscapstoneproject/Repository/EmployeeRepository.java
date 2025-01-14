package com.ipaye.emscapstoneproject.Repository;


import com.ipaye.emscapstoneproject.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{


    List<Employee> findByFirstNameContainingIgnoreCase(String name);

    List<Employee> findByDepartmentNameContainingIgnoreCase(String departmentName);

    List<Employee> findByEmailContainingIgnoreCase(String email);

    boolean existsByFirstName(String john);

    long countByDepartmentName(String finance);
}
