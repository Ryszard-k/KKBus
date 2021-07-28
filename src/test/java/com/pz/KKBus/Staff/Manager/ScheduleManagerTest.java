package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Staff.Model.Entites.Availability;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Entites.Schedule;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Repositories.AvailabilityRepo;
import com.pz.KKBus.Staff.Model.Repositories.ScheduleRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class ScheduleManagerTest {

    @Mock
    private ScheduleRepo scheduleRepo;

    @InjectMocks
    private ScheduleManager scheduleManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Schedule> scheduleList() {
        List<Schedule> schedule = new ArrayList<>();
        schedule.add(new Schedule((long) 1, LocalDate.parse("2021-12-22"), LocalTime.parse("08:10"), LocalTime.parse("14:15"),
                employeesList().get(0)));
        schedule.add(new Schedule((long) 2, LocalDate.parse("2021-12-23"), LocalTime.parse("09:15"), LocalTime.parse("17:15"),
                employeesList().get(0)));
        schedule.add(new Schedule((long) 3, LocalDate.parse("2021-12-24"), LocalTime.parse("12:10"), LocalTime.parse("18:15"),
                employeesList().get(0)));
        return schedule;
    }

    private List<Employees> employeesList() {
        List<Employees> employees = new ArrayList<>();

        employees.add(new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000));
        employees.add(new Employees((long) 2,"Anna",
                "Nowak", LocalDate.parse("1997-05-20"), Role.OfficeWorker, 3000));
        return employees;
    }

    @Test
    void findAll() {
        when(scheduleRepo.findAll()).thenReturn(scheduleList());

        List<Schedule> schedules = scheduleRepo.findAll();

        assertEquals(3, schedules.size());
        verify(scheduleRepo, times(1)).findAll();
    }

    @Test
    void findAll_with_null() {
        when(scheduleRepo.findAll()).thenReturn(null);

        List<Schedule> schedules = scheduleRepo.findAll();

        assertNull(schedules);
        verify(scheduleRepo, times(1)).findAll();
    }

    @Test
    void findById() {
        when(scheduleRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(scheduleList().get(0)));

        Optional<Schedule> schedule = scheduleManager.findById(1L);

        assertEquals(scheduleList().get(0).getId(), schedule.get().getId());
        assertEquals(scheduleList().get(0).getWorkDate(), schedule.get().getWorkDate());
        assertEquals(scheduleList().get(0).getEmployeesSchedule().getFirstName(), schedule.get().getEmployeesSchedule().getFirstName());

        verify(scheduleRepo, times(1)).findById(1L);
    }

    @Test
    void findById_not_found() {
        when(scheduleRepo.findById(anyLong())).thenReturn(null);

        Optional<Schedule> schedule = scheduleManager.findById(1L);

        assertNull(schedule);
        verify(scheduleRepo, times(1)).findById(1L);
    }

    @Test
    void findByPeriodAll() {
        when(scheduleRepo.findAllByWorkDateBetween(LocalDate.now(), LocalDate.now().plusDays(3)))
                .thenReturn(scheduleList().subList(1, 3));

        List<Schedule> schedule = scheduleManager.findByPeriodAll(LocalDate.now(), LocalDate.now().plusDays(3));

        assertEquals(2, schedule.size());

        verify(scheduleRepo, times(1)).findAllByWorkDateBetween(LocalDate.now(), LocalDate.now().plusDays(3));
    }

    @Test
    void findByPeriodAll_withNull() {
        when(scheduleRepo.findAllByWorkDateBetween(LocalDate.now(), LocalDate.now().plusDays(3)))
                .thenReturn(null);

        List<Schedule> schedule = scheduleManager.findByPeriodAll(LocalDate.now(), LocalDate.now().plusDays(3));

        assertNull(schedule);

        verify(scheduleRepo, times(1)).findAllByWorkDateBetween(LocalDate.now(), LocalDate.now().plusDays(3));
    }

    @Test
    void findByPeriodForEmployee(){
        when(scheduleRepo.findAllByEmployeesAndWorkDateBetween(any(Employees.class), any(LocalDate.class),
                any(LocalDate.class))).thenReturn(scheduleList().subList(1, 3));

        List<Schedule> schedule = scheduleManager.findByPeriodForEmployee(employeesList().get(0), LocalDate.now(),
                LocalDate.now().plusDays(3));

        assertEquals(2, schedule.size());

        verify(scheduleRepo, times(1)).findAllByEmployeesAndWorkDateBetween(any(Employees.class), any(LocalDate.class),
                any(LocalDate.class));
    }

    @Test
    void findByPeriodForEmployee_withNullFoundedSchedule(){
        when(scheduleRepo.findAllByEmployeesAndWorkDateBetween(any(Employees.class), any(LocalDate.class),
                any(LocalDate.class))).thenReturn(null);

        List<Schedule> schedule = scheduleManager.findByPeriodForEmployee(employeesList().get(0), LocalDate.now(),
                LocalDate.now().plusDays(3));

        assertNull(schedule);

        verify(scheduleRepo, times(1)).findAllByEmployeesAndWorkDateBetween(any(Employees.class), any(LocalDate.class),
                any(LocalDate.class));
    }

    @Test
    void findByEmployee() {
        when(scheduleRepo.findByEmployeesSchedule(any(Employees.class))).thenReturn(scheduleList());

        List<Schedule> schedule = scheduleManager.findByEmployee(employeesList().get(0));

        assertEquals(scheduleList().get(0).getId(), schedule.get(0).getId());
        assertEquals(scheduleList().get(0).getWorkDate(), schedule.get(0).getWorkDate());
        assertEquals(scheduleList().get(0).getEmployeesSchedule().getFirstName(), schedule.get(0).getEmployeesSchedule().getFirstName());

        verify(scheduleRepo, times(1)).findByEmployeesSchedule(any(Employees.class));
    }

    @Test
    void findByEmployee_not_found() {
        when(scheduleRepo.findByEmployeesSchedule(any(Employees.class))).thenReturn(null);

        List<Schedule> schedule = scheduleManager.findByEmployee(employeesList().get(0));

        assertNull(schedule);
        verify(scheduleRepo, times(1)).findByEmployeesSchedule(any(Employees.class));
    }

    @Test
    void saveAll() {
        when(scheduleRepo.saveAll(anyList())).thenReturn(scheduleList());

        List<Schedule> schedule = scheduleManager.saveAll(scheduleList());

        assertEquals(3, schedule.size());
        assertEquals(schedule.get(0), scheduleList().get(0));
        verify(scheduleRepo, times(1)).saveAll(scheduleList());
    }

    @Test
    void saveAll_emptyList() {

        when(scheduleRepo.saveAll(anyList())).thenReturn(List.of());

        List<Schedule> schedule = scheduleManager.saveAll(List.of());

        assertEquals(schedule, List.of());
        verify(scheduleRepo, times(1)).saveAll(List.of());
    }

    @Test
    void deleteAll() {
        when(scheduleRepo.existsById(1L)).thenReturn(true);
        when(scheduleRepo.existsById(2L)).thenReturn(true);
        when(scheduleRepo.existsById(3L)).thenReturn(true);

        List<Schedule> schedule = scheduleManager.deleteAll(scheduleList());

        assertEquals(3, schedule.size());

        verify(scheduleRepo, times(3)).existsById(anyLong());
        verify(scheduleRepo, times(1)).deleteAll(anyList());
    }

    @Test
    void deleteAll_with2Exist() {
        when(scheduleRepo.existsById(1L)).thenReturn(true);
        when(scheduleRepo.existsById(2L)).thenReturn(false);
        when(scheduleRepo.existsById(3L)).thenReturn(true);

        List<Schedule> schedule = scheduleManager.deleteAll(scheduleList());

        assertEquals(2, schedule.size());
        assertEquals(schedule.get(0), scheduleList().get(0));
        assertEquals(schedule.get(1), scheduleList().get(2));

        verify(scheduleRepo, times(3)).existsById(anyLong());
        verify(scheduleRepo, times(1)).deleteAll(anyList());
    }

    @Test
    void deleteAll_with0Exist() {
        when(scheduleRepo.existsById(1L)).thenReturn(false);
        when(scheduleRepo.existsById(2L)).thenReturn(false);
        when(scheduleRepo.existsById(3L)).thenReturn(false);

        List<Schedule> schedule = scheduleManager.deleteAll(scheduleList());

        assertEquals(0, schedule.size());

        verify(scheduleRepo, times(3)).existsById(anyLong());
        verify(scheduleRepo, times(1)).deleteAll(anyList());
    }
}