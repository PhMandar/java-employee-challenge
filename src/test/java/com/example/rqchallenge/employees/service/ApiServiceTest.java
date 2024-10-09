package com.example.rqchallenge.employees.service;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.example.rqchallenge.employees.model.Employee;

import java.util.List;

class ApiServiceTest {

    @InjectMocks
    private ApiService apiService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        apiService.apiUrl = "http://dummy.api/employee/";
    }

    @Test
    void deleteEmployee_success() {
        String employeeId = "123";
        String urlToDelete = apiService.apiUrl + employeeId;
        
        // Mocking the response
        ResponseEntity<String> responseEntity = new ResponseEntity<>(HttpStatus.OK);
        when(restTemplate.exchange(urlToDelete, HttpMethod.DELETE, null, String.class))
                .thenReturn(responseEntity);

        String result = apiService.deleteEmployee(employeeId);

        verify(restTemplate, times(1)).exchange(urlToDelete, HttpMethod.DELETE, null, String.class);
        assertEquals("Success", result);
    }

    @Test
    void deleteEmployee_failure() {
        String employeeId = "123";
        String urlToDelete = apiService.apiUrl + employeeId;

        // Mocking the response to throw an exception
        when(restTemplate.exchange(urlToDelete, HttpMethod.DELETE, null, String.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        String result = apiService.deleteEmployee(employeeId);

        verify(restTemplate, times(1)).exchange(urlToDelete, HttpMethod.DELETE, null, String.class);
        assertEquals("Failed", result);
    }

    @Test
    void createEmployee_success() {
        Employee employee = new Employee("John Doe", 50000.0, 30);
        when(restTemplate.postForObject(anyString(), any(Employee.class), eq(Employee.class)))
                .thenReturn(employee);

        Employee result = apiService.createEmployee("John Doe", "50000", "30");

        verify(restTemplate, times(1)).postForObject(anyString(), any(Employee.class), eq(Employee.class));
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void getEmployeeById() {
        String employeeId = "123";
        Employee employee = new Employee("John Doe", 50000.0, 30);
        when(restTemplate.getForObject(apiService.apiUrl + employeeId, Employee.class))
                .thenReturn(employee);

        Employee result = apiService.getEmployeeById(employeeId);

        verify(restTemplate, times(1)).getForObject(apiService.apiUrl + employeeId, Employee.class);
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    void getAllEmployees() {
        Employee[] employees = {
            new Employee("John Doe", 50000.0, 30),
            new Employee("Jane Doe", 60000.0, 28)
        };
        when(restTemplate.getForEntity(apiService.apiUrl, Employee[].class))
                .thenReturn(new ResponseEntity<>(employees, HttpStatus.OK));

        List<Employee> result = apiService.getAllEmployees();

        verify(restTemplate, times(1)).getForEntity(apiService.apiUrl, Employee[].class);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void getHighestSalaryOfEmployees() {
        Employee[] employees = {
            new Employee("John Doe", 50000.0, 30),
            new Employee("Jane Doe", 60000.0, 28)
        };
        when(restTemplate.getForEntity(apiService.apiUrl, Employee[].class))
                .thenReturn(new ResponseEntity<>(employees, HttpStatus.OK));

        double result = apiService.getHighestSalaryOfEmployees();

        verify(restTemplate, times(1)).getForEntity(apiService.apiUrl, Employee[].class);
        assertEquals(60000.0, result);
    }

    @Test
    void getTopTenHigestEarningEmployeeNames() {
        Employee[] employees = {
            new Employee("John Doe", 50000.0, 30),
            new Employee("Jane Doe", 60000.0, 28),
            new Employee("Alice", 70000.0, 35)
        };
        when(restTemplate.getForEntity(apiService.apiUrl, Employee[].class))
                .thenReturn(new ResponseEntity<>(employees, HttpStatus.OK));

        List<String> result = apiService.getTopTenHigestEarningEmployeeNames();

        verify(restTemplate, times(1)).getForEntity(apiService.apiUrl, Employee[].class);
        assertEquals(3, result.size());
        assertEquals("Alice", result.get(0));  // Highest salary
    }

    @Test
    void getEmployeesByNameSearch() {
        Employee[] employees = {
            new Employee("John Doe", 50000.0, 30),
            new Employee("Jane Doe", 60000.0, 28)
        };
        when(restTemplate.getForEntity(apiService.apiUrl, Employee[].class))
                .thenReturn(new ResponseEntity<>(employees, HttpStatus.OK));

        List<Employee> result = apiService.getEmployeesByNameSearch("Jane Doe");

        verify(restTemplate, times(1)).getForEntity(apiService.apiUrl, Employee[].class);
        assertEquals(1, result.size());
        assertEquals("Jane Doe", result.get(0).getName());
    }
}