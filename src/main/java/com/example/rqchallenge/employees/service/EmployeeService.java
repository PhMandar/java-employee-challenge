package com.example.rqchallenge.employees.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.rqchallenge.employees.exceptions.ResourceNotFoundException;
import com.example.rqchallenge.employees.model.Employee;
import com.example.rqchallenge.employees.repository.IEmployeeRepository;

@Service
public class EmployeeService implements IEmployeeService {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeService.class);

    @Autowired
    public IEmployeeRepository empRepo;

    public List<Employee> getAllEmployees() {
        logger.info("Calling getAllEmployees service call");
        List<Employee> employees = empRepo.findAll();
        logger.info("Total {} number of employees fetched.", employees.size());
        return employees;
    }

    public List<Employee> getEmployeesByNameSearch(String employeeName) {
        logger.info("Calling getEmployeesByNameSearch service call");
        return empRepo.searchEmployeeByName(employeeName);
    }

    public Employee getEmployeeById(String id) {
        logger.info("Calling getEmployeeById service call for id : {}", id);
        return empRepo.findById(Long.parseLong(id))
                .orElseThrow(() -> new ResourceNotFoundException("Requested employee with id : " + id + " not found"));
    }

    public Double getHighestSalaryOfEmployees() {
        logger.info("Calling getHighestSalaryOfEmployees service call");
        return empRepo.getHighestSalaryOfEmployees();
    }

    public List<String> getTop10HighestEarningEmployeeNames() {
        logger.info("Calling getTop10HighestEarningEmployeeNames service call");
        return empRepo.getTop10HighestEarningEmployeeNames();
    }

    public Employee createEmployee(String name, String salary, String age) {
        logger.info("Calling createEmployee service call for name : {}, salary : {}, age : {}", name, salary, age);
        Employee empToCreate = new Employee(name, Double.parseDouble(salary), Integer.parseInt(age));
        return empRepo.save(empToCreate);
    }

    public String deleteEmployee(String id) {
        logger.info("Calling deleteEmployee service call");
        try {
            getEmployeeById(id);
            empRepo.deleteById(Long.parseLong(id));
        } catch (IllegalArgumentException iae) {
            return "Failure in deleting employee";
        }
        return "Successfully deleted employee";
    }
}
