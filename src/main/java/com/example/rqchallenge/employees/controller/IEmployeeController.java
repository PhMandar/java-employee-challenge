package com.example.rqchallenge.employees.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.rqchallenge.employees.model.Employee;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface IEmployeeController {

    ResponseEntity<List<Employee>> getAllEmployees() throws IOException;

    ResponseEntity<List<Employee>> getEmployeesByNameSearch(@PathVariable String searchString);

    ResponseEntity<Employee> getEmployeeById(@PathVariable String id);

    ResponseEntity<Double> getHighestSalaryOfEmployees();

    ResponseEntity<List<String>> getTopTenHighestEarningEmployeeNames();

    ResponseEntity<Employee> createEmployee(@RequestBody Map<String, Object> employeeInput);

    ResponseEntity<String> deleteEmployeeById(@PathVariable String id);

}
