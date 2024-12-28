package com.ipaye.emscapstoneproject;

import com.ipaye.emscapstoneproject.Controller.EmployeeController;
import com.ipaye.emscapstoneproject.Model.Employee;
import com.ipaye.emscapstoneproject.Repository.EmployeeRepository;
import com.ipaye.emscapstoneproject.Service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;



    @BeforeEach
    public void setUp(){
        // seed initial data
        employeeRepository.deleteAll();
        employeeRepository.save(new Employee(null, "john", "mark", "jmark@yopmail.com", "finance"));
        employeeRepository.save(new Employee(null, "jane", "smith", "jmark@yopmail.com", "finance"));

    }

    // TEST CASE 1
    @Test
    public void GetAllEmployees() throws Exception{
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].firstName").value("john"));
    }

    // TEST CASE 2

    @Test
    public void getEmployeesById() throws Exception{
        Employee employee = employeeRepository.findAll().get(0);
        mockMvc.perform(get("/employees/" + employee.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value(employee.getFirstName()));
    }

    // TEST CASE 3
    @Test
    public void createEmployee() throws Exception {
        String newEmployee="""
                            {
                            "firstName" : "Alice",
                            "lastName" : "Daniel",
                            "email" : "Aliced@gmail.com",
                            "Department" : "finance"
                }
                """;
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(newEmployee))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Alice"));

        assertThat(employeeRepository.count()).isEqualTo(3);
    }

    // TEST CASE 4
    @Test
    void updateEmployee() throws Exception{
        Employee employee = employeeRepository.findAll().get(0);
        String updatedEmployee ="""
                {
                "firstName" : "Jonathan",
                "lastName" : "David",
                "email" : "jdavid@gmail.com",
                "departmentName" : "HR"
                }
                """;
        mockMvc.perform(put("/employees/" + employee.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedEmployee))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Jonathan"));

        Employee updated = employeeRepository.findById(employee.getId()).orElseThrow();
        assertThat(updated.getFirstName()).isEqualTo("Jonathan");
    }

    // TEST CASE 5
    @Test
    void deleteEmployees() throws Exception{
        Employee employee = employeeRepository.findAll().get(0);

        mockMvc.perform(delete("/employees/" + employee.getId()))
                .andExpect(status().isNoContent());

        assertThat(employeeRepository.existsById(employee.getId())).isFalse();

    }



}

