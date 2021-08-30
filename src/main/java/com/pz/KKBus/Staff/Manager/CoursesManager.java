package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Customer.Model.Enums.Route;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.Courses.Courses;
import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Repositories.Courses.CoursesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    public List<Courses> findByDate(LocalDate date, Route route){
        List<Courses> courses = coursesRepo.findByDateAndRoute(date, route);
        if (!courses.isEmpty()){
            return courses;
        } else
            return List.of();
    }

    public Courses save(Courses course) {
       return coursesRepo.save(course);
    }

    public void update(Long id, Report report){
        Optional<Courses> courses = coursesRepo.findById(id);
        if (courses.isPresent()){
            courses.get().setReport(report);
            coursesRepo.save(courses.get());
        }
    }

    public Optional<Courses> deleteById(Long id){
        Optional<Courses> deleted = coursesRepo.findById(id);
        coursesRepo.deleteById(id);

        return deleted;
    }
}
