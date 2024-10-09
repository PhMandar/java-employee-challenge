package com.example.rqchallenge.employees.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.ApiService;

public class EmployeeControllerTest {
    private EmployeeController employeeController;

    @Mock
    private ApiService apiService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeController = new EmployeeController();
        // employeeController.employeeService = employeeService;
        employeeController.apiService = apiService;
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() throws Exception {
        List<Employee> employees = Arrays.asList(new Employee("John Doe", 50000.0, 30));
        when(apiService.getAllEmployees()).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());
    }

    @Test
    void getEmployeesByNameSearch_ShouldReturnEmployeeList() {
        String name = "John";
        List<Employee> employees = Arrays.asList(new Employee("John Doe", 50000.0, 30));
        when(apiService.getEmployeesByNameSearch(name)).thenReturn(employees);

        ResponseEntity<List<Employee>> response = employeeController.getEmployeesByNameSearch(name);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employees, response.getBody());
    }

    @Test
    void getEmployeeById_ValidId_ShouldReturnEmployee() {
        String id = "1";
        Employee employee = new Employee("John Doe", 50000.0, 30);
        when(apiService.getEmployeeById(id)).thenReturn(employee);

        ResponseEntity<Employee> response = employeeController.getEmployeeById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(employee, response.getBody());
    }

    @Test
    void createEmployee_ValidInput_ShouldReturnCreatedEmployee() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("name", "John Doe");
        employeeInput.put("age", "30");
        employeeInput.put("salary", "50000");

        Employee createdEmployee = new Employee("John Doe", 50000.0, 30);
        when(apiService.createEmployee("John Doe", "50000", "30")).thenReturn(createdEmployee);

        ResponseEntity<Employee> response = employeeController.createEmployee(employeeInput);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(createdEmployee, response.getBody());
    }

    @Test
    void createEmployee_InvalidName_ShouldThrowException() {
        Map<String, Object> employeeInput = new HashMap<>();
        employeeInput.put("age", "30");
        employeeInput.put("salary", "50000");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeController.createEmployee(employeeInput);
        });

        assertEquals("Invalid name : Employee name is a mandatory field.", exception.getMessage());
    }

    @Test
    void deleteEmployeeById_ValidId_ShouldReturnSuccess() {
        String id = "1";
        when(apiService.deleteEmployee(id)).thenReturn("Success");

        ResponseEntity<String> response = employeeController.deleteEmployeeById(id);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Success", response.getBody());
    }

    @Test
    void deleteEmployeeById_InvalidId_ShouldThrowException() {
        String invalidId = "abc";

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            employeeController.deleteEmployeeById(invalidId);
        });

        assertEquals("Invalid id : must be provided valid id", exception.getMessage());
    }
}
