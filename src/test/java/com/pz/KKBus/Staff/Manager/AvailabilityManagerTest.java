package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Staff.Model.Entites.Availability;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Repositories.AvailabilityRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class AvailabilityManagerTest {

    @Mock
    private AvailabilityRepo availabilityRepo;

    @InjectMocks
    private AvailabilityManager availabilityManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Availability> availabilityList() {
        List<Availability> availabilities = new ArrayList<>();
        availabilities.add(new Availability((long) 1, LocalDate.parse("2021-02-24"), employeesList().get(0)));
        availabilities.add(new Availability((long) 2, LocalDate.parse("2021-05-14"), employeesList().get(0)));
        availabilities.add(new Availability((long) 3, LocalDate.parse("2021-09-11"), employeesList().get(1)));
        return availabilities;
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
        when(availabilityRepo.findAll()).thenReturn(availabilityList());

        List<Availability> availabilities = availabilityRepo.findAll();

        assertEquals(3, availabilities.size());
        verify(availabilityRepo, times(1)).findAll();
    }

    @Test
    void findAll_with_null() {
        when(availabilityRepo.findAll()).thenReturn(null);

        List<Availability> availabilities = availabilityRepo.findAll();

        assertNull(availabilities);
        verify(availabilityRepo, times(1)).findAll();
    }

    @Test
    void findById() {
        when(availabilityRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(availabilityList().get(0)));

        Optional<Availability> availability = availabilityManager.findById(1L);

        assertEquals(availabilityList().get(0).getId(), availability.get().getId());
        assertEquals(availabilityList().get(0).getDate(), availability.get().getDate());
        assertEquals(availabilityList().get(0).getEmployees().getFirstName(), availability.get().getEmployees().getFirstName());

        verify(availabilityRepo, times(1)).findById(1L);
    }

    @Test
    void findById_not_found() {
        when(availabilityRepo.findById(anyLong())).thenReturn(null);

        Optional<Availability> availability = availabilityManager.findById(1L);

        assertNull(availability);
        verify(availabilityRepo, times(1)).findById(1L);
    }

    @Test
    void saveAll() {

        when(availabilityRepo.saveAll(anyList())).thenReturn(availabilityList());

        List<Availability> availabilities = availabilityManager.saveAll(availabilityList());

        assertEquals(3, availabilities.size());
        assertEquals(availabilities.get(0), availabilityList().get(0));
        verify(availabilityRepo, times(1)).saveAll(availabilityList());
    }

    @Test
    void saveAll_emptyList() {

        when(availabilityRepo.saveAll(anyList())).thenReturn(List.of());

        List<Availability> availabilities = availabilityManager.saveAll(List.of());

        assertEquals(availabilities, List.of());
        verify(availabilityRepo, times(1)).saveAll(List.of());
    }

    @Test
    void deleteAll() {
        when(availabilityRepo.existsById(1L)).thenReturn(true);
        when(availabilityRepo.existsById(2L)).thenReturn(true);
        when(availabilityRepo.existsById(3L)).thenReturn(true);

        List<Availability> availabilities = availabilityManager.deleteAll(availabilityList());

        assertEquals(3, availabilities.size());

        verify(availabilityRepo, times(3)).existsById(anyLong());
        verify(availabilityRepo, times(1)).deleteAll(anyList());
    }

    @Test
    void deleteAll_with2Exist() {
        when(availabilityRepo.existsById(1L)).thenReturn(true);
        when(availabilityRepo.existsById(2L)).thenReturn(false);
        when(availabilityRepo.existsById(3L)).thenReturn(true);

        List<Availability> availabilities = availabilityManager.deleteAll(availabilityList());

        assertEquals(2, availabilities.size());
        assertEquals(availabilities.get(0), availabilityList().get(0));
        assertEquals(availabilities.get(1), availabilityList().get(2));

        verify(availabilityRepo, times(3)).existsById(anyLong());
        verify(availabilityRepo, times(1)).deleteAll(anyList());
    }

    @Test
    void deleteAll_with0Exist() {
        when(availabilityRepo.existsById(1L)).thenReturn(false);
        when(availabilityRepo.existsById(2L)).thenReturn(false);
        when(availabilityRepo.existsById(3L)).thenReturn(false);

        List<Availability> availabilities = availabilityManager.deleteAll(availabilityList());

        assertEquals(0, availabilities.size());

        verify(availabilityRepo, times(3)).existsById(anyLong());
        verify(availabilityRepo, times(1)).deleteAll(anyList());
    }
}