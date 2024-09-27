package com.example.rqchallenge.employees.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Column(name = "name")
    private String name;

    @NotNull(message = "Salary cannot be null")
    @Min(value = 0, message = "Salary must be a positive number")
    @Column(name = "salary")
    private Double salary;

    @Min(value = 0, message = "Age must be a positive number")
    @Column(name = "age")
    private Integer age;

    public Employee() {
        // Default constructor
    }

    public Employee(String pName, Double pSalary, Integer pAge) {
        this.name = pName;
        this.salary = pSalary;
        this.age = pAge;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getSalary() {
        return salary;
    }

    public Integer getAge() {
        return age;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Employee [Id = " + getId()
                + ", Name = " + getName()
                + ", Salary =" + getSalary()
                + ", Age= " + getAge() + "]";
    }
}
