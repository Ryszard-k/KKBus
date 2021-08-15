package com.pz.KKBus.Staff.Model.Repositories;

import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Entites.Schedule.Schedule;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Repositories.Schedule.ScheduleRepo;
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

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@DataJpaTest
class ScheduleRepoTest {

    @Autowired
    private ScheduleRepo scheduleRepo;

    @Autowired
    private EmployeesRepo employeesRepo;

    private List<Schedule> scheduleList() {
        List<Schedule> schedule = new ArrayList<>();
        schedule.add(new Schedule((long) 1, LocalDate.parse("2021-12-22"), LocalTime.parse("08:10"), LocalTime.parse("14:15"),
                employeesList().get(0)));
        schedule.add(new Schedule((long) 2, LocalDate.parse("2021-12-23"), LocalTime.parse("09:15"), LocalTime.parse("17:15"),
                employeesList().get(0)));
        schedule.add(new Schedule((long) 3, LocalDate.parse("2021-12-24"), LocalTime.parse("12:10"), LocalTime.parse("18:15"),
                employeesList().get(1)));
        return schedule;
    }

    private List<Employees> employeesList() {
        List<Employees> employees = new ArrayList<>();

        employees.add(new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000));
        employees.add(new Employees((long) 2,"Anna",
                "Nowak", LocalDate.parse("1997-05-20"), Role.OfficeWorker, 3000));
        employees.add(new Employees((long) 3,"Marek",
                "Markowski", LocalDate.parse("1997-05-20"), Role.Driver, 3000));
        return employees;
    }

    @BeforeEach
    void setUp() {
        employeesRepo.saveAll(employeesList());
        scheduleRepo.saveAll(scheduleList());
    }

    @Test
    void findByEmployeesSchedule() {

        List<Schedule> schedules = scheduleRepo.findByEmployeesSchedule(employeesList().get(0));

        assertEquals(2, schedules.size());
        assertEquals(scheduleList().get(0).getWorkDate(), schedules.get(0).getWorkDate());
        assertEquals(scheduleList().get(1).getWorkDate(), schedules.get(1).getWorkDate());
    }

    @Test
    void findByEmployeesSchedule_notFoundSchedule() {

        List<Schedule> schedules = scheduleRepo.findByEmployeesSchedule(employeesList().get(2));

        assertTrue(schedules.isEmpty());
    }

    @Test
    void findAllByWorkDateBetween() {
        List<Schedule> schedules = scheduleRepo.findAllByWorkDateBetween(LocalDate.parse("2021-12-22"),
                LocalDate.parse("2021-12-28"));

        assertEquals(3, schedules.size());
        assertEquals(scheduleList().get(0).getWorkDate(), schedules.get(0).getWorkDate());
        assertEquals(scheduleList().get(1).getWorkDate(), schedules.get(1).getWorkDate());
    }

    @Test
    void findAllByWorkDateBetween_withNotProperDate() {
        List<Schedule> schedules = scheduleRepo.findAllByWorkDateBetween(LocalDate.parse("2021-11-22"),
                LocalDate.parse("2021-11-28"));

        assertTrue(schedules.isEmpty());
    }

    @Test
    void findAllByEmployeesAndWorkDateBetween() {
        List<Schedule> schedules = scheduleRepo.findAllByEmployeesAndWorkDateBetween(employeesList().get(0),
                LocalDate.parse("2021-12-12"), LocalDate.parse("2021-12-23"));

        assertEquals(2, schedules.size());
        assertEquals(scheduleList().get(0).getWorkDate(), schedules.get(0).getWorkDate());
        assertEquals(scheduleList().get(1).getWorkDate(), schedules.get(1).getWorkDate());
    }

    @Test
    void findAllByEmployeesAndWorkDateBetween_notProperEmployee() {
        List<Schedule> schedules = scheduleRepo.findAllByEmployeesAndWorkDateBetween(employeesList().get(2),
                LocalDate.parse("2021-12-12"), LocalDate.parse("2021-12-23"));

        assertTrue(schedules.isEmpty());
    }

    @Test
    void findAllByEmployeesAndWorkDateBetween_notProperDate() {
        List<Schedule> schedules = scheduleRepo.findAllByEmployeesAndWorkDateBetween(employeesList().get(0),
                LocalDate.parse("2021-11-12"), LocalDate.parse("2021-11-23"));

        assertTrue(schedules.isEmpty());
    }
}