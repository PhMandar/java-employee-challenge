package com.example.rqchallenge.employees.service;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.example.rqchallenge.employees.model.Employee;

@Service
public class ApiService {

    private static final Logger logger = LoggerFactory.getLogger(ApiService.class);

    @Autowired
    private RestTemplate restTemplate;

    @Value("${third.party.api.url}")
    public String apiUrl;

    public String deleteEmployee(String id) {
        logger.info("ApiService.deleteEmployee() with id : {}", id);
        String urlToDeleteEmployee = apiUrl + id;
        String resp = "Failed";
        try {
            ResponseEntity<String> response = restTemplate.exchange(urlToDeleteEmployee, HttpMethod.DELETE, null,
                    String.class);
            logger.info("Employee with employee id : {} deleted ", id);
            resp = response.getStatusCode() == HttpStatus.OK ? "Success" : resp;
        } catch (HttpClientErrorException e) {
            logger.error(e.getMessage(), e);
        }
        return resp;
    }

    public Employee createEmployee(String pName, String pSalary, String pAge) {
        logger.info("ApiService.createEmployee()");
        Employee objEmployee = restTemplate.postForObject(apiUrl,
                new Employee(pName, Double.parseDouble(pSalary), Integer.parseInt(pAge)), Employee.class);
        if (objEmployee != null) {
            logger.info("Employee with employee id : {} get created ", objEmployee.getId());
        }
        return objEmployee;
    }

    public Employee getEmployeeById(String id) {
        logger.info("ApiService.getEmployeeById() with id : {}", id);
        String url = apiUrl + id;
        return restTemplate.getForObject(url, Employee.class);
    }

    public List<Employee> getEmployeesByNameSearch(String targetName) {
        logger.info("ApiService.getEmployeesByNameSearch() with name : {}", targetName);
        List<Employee> employees = getAllEmployees();
        
        List<Employee> matchingEmployees = employees.stream()
            .filter(employee -> employee.getName().equals(targetName))
            .collect(Collectors.toList());

        return matchingEmployees;
    }

    public List<Employee> getAllEmployees() {
        logger.info("ApiService.getAllEmployees()");
        Employee[] employees = null;
        try {
            ResponseEntity<Employee[]> empArrayResp = restTemplate.getForEntity(apiUrl, Employee[].class);
            if (empArrayResp != null) {
                if (empArrayResp.getBody() != null) {
                    employees = empArrayResp.getBody();
                } else {
                    // Handling when the body is null
                    logger.info("Response body is null.");
                    employees = new Employee[]{};
                }
            }
        } catch (RestClientException rce) {
            logger.error(rce.getMessage(), rce);
            return Collections.emptyList();
        }
        
        return (employees != null) ? Arrays.asList(employees) : Collections.emptyList(); 
        // if(employees != null) {
        //     return Arrays.asList(employees);
        // } else {
        //     return Collections.emptyList();
        // }
    }

    public Double getHighestSalaryOfEmployees() {
        logger.info("ApiService.getHighestSalaryOfEmployees()");
        List<Employee> employees = getAllEmployees();
        logger.info("Got list of employees with size : {}", employees.size());
        double highestSalary = employees.stream()
                .mapToDouble(Employee::getSalary)
                .max()
                .orElse(0);
        logger.info("Higest salary : {}", highestSalary);
        return highestSalary;
    }

    public List<String> getTopTenHigestEarningEmployeeNames() {
        logger.info("ApiService.getTopTenHigestEarningEmployeeNames()");
        List<Employee> employees = getAllEmployees();
        logger.info("Got list of employees with size : {}", employees.size());
        PriorityQueue<Employee> topEmployees = new PriorityQueue<>(Comparator.comparingDouble(Employee::getSalary));
        logger.info("Getting top ten higest earning employee names");
        
        employees.forEach(employee -> {
            topEmployees.offer(employee);
            if (topEmployees.size() > 10) {
                topEmployees.poll();
            }
        });

        return topEmployees.stream()
            .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
            .map(Employee::getName)
            .collect(Collectors.toList());
    }
}
