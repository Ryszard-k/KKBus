package com.pz.KKBus.Staff.Manager;

import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowice;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import com.pz.KKBus.Customer.Model.Enums.Route;
import com.pz.KKBus.Staff.Model.Entites.Car;
import com.pz.KKBus.Staff.Model.Entites.CarProperties;
import com.pz.KKBus.Staff.Model.Entites.Courses.Courses;
import com.pz.KKBus.Staff.Model.Entites.Courses.Report;
import com.pz.KKBus.Staff.Model.Entites.Employees;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Enums.State;
import com.pz.KKBus.Staff.Model.Repositories.CarPropertiesRepo;
import com.pz.KKBus.Staff.Model.Repositories.CarRepo;
import com.pz.KKBus.Staff.Model.Repositories.Courses.CoursesRepo;
import com.pz.KKBus.Staff.Model.Repositories.EmployeesRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

    public List<Report> findByDate(LocalDate date, Route route){
        List<Report> reports = coursesRepo.findByDate(date, route);
        if (!reports.isEmpty()){
            return reports;
        } else
            return List.of();
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
