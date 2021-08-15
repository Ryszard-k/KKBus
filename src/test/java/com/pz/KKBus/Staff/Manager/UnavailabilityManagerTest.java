package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Staff.Manager.Schedule.UnavailabilityManager;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Entites.Schedule.Unavailability;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Repositories.Schedule.UnavailabilityRepo;
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
class UnavailabilityManagerTest {

    @Mock
    private UnavailabilityRepo unavailabilityRepo;

    @InjectMocks
    private UnavailabilityManager unavailabilityManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Unavailability> unavailabilityList() {
        List<Unavailability> unavailabilities = new ArrayList<>();
        unavailabilities.add(new Unavailability((long) 1, LocalDate.parse("2021-02-24"), employeesList().get(0)));
        unavailabilities.add(new Unavailability((long) 2, LocalDate.parse("2021-05-14"), employeesList().get(0)));
        unavailabilities.add(new Unavailability((long) 3, LocalDate.parse("2021-09-11"), employeesList().get(1)));
        return unavailabilities;
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
        when(unavailabilityRepo.findAll()).thenReturn(unavailabilityList());

        List<Unavailability> unavailabilities = unavailabilityRepo.findAll();

        assertEquals(3, unavailabilities.size());
        verify(unavailabilityRepo, times(1)).findAll();
    }

    @Test
    void findAll_with_null() {
        when(unavailabilityRepo.findAll()).thenReturn(null);

        List<Unavailability> unavailabilities = unavailabilityRepo.findAll();

        assertNull(unavailabilities);
        verify(unavailabilityRepo, times(1)).findAll();
    }

    @Test
    void findById() {
        when(unavailabilityRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(unavailabilityList().get(0)));

        Optional<Unavailability> unavailability = unavailabilityManager.findById(1L);

        assertEquals(unavailabilityList().get(0).getId(), unavailability.get().getId());
        assertEquals(unavailabilityList().get(0).getDate(), unavailability.get().getDate());
        assertEquals(unavailabilityList().get(0).getEmployeesUn().getFirstName(), unavailability.get().getEmployeesUn().getFirstName());

        verify(unavailabilityRepo, times(1)).findById(1L);
    }

    @Test
    void findById_not_found() {
        when(unavailabilityRepo.findById(anyLong())).thenReturn(null);

        Optional<Unavailability> unavailability = unavailabilityManager.findById(1L);

        assertNull(unavailability);
        verify(unavailabilityRepo, times(1)).findById(1L);
    }

    @Test
    void saveAll() {
        when(unavailabilityRepo.saveAll(anyList())).thenReturn(unavailabilityList());

        List<Unavailability> unavailabilities = unavailabilityManager.saveAll(unavailabilityList());

        assertEquals(3, unavailabilities.size());
        assertEquals(unavailabilities.get(0), unavailabilityList().get(0));
        verify(unavailabilityRepo, times(1)).saveAll(unavailabilityList());
    }

    @Test
    void saveAll_emptyList() {
        when(unavailabilityRepo.saveAll(anyList())).thenReturn(List.of());

        List<Unavailability> unavailabilities = unavailabilityManager.saveAll(List.of());

        assertEquals(unavailabilities, List.of());
        verify(unavailabilityRepo, times(1)).saveAll(List.of());
    }

    @Test
    void deleteAll() {
        when(unavailabilityRepo.existsById(1L)).thenReturn(true);
        when(unavailabilityRepo.existsById(2L)).thenReturn(true);
        when(unavailabilityRepo.existsById(3L)).thenReturn(true);

        List<Unavailability> unavailabilities = unavailabilityManager.deleteAll(unavailabilityList());

        assertEquals(3, unavailabilities.size());

        verify(unavailabilityRepo, times(3)).existsById(anyLong());
        verify(unavailabilityRepo, times(1)).deleteAll(anyList());
    }

    @Test
    void deleteAll_with2Exist() {
        when(unavailabilityRepo.existsById(1L)).thenReturn(true);
        when(unavailabilityRepo.existsById(2L)).thenReturn(false);
        when(unavailabilityRepo.existsById(3L)).thenReturn(true);

        List<Unavailability> unavailabilities = unavailabilityManager.deleteAll(unavailabilityList());

        assertEquals(2, unavailabilities.size());
        assertEquals(unavailabilities.get(0), unavailabilityList().get(0));
        assertEquals(unavailabilities.get(1), unavailabilityList().get(2));

        verify(unavailabilityRepo, times(3)).existsById(anyLong());
        verify(unavailabilityRepo, times(1)).deleteAll(anyList());
    }

    @Test
    void deleteAll_with0Exist() {
        when(unavailabilityRepo.existsById(1L)).thenReturn(false);
        when(unavailabilityRepo.existsById(2L)).thenReturn(false);
        when(unavailabilityRepo.existsById(3L)).thenReturn(false);

        List<Unavailability> unavailabilities = unavailabilityManager.deleteAll(unavailabilityList());

        assertEquals(0, unavailabilities.size());

        verify(unavailabilityRepo, times(3)).existsById(anyLong());
        verify(unavailabilityRepo, times(1)).deleteAll(anyList());
    }
}