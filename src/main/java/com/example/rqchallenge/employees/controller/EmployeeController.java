package com.example.rqchallenge.employees.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.service.EmployeeService;

@RestController
@RequestMapping("/employees/api/v1")
public class EmployeeController implements IEmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    public EmployeeService employeeService;

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        logger.info("Calling EmployeeController.getAllEmployees()");
        return new ResponseEntity<List<Employee>>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/search/{emp_name}")
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@Valid @PathVariable("emp_name") String emp_name) {
        return ResponseEntity.ok(employeeService.getEmployeesByNameSearch(emp_name));
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeById(@Valid @PathVariable("id") String id) {
        logger.info("Calling EmployeeController.getEmployeeById()");
        try {
            Integer.parseInt(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid id : must be provided valid id");
        }
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @GetMapping("/highestSalary")
    public ResponseEntity<Double> getHighestSalaryOfEmployees() {
        return ResponseEntity.ok(employeeService.getHighestSalaryOfEmployees());
    }

    @GetMapping("/topTenHighestEarningEmployeeNames")
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        return ResponseEntity.ok(employeeService.getTop10HighestEarningEmployeeNames());
    }

    @PostMapping("/create")
    public ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput) {
        validateEmployeeInputData(employeeInput);

        String name = employeeInput.get("name").toString();
        String age = employeeInput.get("age").toString();
        String salary = employeeInput.get("salary").toString();

        Employee empCreated = employeeService.createEmployee(name, salary, age);
        HttpStatus status = empCreated != null ? HttpStatus.CREATED : HttpStatus.valueOf(501);
        return new ResponseEntity<Employee>(empCreated, status);
    }

    private void validateEmployeeInputData(Map<String, Object> employeeData) {
        if(!employeeData.containsKey("name")) {
            throw new IllegalArgumentException("Invalid name : Employee name is a mandatory field.");
        } else {
            String name = employeeData.get("name").toString();
            if (name == null || name.isBlank()) {
                throw new IllegalArgumentException("Invalid name: must be provided as a valid string.");
            }
        }

        if(!employeeData.containsKey("age")) {
            throw new IllegalArgumentException("Invalid age : Employee age is a mandatory field.");
        } else {
            String age = employeeData.get("age").toString();
            try {
                Integer empAge = Integer.parseInt(age);
                if(empAge <= 0) {
                    throw new IllegalArgumentException("Invalid age: must be provided as a valid age.");
                }
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("Invalid age: must be provided as positive number.");
            }
        }
        
        if(!employeeData.containsKey("salary")) {
            throw new IllegalArgumentException("Invalid salary : Employee salary is a mandatory field.");
        } else {
            String salary = employeeData.get("salary").toString();
            try {
                Double empSalary = Double.parseDouble(salary);
    
                if (empSalary < 0) {
                    throw new IllegalArgumentException("Invalid salary: must be provided as valid number.");
                }    
            } catch (Exception e) {
                throw new IllegalArgumentException("Invalid salary: must be provided as valid number.");
            }
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteEmployeeById(@Valid @PathVariable("id") String id) {
        try {
            Integer.parseInt(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid id : must be provided valid id");
        }
        String deleteEmployeeStatus = employeeService.deleteEmployee(id);
        HttpStatus status = "Success".equalsIgnoreCase(deleteEmployeeStatus) ? HttpStatus.OK : HttpStatus.EXPECTATION_FAILED;
        return new ResponseEntity<String>(deleteEmployeeStatus, status);
    }

}
