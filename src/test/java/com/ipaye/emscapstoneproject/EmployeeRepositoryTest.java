package com.ipaye.emscapstoneproject;

import com.ipaye.emscapstoneproject.Model.Employee;
import com.ipaye.emscapstoneproject.Repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;


@SpringBootTest
@Transactional
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    public void setUp() {

        employeeRepository.deleteAll(); // clean slate before test
        employeeRepository.save(new Employee(null, "john", "mark", "jmark@yopmail.com", "finance"));
        employeeRepository.save(new Employee(null, "jane", "smith", "jmark@yopmail.com", "finance"));
        employeeRepository.save(new Employee(null, "chloe", "mark", "jmark@yopmail.com", "finance"));
        employeeRepository.save(new Employee(null, "jack", "smith", "jmark@yopmail.com", "finance"));
    }


    // 1
    @Test
    void findAll() {
        List<Employee> employees=employeeRepository.findAll();
        assertThat(employees).hasSize(4);
    }

    // 2
    @Test
    public void findById() {
        Employee employee=employeeRepository.findAll().get(0);
        Optional<Employee> result=employeeRepository.findById(employee.getId());

        assertThat(result).isPresent();

        result.ifPresent(res -> assertThat(res.getFirstName()).isEqualToIgnoringCase("John"));
    }


    // 3
    @Test
    void findByNonExistingId() {
        Optional<Employee> result=employeeRepository.findById(999L);
        assertThat(result).isNotPresent();

    }

    // 4
    @Test
    void testSaveEmployee() {
        Employee newEmployee=new Employee(null, "John", "Break", "johnB@gmail.com", "finance");
        Employee savedEmployee=employeeRepository.save(newEmployee);

        assertThat(savedEmployee.getId()).isNotNull();
        assertThat(employeeRepository.findAll()).hasSize(5);
    }

    // 5
    @Test
    public void updateEmployee() {
        Employee employee=employeeRepository.findAll().get(0);
        employee.setDepartmentName("Accounting");
        employee.setEmail("johnDoe@gmail.com");

        Employee updatedEmployee=employeeRepository.save(employee);
        Optional<Employee> result=employeeRepository.findById(updatedEmployee.getId());

        assertThat(result).isPresent();
        assertThat(result.get().getDepartmentName()).isEqualTo("Accounting");
        assertThat(result.get().getEmail()).isEqualTo("johnDoe@gmail.com");
    }

    // 6
    @Test
    public void deleteEmployee() {
        Employee employee=employeeRepository.findAll().get(0);

        employeeRepository.deleteById(employee.getId());
        List<Employee> remainingEmployees=employeeRepository.findAll();

        assertThat(remainingEmployees).hasSize(3);
        assertThat(employeeRepository.existsById(employee.getId())).isFalse();
    }

    // 7
    @Test
    void customFindByName() {
        List<Employee> employees=employeeRepository.findByFirstNameContainingIgnoreCase("john");

        assertThat(employees).hasSize(1);
        assertThat(employees.get(0).getDepartmentName()).isEqualTo("finance");
    }

    // 8
    @Test
    void findByDepartment() {
        List<Employee> employees=employeeRepository.findByDepartmentNameContainingIgnoreCase("finance");

        assertThat(employees).hasSize(4);
        assertThat(employees.get(0).getFirstName()).isEqualTo("john");
    }

    //9
    @Test
    void findByEmail() {

        List<Employee> employees=employeeRepository.findByEmailContainingIgnoreCase("jmark@yopmail.com");

        assertThat(employees).hasSize(4);
        assertThat(employees).extracting(Employee::getFirstName).containsExactlyInAnyOrder("john", "jane", "chloe", "jack");
    }

    // 10
    @Test
    void countEmployeeByDepartment() {
        long count=employeeRepository.countByDepartmentName("finance");

        assertThat(count).isEqualTo(4);
    }

    // 11
    @Test
    void ExistsByFirstName() {
        boolean exists=employeeRepository.existsByFirstName("john");

        assertThat(exists).isTrue();
    }


}
