package com.ipaye.emscapstoneproject;

import com.ipaye.emscapstoneproject.Exception.EmployeeNotFoundException;
import com.ipaye.emscapstoneproject.Model.Employee;
import com.ipaye.emscapstoneproject.Repository.EmployeeRepository;
import com.ipaye.emscapstoneproject.Service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeServiceTest {

   @Mock
   private EmployeeRepository employeeRepository;

   @InjectMocks
   private EmployeeService employeeService;


   // TEST CASE 1
    @Test
    void AddAndFetchAllEmployees() {
        Employee emp1 = (new Employee(1L, "Ipaye", "james", "ipaye@gmail.com", "civil engineer"));
        Employee emp2 = (new Employee(2L, "mariam", "eleanor", "ipaye@gmail.com", "civil engineer"));


        List<Employee> mockEmployees = Arrays.asList(emp1, emp2);

        // Mock repository behavior
        when(employeeRepository.findAll()).thenReturn(mockEmployees);

        List<Employee> employees = employeeService.getAllEmployees();

        assertEquals(2, employees.size());
        assertTrue(employees.contains(emp1));
        assertTrue(employees.contains(emp2));

    }

    // TEST CASE 2
    @Test
    void getEmployeeById(){

        // arrange
        Employee employee = (new Employee(1L, "Ipaye", "james", "ipaye@gmail.com", "civil engineer"));
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // act
        Employee result = employeeService.getEmployeeById(1L);

        // assert
        assertNotNull(result);
        assertEquals("Ipaye", result.getFirstName());
        verify(employeeRepository, times(1)).findById(1L);

    }

    // TEST CASE 3
    @Test
    void GetEmployeeById_EmployeeNotFoundException(){

        // arrange
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        //act and assert
        EmployeeNotFoundException exception = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.getEmployeeById(1L);
        });
        assertEquals("Employee with id 1 not found", exception.getMessage());
        verify(employeeRepository, times(1)).findById(1L);
    }

    // TEST CASE 4
    @Test
    void getAllEmployees_employeeList(){

        // Arrange
        when(employeeRepository.findAll()).thenReturn(Arrays.asList());

        // act
        List<Employee> employees = employeeService.getAllEmployees();

        // assert
        assertTrue(employees.isEmpty());
        verify(employeeRepository, times(1)).findAll();
    }

    // TEST CASE 5
    @Test
    void getAllEmployees_NullList(){

        // Arrange
        when(employeeRepository.findAll()).thenReturn(Arrays.asList());

        // Act
        List<Employee> employees = employeeService.getAllEmployees();

        //Assert
        assertTrue(employees.isEmpty());
        verify(employeeRepository, times(1)).findAll();

    }

    // TEST CASE 6
    @Test
    void getEmployeeById_InvalidId(){
        // Arrange
        Long invalidId = -1L;

        // Act and assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> employeeService.getEmployeeByIdInvalid(-1L));

        assertEquals("Employee with id " +invalidId+" not found", exception.getMessage());
        verify(employeeRepository, never()).findById(anyLong());
    }

    // TEST CASE 7
    @Test
    void getEmployeeByNullId(){
        // act and assert
        assertThrows(NullPointerException.class, ()-> {
            employeeService.getEmployeeById((Integer) null);
        });
        verify(employeeRepository, never()).findById(anyLong());
    }


    // TEST CASE 8
    @Test
    void getEmployeeById_DuplicateId(){

        // arrange
        Employee emp1 = (new Employee(1L, "Ipaye", "james", "ipaye@gmail.com", "civil engineer"));
        Employee emp2 = (new Employee(1L, "mariam", "eleanor", "ipaye@gmail.com", "civil engineer"));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp1)); // simulate first match

        // act
        Employee result = employeeService.getEmployeeById(1L);

        // assert
        assertNotNull(result);
        assertEquals("Ipaye", result.getFirstName());
        verify(employeeRepository, times(1)).findById(1L);
    }


    // TEST CASE 9
    @Test
    void GetEmployeeById_multipleCalls(){
        // arrange
        Employee employee = (new Employee(1L, "Ipaye", "james", "ipaye@gmail.com", "civil engineer"));

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        // Act and assert
        Employee firstCall = employeeService.getEmployeeById(1L);
        Employee secondCall = employeeService.getEmployeeById(1L);

        // assert
        assertNotNull(firstCall);
        assertNotNull(secondCall);
        assertEquals(firstCall, secondCall);
        verify(employeeRepository, times(2)).findById(1L);

    }

    // TEST CASE 10
    @Test
    void getEmployeeById_NullRepository() {


        Long invalidEmployeeId = 999L;
        when(employeeRepository.findById(invalidEmployeeId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.getEmployeeByIdNull(invalidEmployeeId));
    }

    // TEST CASE 11
    @Test
    void testRepositoryMethodNotCallForInvalidInput(){
        assertThrows(IllegalArgumentException.class, ()-> {
            employeeService.getEmployeeByIdInvalid(-5L);
        } );

        verify(employeeRepository, never()).findById(anyLong());
    }
}
