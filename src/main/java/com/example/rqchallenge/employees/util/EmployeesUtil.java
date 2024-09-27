package com.example.rqchallenge.employees.util;

import java.util.Map;

public class EmployeesUtil {
    public static void validateEmployeeInputData(Map<String, Object> employeeData) {
        validateEmployeeName(employeeData);

        validateEmployeeAge(employeeData);
        
        validateEmployeeSalary(employeeData);
    }

    private static void validateEmployeeSalary(Map<String, Object> employeeData) {
        if(!employeeData.containsKey("salary")) {
            throw new IllegalArgumentException("Invalid salary : Employee salary is a mandatory field.");
        } else {
            String salary = employeeData.get("salary").toString();
            try {
                Double empSalary = Double.parseDouble(salary);
    
                if (empSalary < 0) {
                    throw new IllegalArgumentException("Invalid salary: must be provided as valid number.");
                }    
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid salary: must be provided as valid number.");
            }
        }
    }

    private static void validateEmployeeAge(Map<String, Object> employeeData) {
        if(!employeeData.containsKey("age")) {
            throw new IllegalArgumentException("Invalid age : Employee age is a mandatory field.");
        } else {
            String age = employeeData.get("age").toString();
            try {
                Integer empAge = Integer.parseInt(age);
                if(empAge <= 0 || empAge > 130) {
                    throw new IllegalArgumentException("Invalid age: must be provided within valid age range.");
                }
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Invalid age: must be provided as positive integer.");
            }
        }
    }

    private static void validateEmployeeName(Map<String, Object> employeeData) {
        if (!employeeData.containsKey("name")) {
            throw new IllegalArgumentException("Invalid name : Employee name is a mandatory field.");
        }
    
        String name = (String) employeeData.get("name");
    
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Invalid name: Must be provided as a valid string.");
        }
    }
}
