package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.CarProperties;
import com.pz.KKBus.Staff.Model.Entites.Courses;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.State;
import com.pz.KKBus.Staff.Model.Repositories.CoursesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class CoursesManager {

    private final CoursesRepo coursesRepo;
    private final CarPropertiesManager carPropertiesManager;

    @Autowired
    public CoursesManager(CoursesRepo coursesRepo, CarPropertiesManager carPropertiesManager) {
        this.coursesRepo = coursesRepo;
        this.carPropertiesManager = carPropertiesManager;
    }

    public List<Courses> findAll(){
        return coursesRepo.findAll();
    }

    public Optional<Courses> findById(Long id){
        return coursesRepo.findById(id);
    }

    public List<Courses> findByDriver(Employees employees){
        return coursesRepo.findByDriver(employees);
    }

    public List<Courses> findByCar(Car car){
        return coursesRepo.findByCar(car);
    }

    public Courses save(Courses course) {
       /* Optional<CarProperties> carProperties = carPropertiesManager.findByCarAndDate(course.getCar(), course.getDate());
        if (carProperties.isPresent() && carProperties.get().getState().equals(State.Available)){
            coursesRepo.save(course);
        } else throw new NoSuchFieldException("No available car: " + course.getCar());*/
        return coursesRepo.save(course);
    }

    public Optional<Courses> deleteById(Long id){
        Optional<Courses> deleted = coursesRepo.findById(id);
        coursesRepo.deleteById(id);

        return deleted;
    }
}
