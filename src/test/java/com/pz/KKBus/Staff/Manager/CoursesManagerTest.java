package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowice;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.CarProperties;
import com.pz.KKBus.Staff.Model.Entites.Courses;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Enums.State;
import com.pz.KKBus.Staff.Model.Repositories.CoursesRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class CoursesManagerTest {

    @Mock
    private CoursesRepo coursesRepo;

    @InjectMocks
    private CoursesManager coursesManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Courses> coursesList() {
        List<Courses> courses = new ArrayList<>();
        KrakowToKatowice krakowToKatowice = new KrakowToKatowice((long) 1, "Przystanek1", null
                , 0, 0);
        KrakowToKatowice krakowToKatowice1 = new KrakowToKatowice((long) 2, "Przystanek2", null
                , 10, 40);

        KrakowToKatowiceDeparture krakowToKatowiceDeparture = new KrakowToKatowiceDeparture((long) 1, LocalTime.parse("08:01"),
                LocalTime.parse("11:47"), krakowToKatowice);
        KrakowToKatowiceDeparture krakowToKatowiceDeparture1 = new KrakowToKatowiceDeparture((long) 2, LocalTime.parse("13:01"),
                LocalTime.parse("16:47"), krakowToKatowice1);

        courses.add(new Courses((long) 1, LocalDate.parse("2021-04-23"),null ,
                krakowToKatowiceDeparture , carList().get(1), employeesList().get(0)));
        courses.add(new Courses((long) 2, LocalDate.parse("2021-04-23"),null ,
                null , carList().get(1), employeesList().get(0)));
        courses.add(new Courses((long) 3, LocalDate.parse("2021-04-23"),null ,
                krakowToKatowiceDeparture1 , carList().get(1), employeesList().get(0)));
        return courses;
    }

    private List<Car> carList() {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car((long) 1, "Mercedes", "Benz", 30, LocalDate.parse("2000-02-23")));
        cars.add(new Car((long) 2, "Volksvagen", "Crafter", 32, LocalDate.parse("2008-06-23")));
        cars.add(new Car((long) 3, "Temsa", "Safari", 64, LocalDate.parse("2010-04-01")));
        return cars;
    }

    private List<Employees> employeesList() {
        List<Employees> employees = new ArrayList<>();
        employees.add(new Employees((long) 1,"Jan",
                "Kowalski", LocalDate.parse("1983-02-23"), Role.Admin, 5000));
        employees.add(new Employees((long) 2,"Anna",
                "Nowak", LocalDate.parse("1997-05-20"), Role.OfficeWorker, 3000));
        employees.add(new Employees((long) 3,"Andrzej",
                "Konrad", LocalDate.parse("1988-05-20"), Role.Driver, 4000));
        return employees;
    }

    @Test
    void findAll() {
        when(coursesRepo.findAll()).thenReturn(coursesList());

        List<Courses> courses = coursesManager.findAll();

        assertEquals(3, courses.size());
        verify(coursesRepo, times(1)).findAll();
    }

    @Test
    void findAll_with_null() {
        when(coursesRepo.findAll()).thenReturn(null);

        List<Courses> courses = coursesManager.findAll();

        assertNull(courses);
        verify(coursesRepo, times(1)).findAll();
    }

    @Test
    void findById() {
        when(coursesRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(coursesList().get(0)));

        Optional<Courses> courses = coursesManager.findById(1L);

        assertEquals(coursesList().get(0).getId(), courses.get().getId());
        assertEquals(coursesList().get(0).getDate(), courses.get().getDate());
        assertEquals(coursesList().get(0).getDriver(), courses.get().getDriver());

        verify(coursesRepo, times(1)).findById(1L);
    }

    @Test
    void findById_not_found() {
        when(coursesRepo.findById(anyLong())).thenReturn(null);

        Optional<Courses> courses = coursesManager.findById(1L);

        assertNull(courses);
        verify(coursesRepo, times(1)).findById(1L);
    }

    @Test
    void findByDriver() {
        when(coursesRepo.findByDriver(any(Employees.class))).thenReturn(coursesList());

        List<Courses> courses = coursesManager.findByDriver(employeesList().get(0));

        assertEquals(coursesList().get(0).getId(), courses.get(0).getId());
        assertEquals(coursesList().get(0).getDate(), courses.get(0).getDate());
        assertEquals(coursesList().get(0).getDriver(), courses.get(0).getDriver());

        verify(coursesRepo, times(1)).findByDriver(any(Employees.class));
    }

    @Test
    void findByDriver_not_found() {
        when(coursesRepo.findByDriver(any(Employees.class))).thenReturn(null);

        List<Courses> courses = coursesManager.findByDriver(employeesList().get(0));

        assertNull(courses);
        verify(coursesRepo, times(1)).findByDriver(any(Employees.class));
    }

    @Test
    void findByCar() {
        when(coursesRepo.findByCar(any(Car.class))).thenReturn(coursesList());

        List<Courses> courses = coursesManager.findByCar(carList().get(0));

        assertEquals(coursesList().get(0).getId(), courses.get(0).getId());
        assertEquals(coursesList().get(0).getDate(), courses.get(0).getDate());
        assertEquals(coursesList().get(0).getDriver(), courses.get(0).getDriver());

        verify(coursesRepo, times(1)).findByCar(any(Car.class));
    }

    @Test
    void findByCar_not_found() {
        when(coursesRepo.findByCar(any(Car.class))).thenReturn(null);

        List<Courses> courses = coursesManager.findByCar(carList().get(0));

        assertNull(courses);
        verify(coursesRepo, times(1)).findByCar(any(Car.class));
    }

    @Test
    void save() {
        when(coursesRepo.save(any(Courses.class))).thenReturn(coursesList().get(0));

        Courses courses = coursesManager.save(coursesList().get(0));

        assertEquals(courses.getId(), coursesList().get(0).getId());
        assertEquals(courses.getDate(), coursesList().get(0).getDate());
        assertEquals(courses.getCar(), coursesList().get(0).getCar());

        verify(coursesRepo, times(1)).save(any(Courses.class));
    }

    @Test
    void save_null() {
        when(coursesRepo.save(null)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> coursesManager.save(null));
    }

    @Test
    void deleteById() {
        when(coursesRepo.findById(anyLong())).thenReturn(Optional.ofNullable(coursesList().get(0)));

        Optional<Courses> courses = coursesManager.deleteById(1L);

        assertEquals(coursesList().get(0).getId(), courses.get().getId());
        assertEquals(coursesList().get(0).getDriver(), courses.get().getDriver());
        assertEquals(coursesList().get(0).getCar(), courses.get().getCar());

        verify(coursesRepo, times(1)).findById(anyLong());
        verify(coursesRepo, times(1)).deleteById(anyLong());
    }

    @Test
    void deleteById_not_found_id() {
        when(coursesRepo.findById(anyLong())).thenReturn(isNull());

        Optional<Courses> courses = coursesManager.deleteById(1L);

        assertNull(courses);
        verify(coursesRepo, times(1)).findById(anyLong());
        verify(coursesRepo, times(1)).deleteById(anyLong());
    }
}