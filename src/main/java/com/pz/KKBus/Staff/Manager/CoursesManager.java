package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.Courses;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Repositories.CoursesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CoursesManager {

    private final CoursesRepo coursesRepo;

    @Autowired
    public CoursesManager(CoursesRepo coursesRepo) {
        this.coursesRepo = coursesRepo;
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
       return coursesRepo.save(course);
    }

    public Optional<Courses> deleteById(Long id){
        Optional<Courses> deleted = coursesRepo.findById(id);
        coursesRepo.deleteById(id);

        return deleted;
    }
}
