package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.CarProperties;
import com.pz.KKBus.Staff.Model.Repositories.CarPropertiesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class CarPropertiesManager {

    private final CarPropertiesRepo carPropertiesRepo;

    @Autowired
    public CarPropertiesManager(CarPropertiesRepo carPropertiesRepo) {
        this.carPropertiesRepo = carPropertiesRepo;
    }

    public List<CarProperties> findAll(){
        return carPropertiesRepo.findAll();
    }

    public Optional<CarProperties> findById(Long id){
        return carPropertiesRepo.findById(id);
    }

    public List<CarProperties> findByCar(Car car){
        return carPropertiesRepo.findByCar(car);
    }

    public List<CarProperties> findByDate(LocalDate date){
        return carPropertiesRepo.findByDate(date);
    }

    public Optional<CarProperties> findByCarAndDate(Car car, LocalDate date){
        return carPropertiesRepo.findByCarAndDate(car, date);
    }

    public CarProperties save(CarProperties carProperties){
        return carPropertiesRepo.save(carProperties);
    }

    public Optional<CarProperties> deleteById(Long id){
        Optional<CarProperties> deleted = carPropertiesRepo.findById(id);
        carPropertiesRepo.deleteById(id);

        return deleted;
    }
}
