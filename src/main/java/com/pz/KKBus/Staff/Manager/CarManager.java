package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Repositories.CarRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CarManager {

    private final CarRepo carRepo;

    @Autowired
    public CarManager(CarRepo carRepo) {
        this.carRepo = carRepo;
    }

    public List<Car> findAll(){
        return carRepo.findAll();
    }

    public Optional<Car> findById(Long id){
        return carRepo.findById(id);
    }

    public Car save(Car car){
        return carRepo.save(car);
    }

    public Optional<Car> deleteById(Long id){
        Optional<Car> deleted = carRepo.findById(id);
        carRepo.deleteById(id);

        return deleted;
    }
}
