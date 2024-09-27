package com.example.rqchallenge.employees.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.rqchallenge.employees.model.Employee;

public interface IEmployeeRepository extends JpaRepository<Employee, Long>{
    @Query(value = "SELECT name FROM employees ORDER BY salary DESC LIMIT 10", nativeQuery = true)
    List<String> getTop10HighestEarningEmployeeNames();

    @Query(value = "SELECT MAX(salary) AS top_salary FROM employees LIMIT 1", nativeQuery = true)
    Double getHighestSalaryOfEmployees(); 

    @Query(value = "SELECT e.id, e.name, e.age, e.salary from employees e where LOWER(e.name) LIKE LOWER(CONCAT('%', :empName, '%'))", nativeQuery = true)
    List<Employee> searchEmployeeByName(@Param("empName") String empName);
}
