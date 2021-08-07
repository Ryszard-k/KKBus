package com.pz.KKBus.Staff.Controller;

import com.pz.KKBus.Staff.Manager.CarManager;
import com.pz.KKBus.Staff.Manager.CarPropertiesManager;
import com.pz.KKBus.Staff.Manager.CoursesManager;
import com.pz.KKBus.Staff.Manager.EmployeesManager;
import com.pz.KKBus.Staff.Model.Entites.*;
import com.pz.KKBus.Staff.Model.Enums.State;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/courses")
public class CoursesController implements CrudControllerMethods<Courses>{

    private final CoursesManager coursesManager;
    private final EmployeesManager employeesManager;
    private final CarManager carManager;
    private final CarPropertiesManager carPropertiesManager;

    @Autowired
    public CoursesController(CoursesManager coursesManager, EmployeesManager employeesManager, CarManager carManager, CarPropertiesManager carPropertiesManager) {
        this.coursesManager = coursesManager;
        this.employeesManager = employeesManager;
        this.carManager = carManager;
        this.carPropertiesManager = carPropertiesManager;
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
        if(founded.isEmpty()){
            return new ResponseEntity<>("Bad id", HttpStatus.NOT_FOUND);
        } else {
            List<Courses> foundedCourses = coursesManager.findByDriver(founded.get());
            return new ResponseEntity<>(foundedCourses, HttpStatus.OK);
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
            if (carProperties.isPresent() && carProperties.get().getState().equals(State.Available)) {
                coursesManager.save(object);
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
