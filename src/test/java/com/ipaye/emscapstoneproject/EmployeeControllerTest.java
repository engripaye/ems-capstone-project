package com.ipaye.emscapstoneproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ipaye.emscapstoneproject.Controller.EmployeeController;
import com.ipaye.emscapstoneproject.Model.Employee;
import com.ipaye.emscapstoneproject.Repository.EmployeeRepository;
import com.ipaye.emscapstoneproject.Service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmployeeControllerTest {

    @Mock
    private  EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    @Autowired
    MockMvc mockMvc;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }



    // TEST CASE 1
    @Test
    public void GetAllEmployees() throws Exception{
       List<Employee> employees =Arrays.asList(
               new Employee(1L, "John", "Doe", "JohnD@gmail.com", "Engineering"),
               new Employee(2L, "Kate", "Smith", "katesmith@gmail.com", "HR")
       );
       when(employeeService.getAllEmployees()).thenReturn(employees);

       mockMvc.perform(get("/employees")
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()").value(employees.size()))
               .andExpect(jsonPath("$[0].firstName").value("John"))
               .andExpect(jsonPath("$[1].departmentName").value("HR"));
    }

    // TEST CASE 2

    @Test
    public void getEmployeesById() throws Exception{
      Long employeeId = 1L;
      Employee employee = new Employee(employeeId, "John", "Doe", "Johnd@gmail.com", "engineering");

      when(employeeService.getEmployeeById(employeeId)).thenReturn(Optional.of(employee));

      mockMvc.perform(get("/employees/{id}", employeeId)
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.id").value(employeeId))
              .andExpect(jsonPath("$.firstName").value("John"))
              .andExpect(jsonPath("$.departmentName").value("engineering"));

    }

    // TEST CASE 3
    @Test
    public void AddEmployee() throws Exception {
        String employeeJson = "{\"firstName\":\"Alice\", \"lastName\":\"Brown\", \"department\":\"Marketing\"}";

        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON) // Ensure Content-Type is set
                        .content(employeeJson)) // Provide a valid JSON payload
                .andExpect(status().isCreated());
    }

    // TEST CASE 4
    @Test
    void updateEmployee() throws Exception{
      Long employeeId = 1L;
      Employee updatedEmployee = new Employee(employeeId, "Smith", "Rowe", "SmithR@gmail.com", "Finance");

        String employeeJson = new ObjectMapper().writeValueAsString(updatedEmployee);
     when(employeeService.updateEmployee(eq(employeeId), any(Employee.class))).thenReturn(updatedEmployee);

     mockMvc.perform(put("/employees/{id}", employeeId)
             .contentType(MediaType.APPLICATION_JSON)
             .content(employeeJson))
             .andExpect(status().isOk())
             .andExpect(jsonPath("$.id").value(employeeId))
             .andExpect(jsonPath("$.firstName").value("Smith"))
             .andExpect(jsonPath("$.departmentName").value("Finance"));
    }

    // TEST CASE 5
    @Test
    void deleteEmployees() throws Exception{

        Long employeeId = 1L;

        doNothing().when(employeeService).deleteEmployeeById(employeeId);

        mockMvc.perform(delete("/employees/{id}", employeeId))
                .andExpect(status().isNoContent());
    }



}
