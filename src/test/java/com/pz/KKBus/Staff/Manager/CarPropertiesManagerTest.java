package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.CarProperties;
import com.pz.KKBus.Staff.Model.Enums.State;
import com.pz.KKBus.Staff.Model.Repositories.CarPropertiesRepo;
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
class CarPropertiesManagerTest {

    @Mock
    private CarPropertiesRepo carPropertiesRepo;

    @InjectMocks
    private CarPropertiesManager carPropertiesManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private List<CarProperties> carPropertiesList() {
        List<CarProperties> cars = new ArrayList<>();
        cars.add(new CarProperties((long) 1, carList().get(0), LocalDate.now(), State.Available, "parking1"));
        cars.add(new CarProperties((long) 2, carList().get(1), LocalDate.now(), State.Broken, "parking2"));
        cars.add(new CarProperties((long) 3, carList().get(0), LocalDate.now().plusDays(2), State.Available, "parking1"));
        return cars;
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
        when(carPropertiesRepo.findAll()).thenReturn(carPropertiesList());

        List<CarProperties> cars = carPropertiesRepo.findAll();

        assertEquals(3, cars.size());
        verify(carPropertiesRepo, times(1)).findAll();
    }

    @Test
    void findAll_with_null() {
        when(carPropertiesRepo.findAll()).thenReturn(null);

        List<CarProperties> cars = carPropertiesRepo.findAll();

        assertNull(cars);
        verify(carPropertiesRepo, times(1)).findAll();
    }

    @Test
    void findById() {
        when(carPropertiesRepo.findById(1L)).thenReturn(java.util.Optional.ofNullable(carPropertiesList().get(0)));

        Optional<CarProperties> car = carPropertiesManager.findById(1L);

        assertEquals(carPropertiesList().get(0).getId(), car.get().getId());
        assertEquals(carPropertiesList().get(0).getDate(), car.get().getDate());
        assertEquals(carPropertiesList().get(0).getParking(), car.get().getParking());

        verify(carPropertiesRepo, times(1)).findById(1L);
    }

    @Test
    void findById_not_found() {
        when(carPropertiesRepo.findById(anyLong())).thenReturn(null);

        Optional<CarProperties> car = carPropertiesManager.findById(1L);

        assertNull(car);
        verify(carPropertiesRepo, times(1)).findById(1L);
    }

    @Test
    void findByCar() {
        when(carPropertiesRepo.findByCar(any(Car.class))).thenReturn(carPropertiesList());

        List<CarProperties> car = carPropertiesManager.findByCar(carList().get(1));

        assertEquals(carPropertiesList().get(1).getId(), car.get(1).getId());
        assertEquals(carPropertiesList().get(1).getDate(), car.get(1).getDate());
        assertEquals(carPropertiesList().get(1).getParking(), car.get(1).getParking());

        verify(carPropertiesRepo, times(1)).findByCar(any(Car.class));
    }

    @Test
    void findByCar_nullCar() {
        when(carPropertiesRepo.findByCar(any(Car.class))).thenReturn(null);

        List<CarProperties> car = carPropertiesManager.findByCar(carList().get(1));

        assertNull(car);

        verify(carPropertiesRepo, times(1)).findByCar(any(Car.class));
    }

    @Test
    void findByDate() {
        when(carPropertiesRepo.findByDate(any(LocalDate.class))).thenReturn(carPropertiesList());

        List<CarProperties> car = carPropertiesManager.findByDate(LocalDate.parse("2021-09-12"));

        assertEquals(carPropertiesList().get(1).getId(), car.get(1).getId());
        assertEquals(carPropertiesList().get(1).getDate(), car.get(1).getDate());
        assertEquals(carPropertiesList().get(1).getParking(), car.get(1).getParking());

        verify(carPropertiesRepo, times(1)).findByDate(any(LocalDate.class));
    }

    @Test
    void findByDate_nullDate() {
        when(carPropertiesRepo.findByDate(any(LocalDate.class))).thenReturn(null);

        List<CarProperties> car = carPropertiesManager.findByDate(LocalDate.parse("2021-09-12"));

        assertNull(car);

        verify(carPropertiesRepo, times(1)).findByDate(any(LocalDate.class));
    }

    @Test
    void save() {
        when(carPropertiesRepo.save(any(CarProperties.class))).thenReturn(carPropertiesList().get(0));

        CarProperties car = carPropertiesManager.save(carPropertiesList().get(0));

        assertEquals(car.getId(), carPropertiesList().get(0).getId());
        assertEquals(car.getDate(), carPropertiesList().get(0).getDate());
        assertEquals(car.getCar(), carPropertiesList().get(0).getCar());
        verify(carPropertiesRepo, times(1)).save(any(CarProperties.class));
    }

    @Test
    void save_null() {
        when(carPropertiesRepo.save(null)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> carPropertiesManager.save(null));
    }

    @Test
    void deleteById() {
        when(carPropertiesRepo.findById(1L)).thenReturn(Optional.ofNullable(carPropertiesList().get(0)));

        Optional<CarProperties> car = carPropertiesManager.deleteById(1L);

        assertEquals(carPropertiesList().get(0).getId(), car.get().getId());
        assertEquals(carPropertiesList().get(0).getParking(), car.get().getParking());
        assertEquals(carPropertiesList().get(0).getCar(), car.get().getCar());

        verify(carPropertiesRepo, times(1)).findById(1L);
        verify(carPropertiesRepo, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_not_found_id() {
        when(carPropertiesRepo.findById(1L)).thenReturn(isNull());

        Optional<CarProperties> car = carPropertiesManager.deleteById(1L);

        assertNull(car);
        verify(carPropertiesRepo, times(1)).findById(1L);
        verify(carPropertiesRepo, times(1)).deleteById(1L);
    }
}