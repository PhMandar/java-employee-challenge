package com.example.rqchallenge.employees.util;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class EmployeesUtilTest {

    @Test
    void testValidateEmployeeName_MissingName() {
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("age", "30");
        employeeData.put("salary", "50000");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EmployeesUtil.validateEmployeeInputData(employeeData);
        });

        assertEquals("Invalid name : Employee name is a mandatory field.", exception.getMessage());
    }

    @Test
    void testValidateEmployeeName_BlankName() {
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("name", "  ");
        employeeData.put("age", "30");
        employeeData.put("salary", "50000");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EmployeesUtil.validateEmployeeInputData(employeeData);
        });

        assertEquals("Invalid name: Must be provided as a valid string.", exception.getMessage());
    }

    @Test
    void testValidateEmployeeAge_MissingAge() {
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("name", "John Doe");
        employeeData.put("salary", "50000");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EmployeesUtil.validateEmployeeInputData(employeeData);
        });

        assertEquals("Invalid age : Employee age is a mandatory field.", exception.getMessage());
    }

    @Test
    void testValidateEmployeeAge_InvalidAge() {
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("name", "John Doe");
        employeeData.put("age", "-5");
        employeeData.put("salary", "50000");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EmployeesUtil.validateEmployeeInputData(employeeData);
        });

        assertEquals("Invalid age: must be provided within valid age range.", exception.getMessage());
    }

    @Test
    void testValidateEmployeeSalary_MissingSalary() {
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("name", "John Doe");
        employeeData.put("age", "30");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EmployeesUtil.validateEmployeeInputData(employeeData);
        });

        assertEquals("Invalid salary : Employee salary is a mandatory field.", exception.getMessage());
    }

    @Test
    void testValidateEmployeeSalary_InvalidSalary() {
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("name", "John Doe");
        employeeData.put("age", "30");
        employeeData.put("salary", "abc");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EmployeesUtil.validateEmployeeInputData(employeeData);
        });

        assertEquals("Invalid salary: must be provided as valid number.", exception.getMessage());
    }

    @Test
    void testValidateEmployeeSalary_NegativeSalary() {
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("name", "John Doe");
        employeeData.put("age", "30");
        employeeData.put("salary", "-5000");

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            EmployeesUtil.validateEmployeeInputData(employeeData);
        });

        assertEquals("Invalid salary: must be provided as valid number.", exception.getMessage());
    }

    @Test
    void testValidEmployeeData() {
        Map<String, Object> employeeData = new HashMap<>();
        employeeData.put("name", "John Doe");
        employeeData.put("age", "30");
        employeeData.put("salary", "50000");

        assertDoesNotThrow(() -> {
            EmployeesUtil.validateEmployeeInputData(employeeData);
        });
    }
}

