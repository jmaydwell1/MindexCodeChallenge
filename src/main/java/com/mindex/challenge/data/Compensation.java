package com.mindex.challenge.data;

import java.util.Date;

public class Compensation {
    private String employeeId;
    private Employee employee;
    private double salary;
    private Date effectiveDate;

    public Compensation() {
    }

    public String getEmployeeId() {

        return employeeId;
    }

    public void setEmployeeId(String employeeId) {

        this.employeeId = employeeId;
    }


    public Employee getEmployee() {

        return employee;
    }

    public void setEmployee(Employee employee) {

        this.employee = employee;
    }

    public double getSalary() {

        return salary;
    }

    public void setSalary(double salary) {

        this.salary = salary;
    }

    public Date getEffectiveDate() {

        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {

        this.effectiveDate = effectiveDate;
    }
}
