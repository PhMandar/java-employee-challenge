package com.example.rqchallenge.employees.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.example.rqchallenge.employees.exceptions.ResourceNotFoundException;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.repository.IEmployeeRepository;
import com.example.rqchallenge.employees.service.EmployeeService;

public class EmployeeServiceTest {
    private EmployeeService employeeService;

    @Mock
    private IEmployeeRepository empRepo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeService = new EmployeeService();
        employeeService.empRepo = empRepo;
    }

    @Test
    void getAllEmployees_ShouldReturnEmployeeList() {
        List<Employee> employees = Arrays.asList(new Employee("John Doe", 50000.0, 30));
        when(empRepo.findAll()).thenReturn(employees);

        List<Employee> result = employeeService.getAllEmployees();
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(empRepo, times(1)).findAll();
    }

    @Test
    void getEmployeesByNameSearch_ShouldReturnEmployeeList() {
        String name = "John";
        List<Employee> employees = Arrays.asList(new Employee("John Doe", 50000.0, 30));
        when(empRepo.searchEmployeeByName(name)).thenReturn(employees);

        List<Employee> result = employeeService.getEmployeesByNameSearch(name);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
        verify(empRepo, times(1)).searchEmployeeByName(name);
    }

    @Test
    void getEmployeeById_ValidId_ShouldReturnEmployee() {
        String id = "1";
        Employee employee = new Employee("John Doe", 50000.0, 30);
        when(empRepo.findById(1L)).thenReturn(Optional.of(employee));

        Employee result = employeeService.getEmployeeById(id);
        assertEquals("John Doe", result.getName());
        verify(empRepo, times(1)).findById(1L);
    }

    @Test
    void getEmployeeById_InvalidId_ShouldThrowResourceNotFoundException() {
        String id = "99";
        when(empRepo.findById(99L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.getEmployeeById(id);
        });

        assertEquals("Requested employee with id : 99 not found", exception.getMessage());
        verify(empRepo, times(1)).findById(99L);
    }

    @Test
    void getHighestSalaryOfEmployees_ShouldReturnHighestSalary() {
        double highestSalary = 100000.0;
        when(empRepo.getHighestSalaryOfEmployees()).thenReturn(highestSalary);

        double result = employeeService.getHighestSalaryOfEmployees();
        assertEquals(highestSalary, result);
        verify(empRepo, times(1)).getHighestSalaryOfEmployees();
    }

    @Test
    void getTop10HighestEarningEmployeeNames_ShouldReturnListOfNames() {
        List<String> names = Arrays.asList("John Doe", "Jane Smith");
        when(empRepo.getTop10HighestEarningEmployeeNames()).thenReturn(names);

        List<String> result = employeeService.getTop10HighestEarningEmployeeNames();
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0));
        verify(empRepo, times(1)).getTop10HighestEarningEmployeeNames();
    }

    @Test
    void createEmployee_ShouldReturnCreatedEmployee() {
        Employee employee = new Employee("John Doe", 50000.0, 30);
        when(empRepo.save(any(Employee.class))).thenReturn(employee);

        Employee result = employeeService.createEmployee("John Doe", "50000", "30");
        assertEquals("John Doe", result.getName());
        assertEquals(employee, result);
    }

    @Test
    void deleteEmployee_ValidId_ShouldReturnSuccess() {
        Employee employee = new Employee("John Doe", 50000.0, 30);
        when(empRepo.findById(anyLong())).thenReturn(Optional.of(employee));

        String id = "1";
        doNothing().when(empRepo).deleteById(1L);

        String result = employeeService.deleteEmployee(id);
        assertEquals("Success", result);
        verify(empRepo, times(1)).deleteById(1L);
    }

    @Test
    void deleteEmployee_InvalidId_ShouldReturnFailure() {
        Employee employee = new Employee("John Doe", 50000.0, 30);
        when(empRepo.findById(anyLong())).thenReturn(Optional.of(employee));
        String id = "abc"; // Invalid ID

        String result = employeeService.deleteEmployee(id);
        assertEquals("Failure", result);
        verify(empRepo, times(0)).deleteById(anyLong());
    }
}
