package com.ipaye.emscapstoneproject.Service;

import com.ipaye.emscapstoneproject.Exception.EmployeeNotFoundException;
import com.ipaye.emscapstoneproject.Model.Employee;
import com.ipaye.emscapstoneproject.Repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {


    @Autowired
    private EmployeeRepository employeeRepository;

    public void addEmployee(Employee employee) {

        employeeRepository.save(employee);


    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Transactional
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee getEmployeeById(long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);

        return employeeOptional.orElseThrow(() -> new EmployeeNotFoundException("Employee with id "+id+ " not found"));
    }

    public Employee getEmployeeByIdInvalid(Long id){
        if (id <= 0) {
            throw new IllegalArgumentException("Employee with id " +id+" not found");
        }
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found"));
    }


    public Employee getEmployeeByIdNull(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with ID: " + id));
    }

    public Employee updateEmployee(Long id, Employee employee) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException ("Employee not found with id: " + id));


        // update the fields
        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setDepartmentName(employee.getDepartmentName());


        // save the updated employee back to the database
        return employeeRepository.save(existingEmployee);
    }

    public Employee partialUpdateUser(Long id, Map<String, Object> updates) {
        // fetch the existing employee
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        // iterate through updates and apply changes
        updates.forEach((Key, value) -> {
            switch (Key) {
                case "firstName":
                    employee.setFirstName((String) value);
                    break;
                case "lastName" :
                    employee.setLastName((String) value);
                    break;
                case "email" :
                    employee.setEmail((String) value);
                    break;
                case "departmentName":
                    employee.setDepartmentName((String) value);
                    break;

                default:
                    throw new IllegalArgumentException("Invalid Field: " + Key);
            }
        });

        return employeeRepository.save(employee);
    }

    public void deleteEmployeeById(Long id) {
        // check if employee exists
        if(!employeeRepository.existsById(id)){
            throw new EmployeeNotFoundException("Employee not found with id " + id);
        }

        // delete Employee
        employeeRepository.deleteById(id);
    }

    public List<Employee> searchEmployeeByName(String name) {
        return employeeRepository.findByFirstNameContainingIgnoreCase(name);
    }

    public List<Employee> searchByDepartment(String departmentName){
        return employeeRepository.findByDepartmentNameContainingIgnoreCase(departmentName);
    }

    public List<Employee> searchByEmail(String email){
        return employeeRepository.findByEmailContainingIgnoreCase(email);
    }

}

