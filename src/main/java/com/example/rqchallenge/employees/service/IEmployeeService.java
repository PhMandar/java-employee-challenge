package com.example.rqchallenge.employees.service;

import java.util.List;

import com.example.rqchallenge.employees.model.Employee;

public interface IEmployeeService {
    List<Employee> getAllEmployees();

    List<Employee> getEmployeesByNameSearch(String employeeName);

    Employee getEmployeeById(String id);

    Double getHighestSalaryOfEmployees();

    List<String> getTop10HighestEarningEmployeeNames();

    Employee createEmployee(String name, String salary, String age);

    String deleteEmployee(String id);
}
