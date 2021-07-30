package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Customer.Model.Entites.Reservation;
import com.pz.KKBus.Customer.Model.Entites.Reward;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.Schedule;
import com.pz.KKBus.Staff.Model.Repositories.CarRepo;
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
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
class CarManagerTest {

    @Mock
    private CarRepo carRepo;

    @InjectMocks
    private CarManager carManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<Car> carList() {
        List<Car> cars = new ArrayList<>();
        cars.add(new Car((long) 1, "Mercedes", "Benz", 30, LocalDate.parse("2000-02-23")));
        cars.add(new Car((long) 2, "Volksvagen", "Crafter", 32, LocalDate.parse("2008-06-23")));
        cars.add(new Car((long) 3, "Temsa", "Safari", 64, LocalDate.parse("2010-04-01")));
        return cars;
    }

    @Test
    void findAll() {
        when(carRepo.findAll()).thenReturn(carList());

        List<Car> cars = carRepo.findAll();

        assertEquals(3, cars.size());
        verify(carRepo, times(1)).findAll();
    }

    @Test
    void findAll_with_null() {
        when(carRepo.findAll()).thenReturn(null);

        List<Car> cars = carRepo.findAll();

        assertNull(cars);
        verify(carRepo, times(1)).findAll();
    }

    @Test
    void findById() {
        when(carRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(carList().get(0)));

        Optional<Car> car = carManager.findById(1L);

        assertEquals(carList().get(0).getId(), car.get().getId());
        assertEquals(carList().get(0).getBrand(), car.get().getBrand());
        assertEquals(carList().get(0).getManufactureYear(), car.get().getManufactureYear());

        verify(carRepo, times(1)).findById(1L);
    }

    @Test
    void findById_not_found() {
        when(carRepo.findById(anyLong())).thenReturn(null);

        Optional<Car> car = carManager.findById(1L);

        assertNull(car);
        verify(carRepo, times(1)).findById(1L);
    }

    @Test
    void save() {
        when(carRepo.save(any(Car.class))).thenReturn(carList().get(0));

        Car car = carManager.save(carList().get(0));

        assertEquals(car.getId(), carList().get(0).getId());
        assertEquals(car.getBrand(), carList().get(0).getBrand());
        assertEquals(car.getManufactureYear(), carList().get(0).getManufactureYear());
        verify(carRepo, times(1)).save(any(Car.class));
    }

    @Test
    void save_null() {
        when(carRepo.save(null)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> carManager.save(null));
    }

    @Test
    void deleteById() {
        when(carRepo.findById(1L)).thenReturn(Optional.ofNullable(carList().get(0)));

        Optional<Car> car = carManager.deleteById(1L);

        assertEquals(carList().get(0).getId(), car.get().getId());
        assertEquals(carList().get(0).getBrand(), car.get().getBrand());
        assertEquals(carList().get(0).getManufactureYear(), car.get().getManufactureYear());

        verify(carRepo, times(1)).findById(1L);
        verify(carRepo, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_not_found_id() {
        when(carRepo.findById(1L)).thenReturn(isNull());

        Optional<Car> car = carManager.deleteById(1L);

        assertNull(car);
        verify(carRepo, times(1)).findById(1L);
        verify(carRepo, times(1)).deleteById(1L);
    }
}