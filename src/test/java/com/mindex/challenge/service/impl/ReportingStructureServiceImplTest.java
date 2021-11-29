package com.mindex.challenge.service.impl;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReportingStructureServiceImplTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String employeeUrl;
    private String reportingStructureUrl;

    /**
     * Create test data
     */
    private HashMap<String, Employee> testEmployees = new HashMap<>();
    {
        Employee boss = new Employee();
        boss.setFirstName("Mr.");
        boss.setLastName("manager");
        boss.setDepartment("Engineering");
        boss.setPosition("Manager");
        testEmployees.put("boss", boss);

        for (int i=0; i<3; i++) {
            Employee drone = new Employee();
            drone.setFirstName("Drone");
            drone.setLastName(String.valueOf(i));
            drone.setDepartment("Engineering");
            drone.setPosition("Drone");
            testEmployees.put("drone" + String.valueOf(i), drone);
        }
    }

    /**
     * Test setup: Add test employees to the database
     */
    @Before
    public void setup() {

        employeeUrl = "http://localhost:" + port + "/employee";
        reportingStructureUrl = "http://localhost:" + port +"/reportingStructure/{id}";

        // Create Drone 3 with no subordinates
        Employee drone3 = restTemplate.postForEntity(employeeUrl, testEmployees.get("drone3"), Employee.class).getBody();

        // Create Drone 2 with no subordinates
        Employee drone2 = restTemplate.postForEntity(employeeUrl, testEmployees.get("drone2"), Employee.class).getBody();

        // Add Drones 3 and 2 as subordinates to Drone 1
        ArrayList<Employee> drone1Reports = new ArrayList<>();
        drone1Reports.add(drone3);
        drone1Reports.add(drone2);
        testEmployees.get("drone1").setDirectReports(drone1Reports);

        // Create Drone 1 with 2 subordinates
        Employee drone1 = restTemplate.postForEntity(employeeUrl, testEmployees.get("drone1"), Employee.class).getBody();

        // Add Drone 1 as subordinate to Boss
        ArrayList<Employee> bossReports = new ArrayList<>();
        bossReports.add(drone1);
        testEmployees.get("boss").setDirectReports(bossReports);

        // Create Boss
        Employee boss = restTemplate.postForEntity(employeeUrl, testEmployees.get("boss"), Employee.class).getBody();

        // Update test data structure with new entities (including IDs)
        testEmployees.put("boss", boss);
        testEmployees.put("drone1", drone1);
        testEmployees.put("drone2", drone2);
        testEmployees.put("drone3", drone3);
    }

    /**
     * Asserts that the correct ReportingStructure is returned for an employee with no direct reports.
     */
    @Test
    public void testReportingStructureNoReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ReportingStructure reports =
                restTemplate.getForEntity(reportingStructureUrl,
                        ReportingStructure.class,
                        testEmployees.get("drone3").getEmployeeId()).getBody();
        assertEquals(0, reports.getNumberOfReports());
    }

    /**
     * Asserts that the correct ReportingStructure is returned for an employee with 2 direct reports.
     */
    @Test
    public void testReportingStructureTwoReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ReportingStructure reports =
                restTemplate.getForEntity(reportingStructureUrl,
                        ReportingStructure.class,
                        testEmployees.get("drone1").getEmployeeId()).getBody();
        System.out.println(reports.getEmployeeId());
        assertEquals(2, reports.getNumberOfReports());
    }

    /**
     * Asserts that the correct ReportingStructure is returned for an employee with 1 direct report
     * who has 2 direct reports.
     */
    @Test
    public void testReportingStructureThreeRecursiveReports() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        ReportingStructure reports =
                restTemplate.getForEntity(reportingStructureUrl,
                        ReportingStructure.class,
                        testEmployees.get("boss").getEmployeeId()).getBody();
        assertEquals(3, reports.getNumberOfReports());
    }
}
