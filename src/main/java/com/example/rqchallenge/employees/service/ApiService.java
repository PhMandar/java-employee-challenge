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
    private String apiUrl;

    // public Employee callThirdPartyApi(String id) {
    // logger.info("Calling rest api endpoint for searching employee by id : " +
    // id);
    // String url = apiUrl + id;
    // return restTemplate.getForObject(url, Employee.class);
    // }

    public String deleteEmployee(String id) {
        logger.info("Calling rest api endpoint for deleting employee by id : " + id);
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
        logger.info("Calling rest api endpoint for creating employee");
        Employee objEmployee = restTemplate.postForObject(apiUrl,
                new Employee(pName, Double.parseDouble(pSalary), Integer.parseInt(pAge)), Employee.class);
        if (objEmployee != null) {
            logger.info("Employee with employee id : {} get created ", objEmployee.getId());
        }
        return objEmployee;
    }

    public Employee getEmployeeById(String id) {
        logger.info("Calling getEmployeeById using endpoint for getting employee by id : " + id);
        String url = apiUrl + id;
        return restTemplate.getForObject(url, Employee.class);
    }

    public List<Employee> getEmployeesByNameSearch(String targetName) {
        List<Employee> employees = getAllEmployees();
        
        List<Employee> matchingEmployees = employees.stream()
            .filter(employee -> employee.getName().equals(targetName))
            .collect(Collectors.toList());

        return matchingEmployees;
    }

    public List<Employee> getAllEmployees() {
        logger.info("\nCalling rest api endpoint for all employees\n");
        
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
        
        if(employees != null) {
            return Arrays.asList(employees);
        } else {
            return Collections.emptyList();
        }
    }

    public Double getHighestSalaryOfEmployees() {
        List<Employee> employees = getAllEmployees();
        logger.info("\n\n~~~~~~~~~~~~ Got list of employees with size : {} ~~~~~~~~~~~~\n\n", employees.size());
        double highestSalary = employees.stream()
                .mapToDouble(Employee::getSalary)
                .max()
                .orElse(0);

        logger.info("\n\n~~~~~~~~~~~~ Higest salary : {} ~~~~~~~~~~~~\n\n", highestSalary);
        return highestSalary;
    }

    public List<String> getTopTenHigestEarningEmployeeNames() {
        List<Employee> employees = getAllEmployees();
        logger.info("\n\n~~~~~~~~~~~~ Got list of employees with size : {} ~~~~~~~~~~~~\n\n", employees.size());
        
        PriorityQueue<Employee> topEmployees = new PriorityQueue<>(Comparator.comparingDouble(Employee::getSalary));
        logger.info("\n\n~~~~~~~~~~~~ getting top ten higest earning employee names ~~~~~~~~~~~~\n\n");
        
        employees.forEach(employee -> {
            topEmployees.offer(employee);
            if (topEmployees.size() > 10) {
                topEmployees.poll();
            }
        });

        List<String> topEarningEmployeeNames = topEmployees.stream()
            .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
            .map(Employee::getName)
            .collect(Collectors.toList());
        return topEarningEmployeeNames;
    }
}
