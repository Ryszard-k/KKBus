package com.pz.KKBus.Staff.Model.Repositories.Courses;

import com.pz.KKBus.Customer.Model.Enums.Route;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.Courses.Courses;
import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import com.pz.KKBus.Staff.Model.Entites.Courses.StopPassengersPair;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Repositories.CarRepo;
import com.pz.KKBus.Staff.Model.Repositories.EmployeesRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
class CoursesRepoTest {

    @Autowired
    private CoursesRepo coursesRepo;

    @Autowired
    private EmployeesRepo employeesRepo;

    @Autowired
    private CarRepo carRepo;

    @Autowired
    private ReportRepo reportRepo;

    @Autowired
    private StopPassengersPairRepo stopPassengersPairRepo;

    private List<Employees> employeesList() {
        List<Employees> employees = new ArrayList<>();

        employees.add(new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.OfficeWorker, 5000));
        employees.add(new Employees((long) 2,"Anna",
                "Nowak", LocalDate.parse("1997-05-20"), Role.Driver, 3000));
        employees.add(new Employees((long) 3,"Marek",
                "Markowski", LocalDate.parse("1997-05-20"), Role.Driver, 3000));
        return employees;
    }

    private List<Car> carList() {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car((long) 4, "Mercedes", "Benz", 30, LocalDate.parse("2000-02-23")));
        cars.add(new Car((long) 5, "Volksvagen", "Crafter", 32, LocalDate.parse("2008-06-23")));
        cars.add(new Car((long) 6, "Temsa", "Safari", 64, LocalDate.parse("2010-04-01")));
        return cars;
    }

    private List<Report> reportList() {
        List<Report> reports = new ArrayList<>();
        reports.add(new Report((long) 10, 100, 30, 430,
                coursesList().get(0)));
        reports.add(new Report((long) 11, 120, 32, 450,
                coursesList().get(1)));
        reports.add(new Report((long) 12, 140, 64, 470,
                coursesList().get(2)));
        return reports;
    }

    private List<StopPassengersPair> stopPassengersPairList() {
        List<StopPassengersPair> stopPassengersPairs = new ArrayList<>();
        stopPassengersPairs.add(new StopPassengersPair((long) 1, "Przystanek1", 5, reportList().get(0)));
        stopPassengersPairs.add(new StopPassengersPair((long) 2, "Przystanek2", 5, reportList().get(0)));
        stopPassengersPairs.add(new StopPassengersPair((long) 3, "Przystanek1", 10, reportList().get(1)));
        stopPassengersPairs.add(new StopPassengersPair((long) 1, "Przystanek2", 15, reportList().get(1)));
        stopPassengersPairs.add(new StopPassengersPair((long) 2, "Przystanek1", 10, reportList().get(2)));
        stopPassengersPairs.add(new StopPassengersPair((long) 3, "Przystanek2", 20, reportList().get(2)));
        return stopPassengersPairs;
    }

    private List<Courses> coursesList() {
        List<Courses> courses = new ArrayList<>();
        courses.add(new Courses((long) 7, LocalDate.now(), Route.KrakowToKatowice, LocalTime.parse("08:11"),
                carList().get(0), employeesList().get(1)));
        courses.add(new Courses((long) 8, LocalDate.now().minusDays(1), Route.KrakowToKatowice, LocalTime.parse("11:11"),
                carList().get(1), employeesList().get(1)));
        courses.add(new Courses((long) 9, LocalDate.now().minusDays(2), Route.KatowiceToKrakow, LocalTime.parse("18:51"),
                carList().get(2), employeesList().get(2)));
        return courses;
    }

    @BeforeEach
    void setUp() {
        employeesRepo.saveAll(employeesList());
        carRepo.saveAll(carList());
        coursesRepo.saveAll(coursesList());
        reportRepo.saveAll(reportList());
        stopPassengersPairRepo.saveAll(stopPassengersPairList());
    }

    @Test
    void findByDateAndRoute() {
        List<Courses> reports = coursesRepo.findByDateAndRoute(LocalDate.now().minusDays(2), Route.KatowiceToKrakow);

        assertEquals(1, reports.size());
        assertEquals(reports.get(0).getReport(), coursesList().get(2).getReport());
    }
}