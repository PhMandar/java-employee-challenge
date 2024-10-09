package com.example.rqchallenge.employees.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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
import com.example.rqchallenge.employees.service.ApiService;
import com.example.rqchallenge.employees.util.EmployeesUtil;

@RestController
@RequestMapping("/employees/api/v1")
public class EmployeeController implements IEmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    public ApiService apiService;

    @PostMapping("/create")
    @Override
    public ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput) {
        logger.info("EmployeeController.createEmployee()");
        EmployeesUtil.validateEmployeeInputData(employeeInput);

        String name = employeeInput.get("name").toString();
        String age = employeeInput.get("age").toString();
        String salary = employeeInput.get("salary").toString();

        Employee empCreated = apiService.createEmployee(name, salary, age);
        HttpStatus status = empCreated != null ? HttpStatus.CREATED : HttpStatus.valueOf(501);
        return new ResponseEntity<Employee>(empCreated, status);
    }

    @DeleteMapping("/delete/{id}")
    @Override
    public ResponseEntity<String> deleteEmployeeById(@PathVariable("id") String id) {
        logger.info("EmployeeController.deleteEmployeeById()");
        try {
            Integer.parseInt(id);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid id : must be provided valid id");
        }
        String deleteEmployeeStatus = apiService.deleteEmployee(id);
        logger.info("Delete Employee Status : {}", deleteEmployeeStatus);
        HttpStatus status = "Success".equalsIgnoreCase(deleteEmployeeStatus) ? HttpStatus.OK
                : HttpStatus.EXPECTATION_FAILED;
        return new ResponseEntity<String>(deleteEmployeeStatus, status);
    }

    @GetMapping("/employees")
    @Override
    public ResponseEntity<List<Employee>> getAllEmployees() throws IOException {
        logger.info("EmployeeController.getAllEmployees()");
        return new ResponseEntity<List<Employee>>(apiService.getAllEmployees(), HttpStatus.OK);
    }

    @GetMapping("/employees/{id}")
    @Override
    public ResponseEntity<Employee> getEmployeeById(@PathVariable("id") String id) {
        logger.info("EmployeeController.getEmployeeById()");
        try {
            Integer.parseInt(id);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid id : must be provided valid id");
        }
        return ResponseEntity.ok(apiService.getEmployeeById(id));
    }

    @GetMapping("/search/{emp_name}")
    @Override
    public ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable("emp_name") String empName) {
        logger.info("EmployeeController.getEmployeesByNameSearch() with employee name : {}", empName);
        return ResponseEntity.ok(apiService.getEmployeesByNameSearch(empName));
    }

    @GetMapping("/highestSalary")
    @Override
    public ResponseEntity<Double> getHighestSalaryOfEmployees() {
        logger.info("EmployeeController.getHighestSalaryOfEmployees()");
        return ResponseEntity.ok(apiService.getHighestSalaryOfEmployees());
    }

    @GetMapping("/topTenHighestEarningEmployeeNames")
    @Override
    public ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames() {
        logger.info("EmployeeController.getTopTenHighestEarningEmployeeNames()");
        return ResponseEntity.ok(apiService.getTopTenHigestEarningEmployeeNames());
    }
}