package com.ipaye.emscapstoneproject.Repository;


import com.ipaye.emscapstoneproject.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>{



}
