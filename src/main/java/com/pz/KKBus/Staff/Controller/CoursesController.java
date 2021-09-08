package com.pz.KKBus.Staff.Controller;

import com.pz.KKBus.Customer.Manager.Schedules.KatowiceToKrakowDepartureManager;
import com.pz.KKBus.Customer.Manager.Schedules.KrakowToKatowiceDepartureManager;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KatowiceToKrakowDeparture;
import com.pz.KKBus.Customer.Model.Entites.Schedules.KrakowToKatowiceDeparture;
import com.pz.KKBus.Staff.Manager.CarManager;
import com.pz.KKBus.Staff.Manager.CarPropertiesManager;
import com.pz.KKBus.Staff.Manager.CoursesManager;
import com.pz.KKBus.Staff.Manager.EmployeesManager;
import com.pz.KKBus.Staff.Model.Entites.*;
import com.pz.KKBus.Staff.Model.Entites.Courses.Courses;
import com.pz.KKBus.Staff.Model.Enums.Role;
import com.pz.KKBus.Staff.Model.Enums.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin
@RequestMapping("/courses")
public class CoursesController implements CrudControllerMethods<Courses>{

    private final CoursesManager coursesManager;
    private final EmployeesManager employeesManager;
    private final CarManager carManager;
    private final CarPropertiesManager carPropertiesManager;
    private final KatowiceToKrakowDepartureManager katowiceToKrakowDepartureManager;
    private final KrakowToKatowiceDepartureManager krakowToKatowiceDepartureManager;

    @Autowired
    public CoursesController(CoursesManager coursesManager, EmployeesManager employeesManager, CarManager carManager, CarPropertiesManager carPropertiesManager, KatowiceToKrakowDepartureManager katowiceToKrakowDepartureManager, KrakowToKatowiceDepartureManager krakowToKatowiceDepartureManager) {
        this.coursesManager = coursesManager;
        this.employeesManager = employeesManager;
        this.carManager = carManager;
        this.carPropertiesManager = carPropertiesManager;
        this.katowiceToKrakowDepartureManager = katowiceToKrakowDepartureManager;
        this.krakowToKatowiceDepartureManager = krakowToKatowiceDepartureManager;
    }

    @GetMapping()
    public ResponseEntity getAll(){
        List<Courses> found = coursesManager.findAll();
        if(found.isEmpty()){
            return new ResponseEntity<>("Repository is empty!", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @Override
    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable Long id) {
        Optional<Courses> founded = coursesManager.findById(id);
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else
            return new ResponseEntity<>(founded, HttpStatus.OK);
    }

    @GetMapping("/driver/{id}")
    public ResponseEntity findByDriver(@PathVariable Long id){
        Optional<Employees> founded = employeesManager.findById(id);
        if(founded.isPresent() && founded.get().getRole().equals(Role.Driver)){
            List<Courses> foundedCourses = coursesManager.findByDriver(founded.get());
            return new ResponseEntity<>(foundedCourses, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/car/{id}")
    public ResponseEntity findByCar(@PathVariable Long id){
        Optional<Car> founded = carManager.findById(id);
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else {
            List<Courses> foundedCourses = coursesManager.findByCar(founded.get());
            return new ResponseEntity<>(foundedCourses, HttpStatus.OK);
        }
    }

    @Override
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Object> add(@RequestBody Courses object) {
            Optional<CarProperties> carProperties = carPropertiesManager.findByCarAndDate(object.getCar(), object.getDate());
            Optional<Employees> driver = employeesManager.findById(object.getDriver().getId());
            if (carProperties.isPresent() && carProperties.get().getState().equals(State.Available) &&
                    driver.get().getRole().equals(Role.Driver)) {
                switch (object.getRoute()){
                    case KatowiceToKrakow:
                        List<KatowiceToKrakowDeparture> kat = katowiceToKrakowDepartureManager
                                .findByMonFriDepartureContains(object.getDepartureTime());
                        kat.addAll(katowiceToKrakowDepartureManager.findBySatSunDepartureContains(object.getDepartureTime()));
                        if (!kat.isEmpty()){
                            coursesManager.save(object);
                        } else {
                            return new ResponseEntity<>("Departure time is not properly", HttpStatus.BAD_REQUEST);
                        }
                        break;

                    case KrakowToKatowice:
                        List<KrakowToKatowiceDeparture> krk = krakowToKatowiceDepartureManager
                                .findByMonFriDepartureContains(object.getDepartureTime());
                        krk.addAll(krakowToKatowiceDepartureManager.findBySatSunDepartureContains(object.getDepartureTime()));
                        if (!krk.isEmpty()){
                            coursesManager.save(object);
                        } else {
                            return new ResponseEntity<>("Departure time is not properly", HttpStatus.BAD_REQUEST);
                        }
                        break;
                }
                    return new ResponseEntity<>(object, HttpStatus.CREATED);
            } else
                return new ResponseEntity<>("Empty input data", HttpStatus.BAD_REQUEST);
    }

    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteById(@PathVariable Long id) {
        Optional<Courses> courses = coursesManager.findById(id);
        if (courses.isPresent()) {
            coursesManager.deleteById(id);
            return new ResponseEntity<>(courses,HttpStatus.OK);
        } else
            return new ResponseEntity<>("Not found course to delete!", HttpStatus.NOT_FOUND);
    }

}
