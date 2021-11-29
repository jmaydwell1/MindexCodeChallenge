package com.mindex.challenge.data;

import java.util.List;

public class ReportingStructure {
    private String employeeId;
    private Employee employee;
    private int numberOfReports;

    public String getEmployeeId() {

        return employeeId;
    }

    public void setEmployeeId(String employeeId) {

        this.employeeId = employeeId;
    }


    public ReportingStructure() {
    }

    public Employee getEmployee() {

        return employee;
    }

    public void setEmployee(Employee employee) {

        this.employee = employee;
    }

    public int getNumberOfReports() {

        return numberOfReports;
    }

    public void setNumberOfReports(int numberOfReports) {

        this.numberOfReports = numberOfReports;
    }
}
